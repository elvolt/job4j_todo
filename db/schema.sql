CREATE TABLE item
(
    id          SERIAL PRIMARY KEY,
    description VARCHAR(500) NOT NULL,
    created     TIMESTAMPTZ,
    done        BOOLEAN
);