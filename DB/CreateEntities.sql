-- creating USER table
CREATE TABLE users (
    user_id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100),
    email VARCHAR(100) NOT NULL unique,
    password VARCHAR(100),
	two_Factor_Secret VARCHAR(100),
    is_Two_Factor_Enabled boolean,
    sign_Up_Method VARCHAR(100),
    created_at DATE,
    updated_at DATE,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
	role_name VARCHAR(100),
	role_id int
);

-- creating ROLE table
create table role (
	role_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	role_name varchar(100) not null
);

-- adding foreign key constraint to users table
ALTER TABLE users
ADD CONSTRAINT role_id
FOREIGN KEY (role_id)
REFERENCES role(role_id);