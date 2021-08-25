CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(100),
    email    VARCHAR(100) UNIQUE,
    password VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS item
(
    id          SERIAL PRIMARY KEY,
    description VARCHAR(500) NOT NULL,
    created     TIMESTAMPTZ,
    done        BOOLEAN,
    user_id     INT          NOT NULL REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS category
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS item_category
(
    item_id     INT NOT NULL REFERENCES item (id),
    category_id INT NOT NULL REFERENCES category (id),
    PRIMARY KEY (item_id, category_id)
);