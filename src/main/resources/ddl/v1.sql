CREATE TABLE IF NOT EXISTS book_category (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) UNIQUE,
    created_by      BIGINT,
    created_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by      BIGINT,
    updated_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

MERGE INTO book_category (name, created_by, created_time, updated_by, updated_time)
    KEY (name)
    VALUES
    ('Fiction', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    ('Non-Fiction', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    ('Science Fiction', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    ('Fantasy', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    ('Mystery & Thriller', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    ('Romance', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    ('Biography & Autobiography', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    ('History', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    ('Self-Help', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    ('Horror', 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

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

MERGE INTO BOOK_INFO(category_id, title, author, price, created_by, created_time, updated_by, updated_time)
    KEY (TITLE)
    VALUES
    (1, 'To Kill a Mockingbird', 'Harper Lee', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (1, 'The Great Gatsby', 'F. Scott Fitzgerald', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (2, 'Sapiens: A Brief History of Humankind', 'Yuval Noah Harari', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (2, 'Educated', 'Tara Westover', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (3, 'Dune', 'Frank Herbert', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (3, 'The Hitchhiker''s Guide to the Galaxy', 'Douglas Adams', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (4, 'The Lord of the Rings', 'J.R.R. Tolkien', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (4, 'Harry Potter and the Sorcerer''s Stone', 'J.K. Rowling', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (5, 'Gone Girl', 'Gillian Flynn', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (5, 'The Girl with the Dragon Tattoo', 'Stieg Larsson', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (6, 'Pride and Prejudice', 'Jane Austen', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (6, 'The Notebook', 'Nicholas Sparks', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (7, 'The Autobiography of Malcolm X', 'Malcolm X & Alex Haley', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (7, 'Becoming', 'Michelle Obama', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (8, 'A People''s History of the United States', 'Howard Zinn', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (8, 'The Guns of August', 'Barbara W. Tuchman', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (9, 'Atomic Habits', 'James Clear', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (9, 'The 7 Habits of Highly Effective People', 'Stephen R. Covey', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (10, 'It', 'Stephen King', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP),
    (10, 'The Exorcist', 'William Peter Blatty', 100, 1, CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP);

CREATE TABLE IF NOT EXISTS shopping_cart (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    book_id         BIGINT NOT NULL,
    quantity        INTEGER NOT NULL DEFAULT 0,
    created_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_shopping_cart_item UNIQUE(user_id, book_id)
);