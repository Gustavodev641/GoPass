CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE event (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(250),
    event_url VARCHAR(100) NOT NULL,
    img_url VARCHAR(300),
    date TIMESTAMP NOT NULL,
    remote BOOLEAN NOT NULL,
    city VARCHAR(100),
    state VARCHAR(2)
);