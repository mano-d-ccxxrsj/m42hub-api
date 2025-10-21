CREATE TABLE abuse_categories (
    abuse_category_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

INSERT INTO abuse_categories (abuse_category_id, name, description) VALUES
(1, 'Spam', 'Conteúdo indesejado ou repetitivo'),
(2, 'Abuso', 'Linguagem ofensiva, assédio ou comportamento abusivo'),
(3, 'Informação falsa', 'Conteúdo enganoso ou fake news'),
(4, 'Violação de direitos autorais', 'Uso não autorizado de material protegido'),
(5, 'Conteúdo impróprio', 'Material ofensivo, sexual ou violento'),
(6, 'Outro', 'Outro motivo não listado');

CREATE TABLE abuse_status (
    abuse_status_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

INSERT INTO abuse_status (abuse_status_id, name, description) VALUES
(1, 'OPEN', 'Denúncia aberta'),
(2, 'IN_REVIEW', 'Denúncia em análise'),
(3, 'CLOSED', 'Denúncia fechada'),
(4, 'RESOLVED', 'Denúncia resolvida');

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
