package com.example.forum.service;

import com.example.forum.dto.QuestionDTO;
import com.example.forum.mapper.QuestionMapper;
import com.example.forum.mapper.UserMapper;
import com.example.forum.model.Question;
import com.example.forum.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public List<QuestionDTO> list() {
            List<Question> questions = questionMapper.list();
            List<QuestionDTO> questionDTOList = new ArrayList<>();
            for (Question question :questions){
                User user = userMapper.findByID(question.getCreator());
                QuestionDTO questionDTO = new QuestionDTO();
                //使用beanutils将question中的所有属性拷贝到questionDTO中
                BeanUtils.copyProperties(question,questionDTO);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
        return questionDTOList;
    }
}
