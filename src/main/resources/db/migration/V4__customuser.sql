ALTER TABLE custom_user
DROP CONSTRAINT custom_user_type_check;

ALTER TABLE custom_user
    ADD CONSTRAINT custom_user_type_check
        CHECK (
            type IN (
                     'admin',
                     'superviseur',
                     'operateur',
                     'reception',
                     'reception-sup'
                )
            );