CREATE TABLE active_tokens
(
    token BINARY(16) PRIMARY KEY,
    expiry_date DATETIME NOT NULL,
    INDEX idx_expiry_date (expiry_date)
);