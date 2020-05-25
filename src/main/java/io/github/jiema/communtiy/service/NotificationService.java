package io.github.jiema.communtiy.service;

import io.github.jiema.communtiy.dto.NotificationDTO;
import io.github.jiema.communtiy.dto.PaginationDTO;
import io.github.jiema.communtiy.dto.QuestionDTO;
import io.github.jiema.communtiy.mapper.NotificationMapper;
import io.github.jiema.communtiy.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Resource
    private NotificationMapper notificationMapper;

    public PaginationDTO list(String accountId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO();
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(accountId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);
        Integer totalPage = totalCount / size + (totalCount % size != 0 ? 1 : 0);
        if (page < 1) page = 1;
        if (page > totalPage) page = totalPage;
        Integer offset = size * (page - 1);
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(accountId);
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        paginationDTO.setData(notificationDTOS);
        paginationDTO.setPagination(totalPage, page);
        return paginationDTO;
    }
}
