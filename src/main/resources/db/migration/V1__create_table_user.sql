CREATE TABLE user
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    username      VARCHAR(20)           NOT NULL,
    name          VARCHAR(255)          NOT NULL,
    email         VARCHAR(255)          NOT NULL,
    `role`        VARCHAR(10)           NOT NULL,
    password      VARCHAR(255)          NOT NULL,
    creation_date datetime              NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE user
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE user
    ADD CONSTRAINT uc_user_username UNIQUE (username);