CREATE TABLE abuse_categories (
    abuse_category_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    label VARCHAR(100) NOT NULL,
    description TEXT
);

INSERT INTO abuse_categories (abuse_category_id, name, label, description) VALUES
(1, 'SPAM', 'Spam', 'Conteúdo indesejado ou repetitivo'),
(2, 'ABUSE', 'Abuso', 'Linguagem ofensiva, assédio ou comportamento abusivo'),
(3, 'FALSE_INFORMATION', 'Informação falsa', 'Conteúdo enganoso ou fake news'),
(4, 'COPYRIGHT_VIOLATION', 'Violação de direitos autorais', 'Uso não autorizado de material protegido'),
(5, 'INAPPROPRIATE_CONTENT', 'Conteúdo impróprio', 'Material ofensivo, sexual ou violento'),
(6, 'OTHER', 'Outro', 'Outro motivo não listado');

CREATE TABLE abuse_status (
    abuse_status_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    label VARCHAR(100) NOT NULL,
    description TEXT
);

INSERT INTO abuse_status (abuse_status_id, name, label, description) VALUES
(1, 'OPEN', 'Aberta', 'Denúncia aberta'),
(2, 'IN_REVIEW', 'Em análise', 'Denúncia em análise'),
(3, 'CLOSED', 'Fechada', 'Denúncia fechada'),
(4, 'RESOLVED', 'Resolvida', 'Denúncia resolvida');

CREATE TABLE abuses (
    abuse_id BIGSERIAL PRIMARY KEY,
    reporter_id BIGINT NOT NULL REFERENCES users(user_id),
    target_type VARCHAR(50) NOT NULL,
    target_id BIGINT NOT NULL,
    reason_category_id BIGINT REFERENCES abuse_categories(abuse_category_id),
    reason_text TEXT NOT NULL,
    status_id BIGINT NOT NULL REFERENCES abuse_status(abuse_status_id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    resolved_at TIMESTAMP
);
