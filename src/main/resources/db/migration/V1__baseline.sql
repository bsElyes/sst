CREATE TABLE users (
                       user_id SERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL
);
create table roles
(
    role_name varchar(50) not null
        primary key
);
create table users_role
(
    user_id        bigint      not null
        constraint fk_user_id
            references users,
    role_name varchar(50) not null
        constraint fk_role_name
            references roles,
    primary key (user_id, role_name)
);

CREATE TABLE ingredients (
                             ingredient_id SERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             price DECIMAL(10, 2) NOT NULL,
                             available_quantity INT NOT NULL,
                             type VARCHAR(100) NOT NULL
);

CREATE TABLE orders (
                        order_id SERIAL PRIMARY KEY,
                        user_id INT REFERENCES users(user_id),
                        order_date DATE NOT NULL,
                        total_cost DECIMAL(10, 2) NOT NULL
);

CREATE TABLE order_items (
                             order_item_id SERIAL PRIMARY KEY,
                             order_id INT REFERENCES orders(order_id),
                             ingredient_id INT REFERENCES ingredients(ingredient_id),
                             quantity INT NOT NULL
);

CREATE TABLE statistics (
                            statistic_id SERIAL PRIMARY KEY,
                            date DATE NOT NULL,
                            best_selling_ingredient VARCHAR(255),
                            number_of_sandwiches_sold INT,
                            profit DECIMAL(10, 2)
);


-- feed database with test values
INSERT INTO roles (role_name)
VALUES
    ('ROLE_ADMIN'),
    ('ROLE_USER');
INSERT INTO users (username, password)
-- Hashed passwords for the admin and user accounts.
-- The passwords are hashed using BCrypt.
VALUES
    ('admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC'),
    ('user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K');

-- admin is a user and admin
INSERT INTO users_role (user_id, role_name)
VALUES
    (1, 'ROLE_ADMIN'),
    (1, 'ROLE_USER'),
    (2, 'ROLE_USER');
