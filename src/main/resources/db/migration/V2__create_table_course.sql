CREATE TABLE course
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    code              VARCHAR(10)           NOT NULL,
    user_id           BIGINT                NOT NULL,
    `description`     VARCHAR(255)          NULL,
    status            VARCHAR(8)            NOT NULL,
    creation_date     datetime              NOT NULL,
    inactivation_date datetime              NULL,
    CONSTRAINT pk_course PRIMARY KEY (id)
);

ALTER TABLE course
    ADD CONSTRAINT uc_course_code UNIQUE (code);

ALTER TABLE course
    ADD CONSTRAINT FK_COURSE_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);