package com.example.forum.service;

import com.example.forum.dto.PaginationDTO;
import com.example.forum.dto.QuestionDTO;
import com.example.forum.mapper.QuestionMapper;
import com.example.forum.mapper.UserMapper;
import com.example.forum.model.Question;
import com.example.forum.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Resource
    private QuestionMapper questionMapper;

    public PaginationDTO list(Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount  = questionMapper.count();
        paginationDTO.setPagination(totalCount,page,size);

        if (page < 1){
            page =1;
        }
        if (page > paginationDTO.getTotalPage()){
            page = paginationDTO.getTotalPage();
        }
        //size*(page - 1)
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question :questions){
            User user = userMapper.findByID(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //使用beanutils将question中的所有属性拷贝到questionDTO中
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }


    public PaginationDTO listByUser(Integer userId, Integer page, Integer size) {
        {

            PaginationDTO paginationDTO = new PaginationDTO();
            Integer totalCount  = questionMapper.countByUserId(userId);
            paginationDTO.setPagination(totalCount,page,size);

            if (page < 1){
                page =1;
            }
            if (page > paginationDTO.getTotalPage()){
                page = paginationDTO.getTotalPage();
            }
            //size*(page - 1)
            Integer offset = size * (page - 1);
            List<Question> questions = questionMapper.listByUser(userId,offset,size);
            List<QuestionDTO> questionDTOList = new ArrayList<>();


            for (Question question :questions){
                User user = userMapper.findByID(question.getCreator());
                QuestionDTO questionDTO = new QuestionDTO();
                //使用beanutils将question中的所有属性拷贝到questionDTO中
                BeanUtils.copyProperties(question,questionDTO);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
            paginationDTO.setQuestions(questionDTOList);
            return paginationDTO;
        }
    }

}
