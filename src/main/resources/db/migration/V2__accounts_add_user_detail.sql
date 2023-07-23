ALTER TABLE accounts
    ADD password       VARCHAR(255)                     NOT NULL,
    ADD roles VARCHAR(255)[]  NOT NULL,
    ADD CONSTRAINT constraint_name UNIQUE (email);


