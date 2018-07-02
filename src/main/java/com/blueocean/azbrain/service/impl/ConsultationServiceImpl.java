package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.common.status.ConsultationStatus;
import com.blueocean.azbrain.dao.*;
import com.blueocean.azbrain.model.*;
import com.blueocean.azbrain.service.ConsultationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int insertUserScoreLog(UserScoreLog record) {
        consultationLogMapper.setSpecialistCommented(record.getLogId());
        return userScoreLogMapper.insert(record);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int insertSpecialistScoreLog(SpecialistScoreLog record) {
        consultationLogMapper.setUserCommented(record.getLogId());
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
