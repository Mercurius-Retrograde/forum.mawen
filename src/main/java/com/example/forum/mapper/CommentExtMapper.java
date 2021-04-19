package com.example.forum.mapper;

import com.example.forum.model.Comment;
import com.example.forum.model.CommentExample;
import com.example.forum.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}