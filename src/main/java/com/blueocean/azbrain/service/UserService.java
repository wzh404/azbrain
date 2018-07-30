package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.model.UserFeedback;
import com.blueocean.azbrain.model.UserPoints;
import com.blueocean.azbrain.vo.SpecialistEditVo;
import com.blueocean.azbrain.vo.SpecialistVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserService {
    /**
     * get user by id.
     *
     * @param id
     * @return
     */
    User get(Integer id);

    /**
     * get user by login name.
     *
     * @param login login name
     * @return user
     */
    User getUserByName(String login);

    User getUserByKCode(String kcode);


    /**
     * 查找专家
     *
     * @param vo
     * @return
     */
    Page<User> searchSpecialist(int page, int pageSize, SpecialistVo vo);

    /**
     * 我的详情
     *
     * @param userId
     * @return
     */
    Map<String, Object> profile(Integer userId);

    /**
     * 专家详情
     *
     * @param userId
     * @return
     */
    Map<String, Object> specialistProfile(Integer userId);

    /**
     * 可咨询条件
     *
     * @param userId
     * @return
     */
    Map<String, Object> consultationConditions(Integer userId);

    /**
     * 用户积分列表
     *
     * @param userId
     * @return
     */
    Page<UserPoints> listUserPoints(int page, int pageSize, Integer userId);

    /**
     * 获取用户总积分
     *
     * @param userId
     * @return
     */
    Integer myPoint(Integer userId);

    /**
     * 列表主题专家
     *
     * @param page
     * @param pageSize
     * @param topicId
     * @return
     */
    Page<User> topicSpecialists(int page, int pageSize, Integer topicId);

    /**
     * 新增用户反馈意见
     *
     * @param feedback
     * @return
     */
    int insertUserFeedback(UserFeedback feedback);

    List<Map<String, Object>> getSpecialistLabels(Integer uid);

    int updateLogin(Integer userId);

    int changeSpecialistLabel(SpecialistEditVo vo);

    int cancelNotify(Integer userId);

    int notify(Integer userId, String content);
}

