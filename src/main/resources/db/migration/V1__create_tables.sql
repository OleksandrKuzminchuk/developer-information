CREATE TABLE my_db.specialty (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           name VARCHAR(100),
                           status VARCHAR(100) NOT NULL,
                           PRIMARY KEY (id)
);

CREATE TABLE my_db.skill (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             name VARCHAR(255),
                             status VARCHAR(100) NOT NULL,
                             PRIMARY KEY (id)
);

CREATE TABLE my_db.developer (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           first_name VARCHAR(100),
                           last_name VARCHAR(100),
                           specialty_id BIGINT,
                           status VARCHAR(100) NOT NULL,
                           PRIMARY KEY (id),
                           FOREIGN KEY (specialty_id) REFERENCES my_db.specialty(id)
);

CREATE TABLE my_db.developer_skill (
                                 developer_id BIGINT,
                                 skill_id BIGINT,
                                 FOREIGN KEY (developer_id) REFERENCES my_db.developer(id),
                                 FOREIGN KEY (skill_id) REFERENCES my_db.skill(id)
);
