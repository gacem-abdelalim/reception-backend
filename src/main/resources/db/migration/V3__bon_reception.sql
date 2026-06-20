-- Table: Bon_Reception
CREATE TABLE bon_reception (
                               brcp_id        SERIAL PRIMARY KEY,
                               ref_facture    VARCHAR(100),
                               date_facture   DATE,
                               date_reception DATE,
                               four_id        INTEGER,
                               four_name      VARCHAR(255),
                               created_by     VARCHAR(100),
                               created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: ligne_bon
CREATE TABLE ligne_bon (
                           id             SERIAL PRIMARY KEY,
                           brcp_id        INTEGER NOT NULL,
                           med_id         INTEGER NOT NULL,
                           lot            VARCHAR(100),
                           qnt            INTEGER NOT NULL,
                           created_by     VARCHAR(100),
                           created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_bon_reception
                               FOREIGN KEY (brcp_id)
                                   REFERENCES bon_reception (brcp_id)
                                   ON DELETE CASCADE
);