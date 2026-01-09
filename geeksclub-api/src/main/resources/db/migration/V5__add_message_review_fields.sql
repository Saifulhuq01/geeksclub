ALTER TABLE messages
    ADD COLUMN reviewed_by VARCHAR(255),
    ADD COLUMN reviewed_at TIMESTAMPTZ,
    ADD COLUMN review_notes TEXT;

CREATE INDEX idx_messages_reviewed_at ON messages (reviewed_at);
