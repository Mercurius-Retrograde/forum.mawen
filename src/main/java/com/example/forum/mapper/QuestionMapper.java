package com.example.forum.mapper;

import com.example.forum.model.Question;
import org.apache.ibatis.annotations.Insert;

public interface QuestionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);
}
