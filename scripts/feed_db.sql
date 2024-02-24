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
