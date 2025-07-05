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
    updated_by VARCHAR(100)
);

-- creating ROLE table
create table role (
	role_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	role_name varchar(100) not null unique
);

-- creating user_roles table
create table user_role (
    id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	user_id bigint,
	role_id int
);

-- adding foreign key constraint to user_roles table
ALTER TABLE user_role
ADD CONSTRAINT fk_user
FOREIGN KEY (user_id)
REFERENCES users(user_id);

ALTER TABLE user_role
ADD CONSTRAINT fk_role
FOREIGN KEY (role_id)
REFERENCES role(role_id);