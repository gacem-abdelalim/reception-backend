ALTER TABLE bon_reception
    ADD COLUMN IF NOT EXISTS is_cloture BOOLEAN DEFAULT false;

ALTER TABLE bon_reception
    ADD COLUMN IF NOT EXISTS cloture_by VARCHAR(255);

ALTER TABLE bon_reception
    ADD COLUMN IF NOT EXISTS cloture_at TIMESTAMP;


