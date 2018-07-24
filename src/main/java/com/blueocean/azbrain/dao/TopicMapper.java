package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.Topic;
import com.blueocean.azbrain.model.UserFollowTopic;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface TopicMapper {
    /**
     *
     * @param record
     * @return
     */
    int insert(Topic record);

    /**
     *
     * @param topic
     * @return
     */
    int edit(Topic topic);

    /**
     *
     * @param id
     * @return
     */
    Topic get(Integer id);

    /**
     *
     * @param topicId
     * @return
     */
    int delete(@Param("topicId")Integer topicId);

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
     * 重置用户主题更新文章数量
     *
     * @param userId
     * @param topicId
     * @return
     */
    int resetUpdatedArticleNum(@Param("userId")Integer userId, @Param("topicId")Integer topicId);

    /**
     * 所有关注主题的用户，文章更新数加一
     *
     * @param topicId
     * @return
     */
    int increaseUpdatedArticleNum(@Param("topicId")Integer topicId);

    /**
     * 主题的文章数量
     *
     * @param topicId
     * @return
     */
    int topicArticleNum(@Param("topicId")Integer topicId);

    /**
     * 增加topic被咨询次数
     *
     * @param topicId
     * @return
     */
    int incrConsultedNum(@Param("topicId")Integer topicId);

    /**
     * 分页显示专题
     *
     * @param conditionMap
     * @return
     */
    Page<Topic> pageTopics(Map<String, Object> conditionMap);

    /**
     *
     * @param topicId
     * @param userId
     * @return
     */
    int insertTopicSpecialist(@Param("topicId")Integer topicId, @Param("userId")Integer userId);

    /**
     *
     * @param topicId
     * @param userId
     * @return
     */
    int deleteTopicSpecialist(@Param("topicId")Integer topicId, @Param("userId")Integer userId);
}