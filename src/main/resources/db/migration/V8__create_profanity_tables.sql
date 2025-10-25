CREATE TABLE banned_words (
    banned_word_id UUID PRIMARY KEY,
    word VARCHAR(255) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE user_flags (
    user_flag_id UUID PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(user_id),
    field VARCHAR(255),
    action VARCHAR(255),
    attempted_text TEXT,
    targetEndpoint TEXT,
    details TEXT,
    matched_words TEXT,
    created_at timestamp
);