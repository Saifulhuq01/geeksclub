CREATE TABLE users
(
    id         BIGINT PRIMARY KEY,
    full_name  VARCHAR(100) NOT NULL,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL DEFAULT 'USER',
    created_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version    INTEGER               DEFAULT 0,
    CONSTRAINT chk_role CHECK (role IN ('USER', 'ADMIN'))
);

CREATE INDEX idx_users_email ON users(LOWER(email));
CREATE INDEX idx_users_username ON users(LOWER(username));
