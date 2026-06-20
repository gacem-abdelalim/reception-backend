ALTER TABLE ligne_bon
ADD COLUMN colis INTEGER,
ADD COLUMN vrag INTEGER,
ADD COLUMN qte_abime INTEGER,
ADD COLUMN name VARCHAR(255),
ADD COLUMN dosage VARCHAR(255),
ADD COLUMN forme VARCHAR(100),
ADD COLUMN ddp DATE,
ADD COLUMN ddf DATE,
ADD COLUMN ppa DOUBLE PRECISION,
ADD COLUMN shp DOUBLE PRECISION,
ADD COLUMN colissage INTEGER;




CREATE TABLE facture
(
    facture_id   SERIAL PRIMARY KEY,
    ref          VARCHAR(100) NOT NULL,
    date_facture DATE NOT NULL,

    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



ALTER TABLE bon_reception
DROP COLUMN ref_facture,
DROP COLUMN date_facture;

ALTER TABLE bon_reception
    ADD COLUMN facture_id INTEGER;

ALTER TABLE bon_reception
    ADD CONSTRAINT fk_facture
        FOREIGN KEY (facture_id)
            REFERENCES facture(facture_id)
            ON DELETE SET NULL;