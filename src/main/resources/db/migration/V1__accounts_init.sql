CREATE TABLE accounts
(
    id SERIAL,
    version BIGINT NOT NULL,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    PRIMARY KEY (id)
);