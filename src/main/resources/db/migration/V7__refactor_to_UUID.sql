-- Adicionamos colunas UUID a todas as tabelas com nomes TEMPORÁRIOS
ALTER TABLE users ADD COLUMN user_uuid_new UUID;
ALTER TABLE projects ADD COLUMN project_uuid_new UUID;
ALTER TABLE contributions ADD COLUMN contribution_uuid_new UUID;
ALTER TABLE donations ADD COLUMN donation_uuid_new UUID;
ALTER TABLE system_roles ADD COLUMN system_role_uuid_new UUID;
ALTER TABLE permissions ADD COLUMN permission_uuid_new UUID;

-- Gera UUIDs para registros existentes usando conversão de hash MD5
-- Aqui tem uma obs: https://stackoverflow.com/questions/65991377/update-in-postgresql-generates-same-uuid
-- Vi sobre a existência de uuid_generate_v4() mas me parece antigo.
-- A forma utilizada, usando CAST e MD5 pode ser consultada aqui: https://www.channable.com/tech/postgres-performance-lessons-2
UPDATE users SET user_uuid_new = CAST(MD5(user_id::text || 'user') AS UUID);
UPDATE projects SET project_uuid_new = CAST(MD5(project_id::text || 'project') AS UUID);
UPDATE contributions SET contribution_uuid_new = CAST(MD5(contribution_id::text || 'contribution') AS UUID);
UPDATE donations SET donation_uuid_new = CAST(MD5(donation_id::text || 'donation') AS UUID);
UPDATE system_roles SET system_role_uuid_new = CAST(MD5(system_role_id::text || 'system_role') AS UUID);
UPDATE permissions SET permission_uuid_new = CAST(MD5(permission_id::text || 'permission') AS UUID);

-- Altera as colunas UUID para NÃO NULAS
ALTER TABLE users ALTER COLUMN user_uuid_new SET NOT NULL;
ALTER TABLE projects ALTER COLUMN project_uuid_new SET NOT NULL;
ALTER TABLE contributions ALTER COLUMN contribution_uuid_new SET NOT NULL;
ALTER TABLE donations ALTER COLUMN donation_uuid_new SET NOT NULL;
ALTER TABLE system_roles ALTER COLUMN system_role_uuid_new SET NOT NULL;
ALTER TABLE permissions ALTER COLUMN permission_uuid_new SET NOT NULL;

ALTER TABLE users ADD COLUMN system_role_uuid_new UUID;
UPDATE users u SET system_role_uuid_new = sr.system_role_uuid_new
FROM system_roles sr WHERE u.system_role_id = sr.system_role_id;
ALTER TABLE users ALTER COLUMN system_role_uuid_new SET NOT NULL;

ALTER TABLE contributions ADD COLUMN user_uuid_new UUID;
UPDATE contributions c SET user_uuid_new = u.user_uuid_new
FROM users u WHERE c.user_id = u.user_id;
ALTER TABLE contributions ALTER COLUMN user_uuid_new SET NOT NULL;

ALTER TABLE donations ADD COLUMN user_uuid_new UUID;
UPDATE donations d SET user_uuid_new = u.user_uuid_new
FROM users u WHERE d.user_id = u.user_id;
ALTER TABLE donations ALTER COLUMN user_uuid_new SET NOT NULL;

ALTER TABLE project_members
    ADD COLUMN user_uuid_new UUID,
    ADD COLUMN project_uuid_new UUID;

UPDATE project_members pm SET
    user_uuid_new = u.user_uuid_new,
    project_uuid_new = p.project_uuid_new
FROM users u, projects p
WHERE pm.user_id = u.user_id AND pm.project_id = p.project_id;

ALTER TABLE project_members
    ALTER COLUMN user_uuid_new SET NOT NULL,
    ALTER COLUMN project_uuid_new SET NOT NULL;

-- Cria tabelas de junção para relacionamentos
CREATE TABLE user_interest_roles (
    user_interest_roles_id SERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    project_role_id INTEGER NOT NULL,
    UNIQUE (user_id, project_role_id)
);

CREATE TABLE system_role_permissions (
    system_role_permissions_id SERIAL PRIMARY KEY,
    system_role_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    UNIQUE (system_role_id, permission_id)
);

CREATE TABLE projects_tools_new (
    projects_tools_id SERIAL PRIMARY KEY,
    project_id UUID NOT NULL,
    project_tools_id INTEGER NOT NULL,
    UNIQUE (project_id, project_tools_id)
);

CREATE TABLE projects_topics_new (
    projects_topics_id SERIAL PRIMARY KEY,
    project_id UUID NOT NULL,
    project_topics_id INTEGER NOT NULL,
    UNIQUE (project_id, project_topics_id)
);

CREATE TABLE project_unfilled_roles_new (
    project_unfilled_roles_id SERIAL PRIMARY KEY,
    project_id UUID NOT NULL,
    project_role_id INTEGER NOT NULL,
    UNIQUE (project_id, project_role_id)
);

-- Migra dados para novas tabelas de junção com geração de UUID
INSERT INTO user_interest_roles (user_id, project_role_id)
SELECT
    u.user_uuid_new,
    pr.project_role_id
FROM user_interest_project_roles uipr
    JOIN users u ON uipr.user_id = u.user_id
    JOIN project_roles pr ON uipr.project_role_id = pr.project_role_id;

INSERT INTO system_role_permissions (system_role_id, permission_id)
SELECT
    sr.system_role_uuid_new,
    p.permission_uuid_new
FROM roles_permissions rp
    JOIN system_roles sr ON rp.system_role_id = sr.system_role_id
    JOIN permissions p ON rp.permission_id = p.permission_id;

INSERT INTO projects_tools_new (project_id, project_tools_id)
SELECT
    p.project_uuid_new,
    pt.project_tools_id
FROM projects_tools pt_old
    JOIN projects p ON pt_old.project_id = p.project_id
    JOIN project_tools pt ON pt_old.project_tools_id = pt.project_tools_id;

INSERT INTO projects_topics_new (project_id, project_topics_id)
SELECT
    p.project_uuid_new,
    pt.project_topics_id
FROM projects_topics pt_old
    JOIN projects p ON pt_old.project_id = p.project_id
    JOIN project_topics pt ON pt_old.project_topics_id = pt.project_topics_id;

INSERT INTO project_unfilled_roles_new (project_id, project_role_id)
SELECT
    p.project_uuid_new,
    pr.project_role_id
FROM project_unfilled_roles pur
    JOIN projects p ON pur.project_id = p.project_id
    JOIN project_roles pr ON pur.project_role_id = pr.project_role_id;

-- Remove/apaga as chaves estrangeira antigas do tipo constraint PRIMEIRO (antes de remover as chaves primárias)
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_system_role_id_fkey;
ALTER TABLE contributions DROP CONSTRAINT IF EXISTS contributions_user_id_fkey;
ALTER TABLE donations DROP CONSTRAINT IF EXISTS donations_user_id_fkey;
ALTER TABLE project_members DROP CONSTRAINT IF EXISTS project_members_user_id_fkey;
ALTER TABLE project_members DROP CONSTRAINT IF EXISTS project_members_project_id_fkey;
ALTER TABLE projects DROP CONSTRAINT IF EXISTS projects_status_id_fkey;
ALTER TABLE projects DROP CONSTRAINT IF EXISTS projects_complexity_id_fkey;
ALTER TABLE project_members DROP CONSTRAINT IF EXISTS project_members_project_role_id_fkey;
ALTER TABLE project_members DROP CONSTRAINT IF EXISTS project_members_status_id_fkey;

ALTER TABLE users DROP CONSTRAINT users_pkey CASCADE;
ALTER TABLE users DROP COLUMN user_id;
ALTER TABLE users RENAME COLUMN user_uuid_new TO user_id;
ALTER TABLE users ADD PRIMARY KEY (user_id);

ALTER TABLE users DROP COLUMN system_role_id;
ALTER TABLE users RENAME COLUMN system_role_uuid_new TO system_role_id;

ALTER TABLE projects DROP CONSTRAINT projects_pkey CASCADE;
ALTER TABLE projects DROP COLUMN project_id;
ALTER TABLE projects RENAME COLUMN project_uuid_new TO project_id;
ALTER TABLE projects ADD PRIMARY KEY (project_id);

ALTER TABLE contributions DROP CONSTRAINT contributions_pkey CASCADE;
ALTER TABLE contributions DROP COLUMN contribution_id;
ALTER TABLE contributions RENAME COLUMN contribution_uuid_new TO contribution_id;
ALTER TABLE contributions ADD PRIMARY KEY (contribution_id);

ALTER TABLE contributions DROP COLUMN user_id;
ALTER TABLE contributions RENAME COLUMN user_uuid_new TO user_id;

ALTER TABLE donations DROP CONSTRAINT donations_pkey CASCADE;
ALTER TABLE donations DROP COLUMN donation_id;
ALTER TABLE donations RENAME COLUMN donation_uuid_new TO donation_id;
ALTER TABLE donations ADD PRIMARY KEY (donation_id);

ALTER TABLE donations DROP COLUMN user_id;
ALTER TABLE donations RENAME COLUMN user_uuid_new TO user_id;

ALTER TABLE system_roles DROP CONSTRAINT system_roles_pkey CASCADE;
ALTER TABLE system_roles DROP COLUMN system_role_id;
ALTER TABLE system_roles RENAME COLUMN system_role_uuid_new TO system_role_id;
ALTER TABLE system_roles ADD PRIMARY KEY (system_role_id);

ALTER TABLE permissions DROP CONSTRAINT permissions_pkey CASCADE;
ALTER TABLE permissions DROP COLUMN permission_id;
ALTER TABLE permissions RENAME COLUMN permission_uuid_new TO permission_id;
ALTER TABLE permissions ADD PRIMARY KEY (permission_id);

ALTER TABLE project_members DROP COLUMN user_id;
ALTER TABLE project_members DROP COLUMN project_id;
ALTER TABLE project_members RENAME COLUMN user_uuid_new TO user_id;
ALTER TABLE project_members RENAME COLUMN project_uuid_new TO project_id;

-- Remove as tabelas de junção antigas
DROP TABLE user_interest_project_roles;
DROP TABLE roles_permissions;
DROP TABLE projects_tools;
DROP TABLE projects_topics;
DROP TABLE project_unfilled_roles;

ALTER TABLE projects_tools_new RENAME TO projects_tools;
ALTER TABLE projects_topics_new RENAME TO projects_topics;
ALTER TABLE project_unfilled_roles_new RENAME TO project_unfilled_roles;

-- Adiciona constraint para chaves estrangeiras
ALTER TABLE users
    ADD CONSTRAINT fk_users_system_role
        FOREIGN KEY (system_role_id) REFERENCES system_roles(system_role_id);

ALTER TABLE contributions
    ADD CONSTRAINT fk_contributions_user
        FOREIGN KEY (user_id) REFERENCES users(user_id);

ALTER TABLE donations
    ADD CONSTRAINT fk_donations_user
        FOREIGN KEY (user_id) REFERENCES users(user_id);

-- Adiciona chaves estrangeiras para tabelas de junção
ALTER TABLE project_members
    ADD CONSTRAINT fk_project_members_user
        FOREIGN KEY (user_id) REFERENCES users(user_id),
    ADD CONSTRAINT fk_project_members_project
        FOREIGN KEY (project_id) REFERENCES projects(project_id),
    ADD CONSTRAINT fk_project_members_role
        FOREIGN KEY (project_role_id) REFERENCES project_roles(project_role_id),
    ADD CONSTRAINT fk_project_members_status
        FOREIGN KEY (status_id) REFERENCES project_member_status(project_member_status_id);

ALTER TABLE projects
    ADD CONSTRAINT fk_projects_status
        FOREIGN KEY (status_id) REFERENCES project_status(project_status_id),
    ADD CONSTRAINT fk_projects_complexity
        FOREIGN KEY (complexity_id) REFERENCES project_complexity(project_complexity_id);

ALTER TABLE user_interest_roles
    ADD CONSTRAINT fk_user_interest_roles_user
        FOREIGN KEY (user_id) REFERENCES users(user_id),
    ADD CONSTRAINT fk_user_interest_roles_role
        FOREIGN KEY (project_role_id) REFERENCES project_roles(project_role_id);

ALTER TABLE system_role_permissions
    ADD CONSTRAINT fk_system_role_permissions_role
        FOREIGN KEY (system_role_id) REFERENCES system_roles(system_role_id),
    ADD CONSTRAINT fk_system_role_permissions_permission
        FOREIGN KEY (permission_id) REFERENCES permissions(permission_id);

ALTER TABLE projects_tools
    ADD CONSTRAINT fk_projects_tools_project
        FOREIGN KEY (project_id) REFERENCES projects(project_id),
    ADD CONSTRAINT fk_projects_tools_tool
        FOREIGN KEY (project_tools_id) REFERENCES project_tools(project_tools_id);

ALTER TABLE projects_topics
    ADD CONSTRAINT fk_projects_topics_project
        FOREIGN KEY (project_id) REFERENCES projects(project_id),
    ADD CONSTRAINT fk_projects_topics_topic
        FOREIGN KEY (project_topics_id) REFERENCES project_topics(project_topics_id);

ALTER TABLE project_unfilled_roles
    ADD CONSTRAINT fk_project_unfilled_roles_project
        FOREIGN KEY (project_id) REFERENCES projects(project_id),
    ADD CONSTRAINT fk_project_unfilled_roles_role
        FOREIGN KEY (project_role_id) REFERENCES project_roles(project_role_id);