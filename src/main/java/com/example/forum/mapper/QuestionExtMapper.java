package com.example.forum.mapper;

import com.example.forum.model.Question;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
}
