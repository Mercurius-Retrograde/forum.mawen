package com.example.forum.service;

import com.example.forum.dto.NotificationDTO;
import com.example.forum.dto.PaginationDTO;
import com.example.forum.dto.QuestionDTO;
import com.example.forum.mapper.NotificationMapper;
import com.example.forum.model.Notification;
import com.example.forum.model.NotificationExample;
import com.example.forum.model.Question;
import com.example.forum.model.QuestionExample;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    public PaginationDTO list(Long userId, Integer page, Integer size) {

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);


//            Integer totalCount  = questionMapper.countByUserId(userId);
        paginationDTO.setPagination(totalCount, page, size);

        if (page < 1) {
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }
        //size*(page - 1)
        Integer offset = size * (page - 1);

        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<NotificationDTO> notificaitonDTOList = new ArrayList<>();

        paginationDTO.setData(notificaitonDTOList);
        return paginationDTO;
    }
}
