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
