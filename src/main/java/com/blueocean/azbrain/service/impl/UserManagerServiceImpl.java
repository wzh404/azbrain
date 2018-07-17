package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.common.status.UserStatus;
import com.blueocean.azbrain.dao.UserEvaluateMapper;
import com.blueocean.azbrain.dao.UserFeedbackMapper;
import com.blueocean.azbrain.dao.UserMapper;
import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.model.UserFeedback;
import com.blueocean.azbrain.service.UserManagerService;
import com.blueocean.azbrain.vo.SpecialistEditVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Splitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userManagerService")
public class UserManagerServiceImpl implements UserManagerService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserFeedbackMapper userFeedbackMapper;

    @Autowired
    private UserEvaluateMapper userEvaluateMapper;

    /**
     * 分页列表用户
     *
     * @param page
     * @param pageSize
     * @param conditionMap
     * @return
     */
    @Override
    public Page<User> findByPage(int page, int pageSize, Map<String, Object> conditionMap) {
        PageHelper.startPage(page, pageSize);
        return userMapper.findByPage(conditionMap);
    }

    @Override
    public UserFeedback getUserFeedback(int id) {
        return userFeedbackMapper.get(id);
    }

    @Override
    public Page<UserFeedback> listUserFeedback(int page, int pageSize, HashMap<String, Object> conditionMap) {
        PageHelper.startPage(page, pageSize);
        return userFeedbackMapper.list(conditionMap);
    }

    /**
     * 编辑专家
     *
     * @param vo
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int editSpecialist(SpecialistEditVo vo) {
        //userMapper.insertUserAppointmentTime(vo.getUserId(), vo.getTimes());
        userMapper.insertUserConsultationWay(vo.getUserId(), vo.getWays());
        return userMapper.insertSpecialistLabel(vo.getUserId(), vo.getLabels(), vo.userLabel(), vo.getDuration());
    }

    /**
     * 根据用户名和手机查找用户
     *
     * @param key
     * @return
     */
    @Override
    public List<User> findSpecialistByKey(String key) {
        return userMapper.findSpecialistByKey(key);
    }

    /**
     * 分页列表专家
     *
     * @param conditionMap
     * @return
     */
    @Override
    public Page<User> listSpecialist(int page, int pageSize, Map<String, Object> conditionMap) {
        PageHelper.startPage(page, pageSize);
        return userMapper.listSpecialist(conditionMap);
    }

    /**
     * 更新用户状态
     *
     * @param userId
     * @param status
     * @return
     */
    @Override
    public int changeStatus(Integer userId, String status) {
        String userStatus = userMapper.getUserStatus(userId);
        if (userStatus == null){
            return -1;
        }

        if (userStatus.equalsIgnoreCase(status)){
            return 0;
        }

        // 用户为删除状态时不能停用和启用用户。
        if (!status.equalsIgnoreCase(UserStatus.DELETED.getCode()) &&
            userStatus.equalsIgnoreCase(UserStatus.DELETED.getCode())){
            return 0;
        }

        return userMapper.changeStatus(userId, status);
    }

    /**
     * 编辑用户
     *
     * @param user
     * @return
     */
    @Override
    public int edit(User user) {
        return userMapper.edit(user);
    }

    /**
     * 查看用户
     * @param userId
     * @return
     */
    @Override
    public User viewUser(Integer userId) {
        return userMapper.viewUser(userId);
    }

    @Override
    public Map<String, Object>  viewSpecialist(Integer userId) {
        Map<String, Object> map = new HashMap<>();
        User user = userMapper.viewSpecialist(userId);
        if (user == null) return map;
        map.put("user", user);
        //map.put("times", userMapper.appointmentTime(userId));
        map.put("ways", userMapper.consultWay(userId));
        map.put("labels", userMapper.specialistLabels(userId));
        return map;
    }

    @Override
    public User getUserByName(String name) {
        return userMapper.getUserByName(name);
    }

    @Override
    public Page<User> searchTopicSpecialists(int page, int pageSize, Map<String, Object> conditionMap) {
        PageHelper.startPage(page, pageSize);
        return userMapper.searchTopicSpecialists(conditionMap);
    }

    @Override
    public Page<User> findSpecialistByLabel(int page, int pageSize, List<String> labels) {
        PageHelper.startPage(page, pageSize);
        return userMapper.findSpecialistByLabel(labels);
    }

    @Override
    public Page<Map<String, Object>> evaluateOnUser(int page, int pageSize, Integer byUserId) {
        PageHelper.startPage(page, pageSize);
        return userEvaluateMapper.evaluateOnUser(byUserId);
    }

    @Override
    public Page<Map<String, Object>> summaryUserEvaluation(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return userEvaluateMapper.summaryUserEvaluation(1);
    }

    @Override
    public Page<Map<String, Object>> summaryByUserEvaluation(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return userEvaluateMapper.summaryUserEvaluation(0);
    }
}
