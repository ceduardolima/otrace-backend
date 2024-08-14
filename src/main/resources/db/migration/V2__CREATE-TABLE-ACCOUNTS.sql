CREATE TABLE accounts (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE UNIQUE INDEX accounts_email_idx ON accounts(email);
