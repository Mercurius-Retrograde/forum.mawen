package com.example.forum.service;

import com.example.forum.dto.CommentDTO;
import com.example.forum.enums.CommentTypeEnum;
import com.example.forum.exception.CustomizeErrorCode;
import com.example.forum.exception.CustomizeException;
import com.example.forum.mapper.*;
import com.example.forum.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    public void insert(Comment comment) {
        if(comment.getParentId() == null || comment.getParentId() ==0){
            //没有传问题ID
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType() == null && !CommentTypeEnum.isExit(comment.getType())) {
            //问题/评论/评论中评论不存在
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment ==null){
                //回复的评论不存在
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
        }else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null){
                //回复的问题不存在
                throw new CustomizeException((CustomizeErrorCode.QUESTION_NOT_FOUND));
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if(comments.size() == 0){
            return  new ArrayList<>();
        }
        //获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment->comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds= new ArrayList<>();
        userIds.addAll(commentators);

        //获取评论人并转换为map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect((Collectors.toMap(user -> user.getId(), user -> user)));

        //转换comment为commentdto
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
