create database blog;
CREATE TABLE users
(
    id INTEGER PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    fullname VARCHAR(255),
    age INT,
    sex BOOLEAN,
    hometown VARCHAR(255),
    school VARCHAR(255),
    role_id INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE role
(
    id INTEGER PRIMARY KEY,
    role VARCHAR(10) NOT NULL
);

CREATE TABLE posts
(
    id INTEGER PRIMARY KEY,
    title VARCHAR(255),
    body TEXT,
    active BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);


CREATE TABLE image
(
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    source TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id INT,
    post_id INT,
    comment_id INT
);

CREATE TABLE avatar
(
    id INT PRIMARY KEY,
    source TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE TABLE comment
(
    id INT PRIMARY KEY,
    body TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    post_id INT,
    FOREIGN KEY (post_id) REFERENCES posts(id)
);


ALTER TABLE image
    ADD CONSTRAINT fk_image_post
        FOREIGN KEY (post_id) REFERENCES posts(id);

ALTER TABLE comment
    ADD CONSTRAINT fk_comment_post
        FOREIGN KEY (post_id) REFERENCES posts(id);

ALTER TABLE avatar
    ADD CONSTRAINT fk_avatar_user
        FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE image
    ADD CONSTRAINT fk_image_comment
        FOREIGN KEY (comment_id) REFERENCES comment(id);

ALTER TABLE image
    ADD CONSTRAINT fk_image_user
        FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE users
    ADD CONSTRAINT fk_user_role
        FOREIGN KEY (role_id) REFERENCES role(id);