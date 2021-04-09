create table comment
(
    id BIGINT auto_increment,
    parent_id BIGINT not null,
    type BIGINT not null,
    commentator BIGINT not null,
    gmt_create BIGINT not null,
    gmt_modified BIGINT not null,
    like_count BIGINT DEFAULT 0,
    constraint COMMENT_PK
        primary key (id)
);