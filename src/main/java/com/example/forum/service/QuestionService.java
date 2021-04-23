package com.example.forum.service;

import com.example.forum.dto.PaginationDTO;
import com.example.forum.dto.QuestionDTO;
import com.example.forum.dto.QuestionQueryDTO;
import com.example.forum.exception.CustomizeErrorCode;
import com.example.forum.exception.CustomizeException;
import com.example.forum.mapper.QuestionExtMapper;
import com.example.forum.mapper.QuestionMapper;
import com.example.forum.mapper.UserMapper;
import com.example.forum.model.Question;
import com.example.forum.model.QuestionExample;
import com.example.forum.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Transactional
    //将方法体加到事务中，防止上一条执行成功，下一条执行失败后仍然插入数据库。
    public PaginationDTO list(String search, Integer page, Integer size) {
        //StringUtils工具类
        // .isBlank 是在 isEmpty 的基础上进行了为空（字符串都为空格、制表符、tab 的情况）的判断。（一般更为常用）
        // .split只要匹配到了分隔符中的任意一个字符，就会进行分割
        //如果有数据，搜索栏的数据切割
        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
//            将数组转换成流  Java 8 Stream
            search = Arrays
                    .stream(tags)
                    .filter(StringUtils::isNotBlank)
                    .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("|"));
        }           // ddd|dgs|sss|fg, 这个|在sql的正则表达式是匹配这里面的任意个

        // 在这里定义
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalPage;

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        //把上面的搜索信息search，传入
        questionQueryDTO.setSearch(search);

        // 标签，传入
//        if (StringUtils.isNotBlank(tag)) {
//            tag = tag.replace("+", "").replace("*", "").replace("?", "");
//            questionQueryDTO.setTag(tag);
//        }

        //sort 种类，传入种类
//        HOT7等还要传时间，不知其他标签实现了吗
//        for (SortEnum sortEnum : SortEnum.values()) {
//            if (sortEnum.name().toLowerCase().equals(sort)) {
//                questionQueryDTO.setSort(sort);
//
//                if (sortEnum == SortEnum.HOT7) {
//                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 7);
//                }
//                if (sortEnum == SortEnum.HOT30) {
//                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30);
//                }
//                break;
//            }
//        }

//按上面的参数搜索，结果的条数，处理页码
//        这个功能实现了，sql
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
//  page页码，大于总页码，当前页码就在总页码数
        if (page > totalPage) {
            page = totalPage;
        }
//
        paginationDTO.setPagination(totalPage, page);
//        三目表达式，page < 1是真，等于：前的0，假是：后面的
        Integer offset = page < 1 ? 0 : size * (page - 1);
        //offset 这个是啥？ size是2，一页展示2条,page = 1，offset是0；p=2，o=2；p=3,o=4; p=4.o=6
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        //size展示条数，
        // offset是从那条数据开始，不是页码，从第0条数据开始
        // 查出问题
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            // 进入这个列表
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }


    public PaginationDTO listByUser(Long userId, Integer page, Integer size) {
        {

            PaginationDTO paginationDTO = new PaginationDTO();
            Integer totalPage;
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andCreatorEqualTo(userId);
            Integer totalCount = (int) questionMapper.countByExample(questionExample);

            if (totalCount % size == 0) {
                totalPage = totalCount / size;
            } else {
                totalPage = totalCount / size + 1;
            }
            if (page < 1) {
                page = 1;
            }
            if (page > totalPage) {
                page = totalPage;
            }
            paginationDTO.setPagination(totalPage, page);


            //size*(page - 1)
            Integer offset = size * (page - 1);

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andCreatorEqualTo(userId);
            List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
            List<QuestionDTO> questionDTOList = new ArrayList<>();


            for (Question question : questions) {
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                QuestionDTO questionDTO = new QuestionDTO();
                //使用beanutils将question中的所有属性拷贝到questionDTO中
                BeanUtils.copyProperties(question, questionDTO);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
            paginationDTO.setData(questionDTOList);
            return paginationDTO;
        }
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);

        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);

        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void creatOrUpdate(Question question) {
        if (question.getId() == null) {
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtModified());
            question.setLikeCount(0);
            question.setViewCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        } else {
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
            int update = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (update != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
//        Question question = questionMapper.selectByPrimaryKey(id);
//        Question updataQuestion = new Question();
//        updataQuestion.setViewCount(question.getViewCount() + 1);
//        QuestionExample example = new QuestionExample();
//        example.createCriteria()
//                .andIdEqualTo(id);
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
//        questionMapper.updateByExampleSelective(updataQuestion, example);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), "，| |,");
        String regexTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexTag);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
