package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.model.UserPoints;
import com.blueocean.azbrain.vo.SpecialistVo;
import com.blueocean.azbrain.vo.UserVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * get user by id.
     *
     * @param id
     * @return
     */
    User get(Integer id);

    /**
     *
     * @param user
     * @return
     */
    int newUser(User user);

    /**
     * get user by login name.
     *
     * @param login login name
     * @return user
     */
    User getUserByName(@Param("name")String login);

    /**
     *
     * @param kcode
     * @return
     */
    User getUserByKCode(@Param("kcode")String kcode);

    /**
     * paging query user.
     *
     * @return
     */
    Page<User> findByPage(Map<String, Object> conditionMap);

    /**
     * 查找专家
     *
     * @param vo
     * @return
     */
    Page<User> searchSpecialist(SpecialistVo vo);

    /**
     * 专家评分
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> byUserAvgScore(@Param("userId")Integer userId);

    /**
     * 个人评分
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> userAvgScore(@Param("userId")Integer userId);

    /**
     * 可预约时间
     *
     * @param userId
     * @return
     */
    //List<Map<String, Object>> appointmentTime(@Param("userId")Integer userId);

    /**
     * 可咨询方式
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> consultWay(@Param("userId")Integer userId);

    /**
     * 专家标签
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> specialistLabels(@Param("userId")Integer userId);

    List<Map<String, Object>> selectpecialistLabels(@Param("userId")Integer userId);
    /**
     * 用户积分列表
     *
     * @param userId
     * @return
     */
    Page<UserPoints> listUserPoints(@Param("userId")Integer userId);

    /**
     * 获取用户总积分
     *
     * @param userId
     * @return
     */
    Integer myPoint(@Param("userId")Integer userId);

    /**
     * 列表主题专家
     *
     * @param topicId
     * @return
     */
    Page<User> topicSpecialists(@Param("topicId")Integer topicId);

    /**
     * 分页查询主题专家
     *
     * @param conditionMap
     * @return
     */
    Page<User> searchTopicSpecialists(Map<String, Object> conditionMap);

    /**
     * 新建积分
     *
     * @param userPoints
     * @return
     */
    int insertPoints(UserPoints userPoints);

    /**
     * 增加用户的咨询次数及咨询时长
     *
     * @param userId
     * @param duration
     * @param byUser  true : 被咨询人  false: 咨询人
     * @return
     */
    int incrConsultation(@Param("userId")Integer userId, @Param("duration")Integer duration, @Param("byUser")Boolean byUser);

    /**
     * 增加爽约次数及好评，差评次数
     *
     * @param userId
     * @param contract
     * @param level
     * @return
     */
    int updateContractAndLevel(@Param("userId")Integer userId, @Param("contract")Boolean contract, @Param("level")Integer level);

    /**
     *
     * @param userId
     * @param labels
     * @param label
     * @param duration
     * @return
     */
    int insertSpecialistLabel(@Param("userId")Integer userId, @Param("labels")List<Map<String, Object>> labels,
                              @Param("label")String label, @Param("duration") Integer duration);

    /**
     *
     * @param userId
     * @param ways
     * @return
     */
    int insertUserConsultationWay(@Param("userId")Integer userId, @Param("ways")List<Map<String, Object>> ways);

    /**
     *
     * @param userId
     * @param times
     * @return
     */
    //int insertUserAppointmentTime(@Param("userId")Integer userId, @Param("times")List<Map<String, Object>> times);

    /**
     *
     * @param userId
     * @param status
     * @return
     */
    int changeStatus(@Param("userId")Integer userId, @Param("status")String status);

    /**
     *
     * @param user
     * @return
     */
    int edit(UserVo userVo);



    /**
     *
     * @param userId
     * @return
     */
    User viewUser(@Param("userId")Integer userId);

    /**
     *
     * @param userId
     * @return
     */
    User viewSpecialist(@Param("userId")Integer userId);

    /**
     *
     * @param conditionMap
     * @return
     */
    Page<User> listSpecialist(Map<String, Object> conditionMap);

    /**
     *
     * @param key
     * @return
     */
    List<User> findSpecialistByKey(@Param("key")String key);

    /**
     *
     * @param userId
     * @return
     */
    String getUserStatus(@Param("userId")Integer userId);

    Page<User> findSpecialistByLabel(@Param("labels")List<String> labels);

    int updateLogin(@Param("userId")Integer userId);

    int changeSpecialistLabel(@Param("userId")Integer userId, @Param("labels")List<Map<String, Object>> labels,
                              @Param("label")String label);

    int notify(@Param("userId")Integer userId);

    int unNotify(@Param("userId")Integer userId);

    int chageConsultedStar(@Param("userId")Integer userId, @Param("star")BigDecimal star);

    int chageConsultStar(@Param("userId")Integer userId, @Param("star")BigDecimal star);
}