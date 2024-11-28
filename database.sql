create database blog;
use blog;
CREATE TABLE users
(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    fullname VARCHAR(255),
    age INT,
    sex BOOLEAN,
    hometown VARCHAR(255),
    avatar VARCHAR(255) DEFAULT "https://static.vecteezy.com/system/resources/previews/005/129/844/non_2x/profile-user-icon-isolated-on-white-background-eps10-free-vector.jpg",
    school VARCHAR(255),
    role_id INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE role
(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    role VARCHAR(10) NOT NULL
);

CREATE TABLE posts
(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    active BOOLEAN DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE image
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    source TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    comment_id INT
);

CREATE TABLE image_blog
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    source TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE comment
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    body TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    post_id INT NOT NULL ,
    user_id INT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);


ALTER TABLE comment
    ADD CONSTRAINT fk_comment_post
        FOREIGN KEY (post_id) REFERENCES posts(id);

ALTER TABLE image
    ADD CONSTRAINT fk_image_comment
        FOREIGN KEY (comment_id) REFERENCES comment(id);

ALTER TABLE users
    ADD CONSTRAINT fk_user_role
        FOREIGN KEY (role_id) REFERENCES role(id);

insert into role values
                     (1, 'USER'),
                     (2, 'ADMIN');

insert into users (id, username, password, fullname, role_id, active) values
                                                                          (1, 'duongquanghao', 'password', 'Duong Hao', 2, true),
                                                                          (2, 'phiconghuan', 'password', 'Huan phi cong', 2, true),
                                                                          (3, 'lengocduc', 'password', 'Le Duc', 1, true),
                                                                          (4, 'admin', '$2a$10$udj523nuAF7rgXoNxD9kXeyfwf9BlGAdMdx.2v.6BAIXxkTi3AqgW', 'DH', 2, true);

insert into posts (id, title, body, active) values
                                                (1, 'Cuoc thi CTF PTIT 2024', 'body', true),
                                                (2, 'Cuoc thi ICPC PTIT 2024', 'body', true);