
create table question
(
    id int auto_increment,
    title varchar(40),
    description text,
    gmt_create bigint,
    gmt_modified bigint,
    creator int,
    comment_count int default 0,
    view_count int default 0,
    like_count int default 0,
    constraint QUESTION_PK
        primary key (id)
);