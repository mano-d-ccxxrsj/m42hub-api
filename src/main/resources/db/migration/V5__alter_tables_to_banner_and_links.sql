ALTER TABLE projects
ADD COLUMN discord VARCHAR(255),
ADD COLUMN github VARCHAR(255),
ADD COLUMN project_website VARCHAR(255);

ALTER TABLE users
ADD COLUMN profile_banner_url TEXT;