CREATE TABLE IF NOT EXISTS book_category (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) UNIQUE,
    created_by      BIGINT,
    created_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by      BIGINT,
    updated_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS book_info (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id     BIGINT,
    title           VARCHAR(255),
    author          VARCHAR(255),
    price           DECIMAL(19, 4),
    created_by      BIGINT,
    created_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by      BIGINT,
    updated_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);