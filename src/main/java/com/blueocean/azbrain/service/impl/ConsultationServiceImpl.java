package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.common.status.ConsultationStatus;
import com.blueocean.azbrain.dao.*;
import com.blueocean.azbrain.model.*;
import com.blueocean.azbrain.service.ConsultationService;
import com.blueocean.azbrain.vo.ConsultationLogVo;
import com.blueocean.azbrain.vo.UserEvaluateVo;
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
    private TopicMapper topicMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserEvaluateMapper userEvaluateMapper;

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
    public int insertUserEvaluate(UserEvaluateVo record) {
        if (record.getFlag()){
            consultationLogMapper.byUserEvaluated(record.getLogId());
        } else {
            consultationLogMapper.userEvaluated(record.getLogId());
        }
        // 积分
        UserPoints userPoints = new UserPoints();
        userPoints.setUserId(record.getByUserId());
        userPoints.setLogId(record.getLogId());
        userPoints.setPoint(20);
        userPoints.setCreateTime(LocalDateTime.now());
        userPoints.setRemark("EVALUATED");
        userMapper.insertPoints(userPoints);

        return userEvaluateMapper.insertBatch(record.asUserEvaluates());
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
