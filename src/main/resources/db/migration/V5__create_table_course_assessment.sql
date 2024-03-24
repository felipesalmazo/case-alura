CREATE TABLE course_assessment
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    assessment_grade INT                   NOT NULL,
    `description`    VARCHAR(255)          NULL,
    user_id          BIGINT                NOT NULL,
    course_id        BIGINT                NOT NULL,
    CONSTRAINT pk_courseassessment PRIMARY KEY (id)
);

ALTER TABLE course_assessment
    ADD CONSTRAINT FK_COURSEASSESSMENT_ON_COURSE FOREIGN KEY (course_id) REFERENCES course (id);

ALTER TABLE course_assessment
    ADD CONSTRAINT FK_COURSEASSESSMENT_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);