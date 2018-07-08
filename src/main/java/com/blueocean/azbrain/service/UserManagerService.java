package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.model.UserFeedback;
import com.blueocean.azbrain.vo.SpecialistEditVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserManagerService {
    /**
     * paging query user.
     *
     * @param page
     * @param pageSize
     * @return
     */
    Page<User> findByPage(int page, int pageSize, Map<String, Object> conditionMap);

    /**
     * 查看用户反馈意见
     *
     * @param id
     * @return
     */
    UserFeedback getUserFeedback(int id);

    /**
     * 分页查询用户反馈意见
     *
     * @param conditionMap
     * @return
     */
    Page<UserFeedback> listUserFeedback(int page, int pageSize, HashMap<String, Object> conditionMap);

    /**
     * 编辑专家
     *
     * @param vo
     * @return
     */
    int editSpecialist(SpecialistEditVo vo);

    /**
     * 根据名称或手机号查找用户
     *
     * @param key
     * @return
     */
    List<User> findSpecialistByKey(String key);

    /**
     * 专家列表
     *
     * @param conditionMap
     * @return
     */
    Page<User> listSpecialist(int page, int pageSize, Map<String, Object> conditionMap);

    /**
     * 改变用户状态（停用，启用）
     *
     * @param userId
     * @param status
     * @return
     */
    int changeStatus(Integer userId, String status);

    /**
     * 编辑用户
     *
     * @param user
     * @return
     */
    int edit(User user);

    /**
     * 查看用户
     *
     * @param userId
     * @return
     */
    User viewUser(Integer userId);

    /**
     * 查看用户
     *
     * @param userId
     * @return
     */
    Map<String, Object> viewSpecialist(Integer userId);

    /**
     *
     * @param name
     * @return
     */
    User getUserByName(String name);

    /**
     * 分页查询主题专家
     *
     * @param conditionMap
     * @return
     */
    Page<User> searchTopicSpecialists(int page, int pageSize, Map<String, Object> conditionMap);

    /**
     * 根据标签查找专家
     *
     * @param page
     * @param pageSize
     * @param labels
     * @return
     */
    Page<User> findSpecialistByLabel(int page, int pageSize, List<String> labels);
}
