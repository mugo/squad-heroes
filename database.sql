CREATE DATABASE squad_heroes;
\c squad_heroes;

CREATE TABLE IF NOT EXISTS heroes (
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    age INTEGER,
    origin_story VARCHAR,
    powers VARCHAR,
    weaknesses VARCHAR,
    squad_id INTEGER
);


CREATE TABLE IF NOT EXISTS squads (
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    size INTEGER,
    cause VARCHAR
);