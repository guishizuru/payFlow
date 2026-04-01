CREATE TABLE customers
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    cpf        VARCHAR(14)  NOT NULL UNIQUE,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW()
);