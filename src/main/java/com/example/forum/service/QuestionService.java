package com.example.forum.service;

import com.example.forum.dto.PaginationDTO;
import com.example.forum.dto.QuestionDTO;
import com.example.forum.exception.CustomizeErrorCode;
import com.example.forum.exception.CustomizeException;
import com.example.forum.mapper.QuestionMapper;
import com.example.forum.mapper.UserMapper;
import com.example.forum.model.Question;
import com.example.forum.model.QuestionExample;
import com.example.forum.model.User;
import org.apache.ibatis.session.RowBounds;
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


    public PaginationDTO list(Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount  = (int)questionMapper.countByExample(new  QuestionExample());
        paginationDTO.setPagination(totalCount,page,size);

        if (page <=1){
            page =1;
        }
        if (page > paginationDTO.getTotalPage()){
            page = paginationDTO.getTotalPage();
        }
        //size*(page - 1)
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(),new RowBounds(offset,size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question :questions){

            User user = userMapper.selectByPrimaryKey(question.getCreator());
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

            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andCreatorEqualTo(userId);
            Integer totalCount  = (int)questionMapper.countByExample(questionExample);


//            Integer totalCount  = questionMapper.countByUserId(userId);
            paginationDTO.setPagination(totalCount,page,size);

            if (page < 1){
                page =1;
            }
            if (page > paginationDTO.getTotalPage()){
                page = paginationDTO.getTotalPage();
            }
            //size*(page - 1)
            Integer offset = size * (page - 1);

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andCreatorEqualTo(userId);
            List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example,new RowBounds(offset,size));
            List<QuestionDTO> questionDTOList = new ArrayList<>();


            for (Question question :questions){
                User user = userMapper.selectByPrimaryKey(question.getCreator());
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

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);

        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);

        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }
    public void creatOrUpdate(Question question) {
        if(question.getId() ==null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtModified());
            questionMapper.insert(question);
        }else {
//            question.setTitle(question.getTitle());
//            question.setDescription(question.getDescription());
//            question.setTag(question.getTag());
//            question.setGmtModified(System.currentTimeMillis());

            Question updateQuestion = new Question();
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setGmtModified(System.currentTimeMillis());

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion,example);
            if(update != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }
}
