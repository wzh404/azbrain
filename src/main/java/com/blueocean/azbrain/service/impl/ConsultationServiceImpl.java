package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.common.status.ConsultationStatus;
import com.blueocean.azbrain.dao.*;
import com.blueocean.azbrain.model.*;
import com.blueocean.azbrain.service.ConsultationService;
import com.blueocean.azbrain.vo.ConsultationLogVo;
import com.blueocean.azbrain.vo.UserEvaluateVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service("consultationService")
public class ConsultationServiceImpl implements ConsultationService {
    private static final Logger logger = LoggerFactory.getLogger(ConsultationServiceImpl.class);

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
    public int confirm(ConsultationLog consultationLog) {
        Integer id = consultationLog.getId();
        Integer topicId = consultationLog.getTopicId();

        // 主题咨询？
        if (topicId > 0){
            // topic咨询次数+1
            topicMapper.incrConsultedNum(topicId);
        }

        // 被咨询人增加被咨询次数及被咨询时长
        userMapper.incrConsultation(consultationLog.getByUserId(), consultationLog.duration().intValue(), true);
        // 咨询人增加咨询次数及咨询时长
        userMapper.incrConsultation(consultationLog.getUserId(), consultationLog.duration().intValue(), false);
        return changeStatus(id, ConsultationStatus.CONFIRMED.getCode());
    }

    @Override
    public int edit(ConsultationLogVo consultationLogVo) {

        return consultationLogMapper.edit(consultationLogVo);
    }



    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int insertUserEvaluate(UserEvaluateVo record) {
        if (record.getByUserFlag() == 0){
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

        //评级 好评>=4 差评 < 3
        BigDecimal star = record.avgStar();
        int level = 0;
        if (star.compareTo(new BigDecimal(3)) > 0){
            level = 1; // 好评
        }
        else if (star.compareTo(new BigDecimal(3)) == -1){
            level = -1; // 差评
        }

        if (record.getByUserFlag() == 0){
            // 咨询人评级
            userMapper.chageConsultStar(record.getUserId(), star);
        } else {
            // 专家评级
            userMapper.chageConsultedStar(record.getByUserId(), star);
        }

        logger.info("***** average star is {} - {}", star, level);
        //是否爽约
        boolean contract = record.breakContract();
        if (level != 0 || contract) {
            userMapper.updateContractAndLevel(record.getByUserId(), contract, level);
        }
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

    @Override
    public Page<ConsultationLog> listConsultation(int page, int pageSize, Map<String, Object> conditionMap) {
        PageHelper.startPage(page, pageSize);
        return consultationLogMapper.listConsultation(conditionMap);
    }

    @Override
    public Map<String, Object> getEvaluationByLog(Integer logId) {
        Map<String, Object> resultMap = new HashMap<>();
        ConsultationLog log = consultationLogMapper.get(logId);
        if (log == null){
            return resultMap;
        }
        resultMap.put("user", userEvaluateMapper.getEvaluationByLog(logId, log.getUserId()));
        resultMap.put("byUser", userEvaluateMapper.getEvaluationByLog(logId, log.getByUserId()));
        return resultMap;
    }

    @Override
    public int selectSametimeLog(Integer userId, LocalDate cdate, LocalTime startTime, LocalTime endTime) {
        return consultationLogMapper.selectSametimeLog(userId, cdate, startTime, endTime);
    }

    @Override
    public int selectBySametimeLog(Integer byUserId, LocalDate cdate, LocalTime startTime, LocalTime endTime) {
        return consultationLogMapper.selectBySametimeLog(byUserId, cdate, startTime, endTime);
    }
}
