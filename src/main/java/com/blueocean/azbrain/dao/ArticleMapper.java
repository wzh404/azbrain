package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.model.UserLikeArticle;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface ArticleMapper {
    /**
     *
     * @param record
     * @return
     */
    int insert(Article record);

    /**
     *
     * @param id
     * @return
     */
    Article get(Integer id);

    /**
     * 关键字搜索文章
     *
     * @param key
     * @return
     */
    Page<Article> search(@Param("key")String key);

    /**
     * 大家都在看的文章
     *
     * @param conditionMap
     * @return
     */
    Page<Article> listByOrder(Map<String, Object> conditionMap);

    /**
     *
     * @param topicId
     * @return
     */
    Page<Article> listByTopic(@Param("topicId") Integer topicId);

    /**
     * 用户点赞文章
     *
     * @param likeArticle
     * @return
     */
    int like(UserLikeArticle likeArticle);

    /**
     * 用户取消点赞
     *
     * @param articleId
     * @param userId
     * @return
     */
    int unlike(@Param("articleId")Integer articleId, @Param("userId")Integer userId);

    /**
     * 用户是否点赞
     *
     * @param articleId
     * @param userId
     * @return
     */
    int likeNum(@Param("articleId")Integer articleId, @Param("userId")Integer userId);

    /**
     * 获取专家发表文章列表
     *
     * @param userId
     * @return
     */
    Page<Article> specialistArticles(@Param("userId") Integer userId);
}

