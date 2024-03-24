CREATE TABLE enrollment
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    user_id         BIGINT                NOT NULL,
    course_id       BIGINT                NOT NULL,
    enrollment_date datetime              NOT NULL,
    CONSTRAINT pk_enrollment PRIMARY KEY (id)
);

ALTER TABLE enrollment
    ADD CONSTRAINT FK_ENROLLMENT_ON_COURSE FOREIGN KEY (course_id) REFERENCES course (id);

ALTER TABLE enrollment
    ADD CONSTRAINT FK_ENROLLMENT_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);