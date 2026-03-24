CREATE TABLE payments (
                          id UUID PRIMARY KEY,

                          amount NUMERIC(15,2) NOT NULL,

                          status VARCHAR(20) NOT NULL,

                          external_id VARCHAR(100) UNIQUE,

                          description VARCHAR(255),

                          created_at TIMESTAMP NOT NULL,

                          updated_at TIMESTAMP
);