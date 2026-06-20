-- ============================
-- Users & Roles
-- ============================

CREATE TABLE custom_user (
                             id SERIAL PRIMARY KEY,
                             password VARCHAR(128) NOT NULL,
                             last_login TIMESTAMP,
                             is_superuser BOOLEAN NOT NULL DEFAULT FALSE,
                             username VARCHAR(150) UNIQUE NOT NULL,
                             first_name VARCHAR(150) NOT NULL DEFAULT '',
                             last_name VARCHAR(150) NOT NULL DEFAULT '',
                             email VARCHAR(254) NOT NULL DEFAULT '',
                             is_staff BOOLEAN NOT NULL DEFAULT FALSE,
                             is_active BOOLEAN NOT NULL DEFAULT TRUE,
                             date_joined TIMESTAMP NOT NULL DEFAULT NOW(),
                             type VARCHAR(20) CHECK (type IN ('admin', 'superviseur', 'operateur')),
                             superviseur_id INT REFERENCES custom_user(id) ON DELETE SET NULL
);

-- Role table
CREATE TABLE role (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(50) UNIQUE NOT NULL,
                      navigate_command VARCHAR(100),
                      icon VARCHAR(100)
);

-- UserRole join table
CREATE TABLE user_role (
                           id SERIAL PRIMARY KEY,
                           user_id INT NOT NULL REFERENCES custom_user(id) ON DELETE CASCADE,
                           role_id INT NOT NULL REFERENCES role(id) ON DELETE CASCADE,
                           CONSTRAINT user_role_unique UNIQUE (user_id, role_id)
);
