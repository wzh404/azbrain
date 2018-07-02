package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.Topic;
import com.blueocean.azbrain.model.UserFollowTopic;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface TopicMapper {
    int insert(Topic record);
    Topic get(Integer id);

    /**
     *
     * @param conditionMap
     * @return
     */
    Page<Topic> userFollowTopics(Map<String, Object> conditionMap);

    /**
     * 关注
     *
     * @param followTopic
     * @return
     */
    int follow(UserFollowTopic followTopic);

    /**
     * 取消关注
     *
     * @param userId
     * @param topicId
     * @return
     */
    int unfollow(@Param("userId")Integer userId, @Param("topicId")Integer topicId);

    /**
     * 主题是否被关注
     *
     * @param userId
     * @param topicId
     * @return
     */
    int followNum(@Param("userId")Integer userId, @Param("topicId")Integer topicId);

    /**
     * 增加topic被咨询次数
     *
     * @param topicId
     * @return
     */
    int incrConsultedNum(@Param("topicId")Integer topicId);
}