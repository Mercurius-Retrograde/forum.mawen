package com.example.forum.mapper;

import com.example.forum.dto.QuestionQueryDTO;
import com.example.forum.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
    List<Question> selectRelated(Question question);
    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}
