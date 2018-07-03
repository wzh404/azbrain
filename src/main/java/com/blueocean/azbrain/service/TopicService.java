package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.model.Topic;
import com.blueocean.azbrain.model.UserFollowTopic;
import com.github.pagehelper.Page;

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
}
