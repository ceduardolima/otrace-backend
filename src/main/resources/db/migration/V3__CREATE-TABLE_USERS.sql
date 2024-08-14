CREATE TABLE
    users (
        id BIGINT GENERATED ALWAYS AS IDENTITY,
        name VARCHAR(50) NOT NULL,
        account_id BIGINT NOT NULL,
        PRIMARY KEY (id),
        FOREIGN KEY (account_id) REFERENCES accounts (id) ON DELETE CASCADE ON UPDATE CASCADE
    )