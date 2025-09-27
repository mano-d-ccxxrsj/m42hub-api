CREATE TABLE contribution_statuses (
    contribution_status_id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    label VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE contribution_types (
    contribution_type_id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    label VARCHAR(100) NOT NULL UNIQUE,
    hex_color VARCHAR(9),
    description TEXT
);

CREATE TABLE donation_statuses (
    donation_status_id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    label VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE donation_types (
    donation_type_id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    label VARCHAR(100) NOT NULL UNIQUE,
    hex_color VARCHAR(9),
    description TEXT
);

CREATE TABLE donation_platforms (
    donation_platform_id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    label VARCHAR(100) NOT NULL UNIQUE,
    hex_color VARCHAR(9),
    description TEXT
);

CREATE TABLE contributions (
    contribution_id UUID PRIMARY KEY,
    user_id INT REFERENCES users(user_id) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    status_id INT REFERENCES contribution_statuses(contribution_status_id) NOT NULL DEFAULT 1,
    type_id INT REFERENCES contribution_types(contribution_type_id) NOT NULL DEFAULT 1,
    submitted_at DATE NOT NULL,
    approved_at DATE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE donations (
    donation_id UUID PRIMARY KEY,
    user_id INT REFERENCES users(user_id) NOT NULL,
    name VARCHAR(255) NOT NULL,
    summary VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    amount DECIMAL(12, 4),
    currency CHAR(3) DEFAULT 'BRL',
    status_id INT REFERENCES donation_statuses(donation_status_id) NOT NULL DEFAULT 1,
    type_id INT REFERENCES donation_types(donation_type_id) NOT NULL DEFAULT 1,
    platform_id INT REFERENCES donation_platforms(donation_platform_id),
    image_url TEXT,
    donated_at DATE NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_contributions_user_id ON contributions(user_id);
CREATE INDEX idx_donations_user_id ON donations(user_id);

INSERT INTO contribution_statuses (
    name,
    label,
    description
) VALUES
('PENDING', 'Pendente', 'Aguardando para ser lançado/liberado'),
('IN_PROGRESS', 'Em progresso', 'Já está sendo desenvolvido'),
('IN_VALIDATION', 'Em validação', 'Está passando por validação antes da aprovação final.'),
('APPROVED', 'Aprovado', 'Foi aprovado para realizar integração'),
('DONE', 'Concluído', 'Foi integrado no sistema'),
('REFUSED', 'Recusado','Recusado por algum motivo'),
('CANCELED', 'Cancelado', 'Cancelado pelo usuário');

INSERT INTO donation_statuses (
    name,
    label,
    description
) VALUES
('PENDING', 'Pendente','Aguardando aprovação/confirmação'),
('DONE', 'Concluído', 'Foi realizado com sucesso'),
('REFUSED', 'Recusado', 'Recusado por algum motivo'),
('CANCELED', 'Cancelado', 'Cancelado pelo usuário');

INSERT INTO contribution_types (
    name,
    label,
    description
) VALUES
('PULL_REQUEST', 'PR', 'Pull request para o código fonte'),
('ISSUE', 'Issue', 'Abertura de issue para melhorias ou correção de bugs'),
('SUGGESTION', 'Sugestão', 'Sugestões de melhorias via canais oficiais da plataforma');

INSERT INTO donation_types (
    name,
    label,
    description
) VALUES
('MESSAGE', 'Mensagem Comum', 'Doação com alerta de mensagem'),
('AUDIO_MESSAGE', 'Mensagem com áudio', 'Mensagem com TTS/Audio'),
('MONTHLY_SUBSCRIPTION', 'Inscrição Mensal', 'Inscrição de pagamento recorrente mensal para acesso a conteúdos especifico');

INSERT INTO donation_platforms (
    name,
    label,
    description
) VALUES
('LIVEPIX', 'LivePix', 'Doações com alertas na live'),
('YOUTUBE', 'YouTube', 'Plataforma de videos'),
('PATREON', 'Patreon', 'Plataforma de pagamentos recorrentes para acessar conteúdos específicos');

