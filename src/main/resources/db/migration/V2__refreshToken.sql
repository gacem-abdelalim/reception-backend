
CREATE TABLE refresh_token (
                               id BIGSERIAL PRIMARY KEY,
                               token VARCHAR(255) NOT NULL UNIQUE,
                               user_id BIGINT REFERENCES custom_user(id) ON DELETE CASCADE,
                               expiry_date TIMESTAMP NOT NULL
);