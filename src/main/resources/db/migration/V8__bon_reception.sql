ALTER TABLE bon_reception
DROP CONSTRAINT IF EXISTS fk_facture;

ALTER TABLE bon_reception
    ADD CONSTRAINT fk_facture
        FOREIGN KEY (facture_id)
            REFERENCES reception_facture(facture_id)
            ON DELETE SET NULL;