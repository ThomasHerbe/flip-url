CREATE TABLE IF NOT EXISTS url
(
    shortened_url VARCHAR(10) NOT NULL PRIMARY KEY,
    initial_url   TEXT        NOT NULL
)