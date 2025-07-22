CREATE TABLE permissions (
    permission_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE system_roles (
    system_role_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    profile_pic_url TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    system_role_id INT REFERENCES system_roles(system_role_id) ON DELETE SET NULL,
    last_login timestamp DEFAULT NULL,
    created_at timestamp,
    updated_at timestamp
);

CREATE TABLE user_logs (
    user_log_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(user_id),
    activity_type VARCHAR(100),
    description TEXT NOT NULL,
    created_at timestamp
);

CREATE TABLE roles_permissions (
    role_permission_id SERIAL PRIMARY KEY,
    system_role_id INT NOT NULL REFERENCES system_roles(system_role_id),
    permission_id INT NOT NULL REFERENCES permissions(permission_id),
    UNIQUE (system_role_id, permission_id)
);

INSERT INTO system_roles (
    name,
    description
) VALUES
('ADMIN', 'Usuário que administra o sistema'),
('USER', 'Usuário comum'),
('MODERATOR', 'Usuário que tem algumas permissões para moderação')