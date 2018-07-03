package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.common.status.ConsultationStatus;
import com.blueocean.azbrain.dao.*;
import com.blueocean.azbrain.model.*;
import com.blueocean.azbrain.service.ConsultationService;
import com.blueocean.azbrain.vo.ConsultationLogVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service("consultationService")
public class ConsultationServiceImpl implements ConsultationService {
    @Autowired
    private ConsultationLogMapper consultationLogMapper;

    @Autowired
    private UserScoreLogMapper userScoreLogMapper;

    @Autowired
    private SpecialistScoreLogMapper specialistScoreLogMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public int insert(ConsultationLog record) {
        return consultationLogMapper.insert(record);
    }

    @Override
    public ConsultationLog get(Integer id) {
        return consultationLogMapper.get(id);
    }

    @Override
    public int changeStatus(Integer id, String status) {
        return consultationLogMapper.changeStatus(id, status);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int confirm(Integer id, Integer topicId) {
        if (topicId > 0){
            // 增加topic咨询次数
            topicMapper.incrConsultedNum(topicId);
        }
        return changeStatus(id, ConsultationStatus.CONFIRMED.getCode());
    }

    @Override
    public int edit(ConsultationLogVo consultationLogVo) {
        return consultationLogMapper.edit(consultationLogVo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int insertUserScoreLog(UserScoreLog record) {
        // 设置专家已评论
        consultationLogMapper.byUserEvaluated(record.getLogId());

        UserPoints userPoints = new UserPoints();
        userPoints.setUserId(record.getByUserId());
        userPoints.setLogId(record.getLogId());
        userPoints.setPoint(20);
        userPoints.setCreateTime(LocalDateTime.now());
        userPoints.setRemark("EVALUATED");
        userMapper.insertPoints(userPoints);

        // 专家（被咨询人）咨询为完成状态
        //consultationLogMapper.changeStatus(record.getLogId(), ConsultationStatus.BYCOMPLETED.getCode());
        return userScoreLogMapper.insert(record);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int insertSpecialistScoreLog(SpecialistScoreLog record) {
        consultationLogMapper.userEvaluated(record.getLogId());
        // 咨询人为完成状态
        UserPoints userPoints = new UserPoints();
        userPoints.setUserId(record.getByUserId());
        userPoints.setLogId(record.getLogId());
        userPoints.setPoint(20);
        userPoints.setCreateTime(LocalDateTime.now());
        userPoints.setRemark("EVALUATED");
        userMapper.insertPoints(userPoints);

        //consultationLogMapper.changeStatus(record.getLogId(), ConsultationStatus.COMPLETED.getCode());
        return specialistScoreLogMapper.insert(record);
    }

    @Override
    public Page<ConsultationLog> myConsult(int page, int pageSize, Integer userId) {
        PageHelper.startPage(page, pageSize);
        return consultationLogMapper.myConsult(userId);
    }

    @Override
    public Page<ConsultationLog> consultMe(int page, int pageSize, Integer userId) {
        PageHelper.startPage(page, pageSize);
        return consultationLogMapper.consultMe(userId);
    }
}
