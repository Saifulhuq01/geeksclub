CREATE TABLE messages
(
    id              BIGINT PRIMARY KEY,
    user_id         BIGINT      NOT NULL,
    content         TEXT        NOT NULL,
    is_spam         BOOLEAN              DEFAULT false,
    spam_confidence DECIMAL(5, 4),
    status          VARCHAR(20) NOT NULL DEFAULT 'PUBLISHED',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_messages_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT chk_status CHECK (status IN ('PUBLISHED', 'FLAGGED', 'REMOVED')),
    CONSTRAINT chk_content_length CHECK (LENGTH(content) > 0 AND LENGTH(content) <= 5000)
);

CREATE INDEX idx_messages_user_id ON messages (user_id);
CREATE INDEX idx_messages_created_at ON messages (created_at DESC);
CREATE INDEX idx_messages_status ON messages (status);
CREATE INDEX idx_messages_is_spam ON messages (is_spam);