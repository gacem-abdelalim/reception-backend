CREATE TABLE dynamic_query (
                               id SERIAL PRIMARY KEY,
                               name VARCHAR(100) UNIQUE NOT NULL,
                               fields VARCHAR(500),
                               sql_text TEXT NOT NULL,
                               description TEXT
);
