package io.github.jiema.communtiy.service;

import io.github.jiema.communtiy.dto.NotificationDTO;
import io.github.jiema.communtiy.dto.PaginationDTO;
import io.github.jiema.communtiy.enums.NotificationStatusEnum;
import io.github.jiema.communtiy.enums.NotificationTypeEnum;
import io.github.jiema.communtiy.exception.CustomizeErrorCode;
import io.github.jiema.communtiy.exception.CustomizeException;
import io.github.jiema.communtiy.mapper.NotificationMapper;
import io.github.jiema.communtiy.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class NotificationService {

    @Resource
    private NotificationMapper notificationMapper;

    public PaginationDTO list(String accountId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(accountId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);
        Integer totalPage = totalCount / size + (totalCount % size != 0 ? 1 : 0);
        if (page < 1) page = 1;
        if (page > totalPage) page = totalPage;
        Integer offset = size * (page - 1);
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(accountId);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        if (notifications.size() == 0) return paginationDTO;

        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }

        paginationDTO.setData(notificationDTOS);
        paginationDTO.setPagination(totalPage, page);
        return paginationDTO;
    }

    public Long unreadCount(String userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getAccountId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
