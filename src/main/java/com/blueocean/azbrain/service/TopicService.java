package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.model.Topic;
import com.blueocean.azbrain.model.UserFollowTopic;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TopicService {
    Topic get(int id);
    /**
     *
     * @param page
     * @param pageSize
     * @param conditionMap
     * @return
     */
    Page<Topic> myFollowTopics(int page, int pageSize, Map<String, Object> conditionMap);

    /**
     *
     * @param topicId
     * @return
     */
    Page<Article> listArticlesByTopic(int page, int pageSize, Integer topicId);

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
    int unfollow(Integer userId, Integer topicId);

    /**
     * 是否关注了主题
     *
     * @param userId
     * @param topicId
     * @return
     */
    boolean isFollowed(Integer userId, Integer topicId);

    /**
     * 分页列表主题
     *
     * @param page
     * @param pageSize
     * @param conditionMap
     * @return
     */
    Page<Topic> pageTopics(int page, int pageSize, Map<String, Object> conditionMap);

    /**
     *
     * @param topic
     * @return
     */
    int newTopic(Topic topic);

    /**
     *
     * @param topic
     * @return
     */
    int editTopic(Topic topic);

    /**
     *
     * @param topic
     * @return
     */
    Topic viewTopic(Integer topic);

    /**
     *
     * @param topicId
     * @param userId
     * @return
     */
    int newTopicSpecialist(Integer topicId, Integer userId);

    /**
     *
     * @param topicId
     * @param userId
     * @return
     */
    int deleteTopicSpecialist(Integer topicId, Integer userId);

    /**
     * 重置用户主题更新文章数量
     *
     * @param userId
     * @param topicId
     * @return
     */
    int resetUpdatedArticleNum(Integer userId, Integer topicId);
}
