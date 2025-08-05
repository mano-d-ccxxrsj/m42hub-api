
CREATE TABLE project_status (
    project_status_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);


CREATE TABLE project_complexity (
    project_complexity_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    hex_color VARCHAR(9),
    description TEXT
);

CREATE TABLE projects (
    project_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    summary VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    status_id INT REFERENCES project_status(project_status_id) NOT NULL DEFAULT 1,
    complexity_id INT REFERENCES project_complexity(project_complexity_id) NOT NULL,
    image_url TEXT,
    start_date DATE NOT NULL,
    end_date DATE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE project_tools (
    project_tools_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    hex_color VARCHAR(9),
    description TEXT
);

CREATE TABLE projects_tools (
    projects_tools_id SERIAL PRIMARY KEY,
    project_id INT NOT NULL REFERENCES projects(project_id),
    project_tools_id INT NOT NULL REFERENCES project_tools(project_tools_id),
    UNIQUE (project_id, project_tools_id)
);

CREATE TABLE project_topics (
    project_topics_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    hex_color VARCHAR(9),
    description TEXT
);

CREATE TABLE projects_topics (
    projects_topics_id SERIAL PRIMARY KEY,
    project_id INT NOT NULL REFERENCES projects(project_id),
    project_topics_id INT NOT NULL REFERENCES project_topics(project_topics_id),
    UNIQUE (project_id, project_topics_id)
);

CREATE TABLE project_roles (
    project_role_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE project_member_status (
    project_member_status_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE project_members (
    project_members_id SERIAL PRIMARY KEY,
    is_manager BOOLEAN NOT NULL DEFAULT FALSE,
    project_id INT REFERENCES projects(project_id) NOT NULL,
    project_role_id INT REFERENCES project_roles(project_role_id) NOT NULL,
    user_id INT REFERENCES users(user_id) NOT NULL,
    status_id INT REFERENCES project_member_status(project_member_status_id) NOT NULL DEFAULT 1,
    application_message VARCHAR(255),
    application_feedback VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    UNIQUE (project_id, user_id)
);

CREATE TABLE project_unfilled_roles (
    project_unfilled_roles_id SERIAL PRIMARY KEY,
    project_id INT REFERENCES projects(project_id) ON DELETE SET NULL,
    project_role_id INT REFERENCES project_roles(project_role_id) ON DELETE SET NULL,
    UNIQUE (project_id, project_role_id)
);