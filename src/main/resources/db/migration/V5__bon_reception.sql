ALTER TABLE bon_reception
    ADD COLUMN is_validated BOOLEAN DEFAULT FALSE,
    ADD COLUMN validated_at TIMESTAMP,
    ADD COLUMN validated_by VARCHAR(100);