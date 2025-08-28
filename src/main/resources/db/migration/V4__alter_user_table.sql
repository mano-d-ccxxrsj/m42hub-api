ALTER TABLE users
ADD COLUMN biography VARCHAR(255),
ADD COLUMN discord VARCHAR(255),
ADD COLUMN linkedin VARCHAR(255),
ADD COLUMN github VARCHAR(255),
ADD COLUMN personal_website VARCHAR(255);

CREATE TABLE user_interest_project_roles (
    user_interest_project_roles_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id) ON DELETE SET NULL,
    project_role_id INT REFERENCES project_roles(project_role_id) ON DELETE SET NULL,
    UNIQUE (user_id, project_role_id)
);