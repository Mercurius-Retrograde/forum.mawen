ALTER TABLE QUESTION alter column ID BIGINT NOT NULL;
ALTER TABLE USER alter column ID BIGINT NOT NULL;
alter table QUESTION alter column CREATOR bigint not null;
alter table COMMENT alter column COMMENTATOR bigint not null;