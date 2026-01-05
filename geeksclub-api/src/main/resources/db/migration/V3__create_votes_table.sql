CREATE TABLE votes
(
    id         BIGINT PRIMARY KEY,
    user_id    BIGINT      NOT NULL,
    message_id BIGINT      NOT NULL,
    vote_type  VARCHAR(10) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_votes_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_votes_message FOREIGN KEY (message_id) REFERENCES messages (id) ON DELETE CASCADE,
    CONSTRAINT chk_vote_type CHECK (vote_type IN ('UP', 'DOWN')),
    CONSTRAINT uq_user_message_vote UNIQUE (user_id, message_id)
);

CREATE INDEX idx_votes_message_id ON votes (message_id);
CREATE INDEX idx_votes_user_id ON votes (user_id);
CREATE INDEX idx_votes_created_at ON votes (created_at);