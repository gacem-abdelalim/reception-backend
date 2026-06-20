ALTER TABLE bon_reception
DROP CONSTRAINT IF EXISTS fk_facture;

ALTER TABLE reception_facture
    ADD COLUMN brcp_id INTEGER;

ALTER TABLE reception_facture
    ADD CONSTRAINT fk_bon_reception
        FOREIGN KEY (brcp_id)
            REFERENCES bon_reception(brcp_id);