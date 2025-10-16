CREATE TABLE banned_words (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    word VARCHAR(255) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE user_flags (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id SERIAL,
    field VARCHAR(255),
    action VARCHAR(255),
    attempted_text TEXT,
    targetEndpoint TEXT,
    details TEXT,
    matched_words TEXT,
    created_at timestamp
);