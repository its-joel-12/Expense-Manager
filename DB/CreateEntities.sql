-- creating USER table
CREATE TABLE users (
    user_id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100),
    email VARCHAR(100) NOT NULL,
    password VARCHAR(100),
    created_at DATE,
    updated_at DATE,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);