package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.model.ArticleEvaluate;
import com.blueocean.azbrain.model.UserLikeArticle;
import com.blueocean.azbrain.vo.ArticleEvaluateVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    /**
     * 文章详情
     *
     * @param id
     * @return
     */
    Article get(Integer id);

    /**
     * 文章搜索
     *
     * @param page
     * @param pageSize
     * @param key
     * @return
     */
    Page<Article> search(int page, int pageSize, String key);

    /**
     * 大家都在看
     *
     * @param conditionMap
     * @return
     */
    Page<Article> watch(int page, int pageSize, Map<String, Object> conditionMap);

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
    int unlike(Integer articleId, Integer userId);

    /**
     * 用户是否已点赞
     *
     * @param articleId
     * @param userId
     * @return
     */
    boolean isLiked(Integer articleId, Integer userId);

    /**
     *
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    Page<Article> specialistArticles(int page, int pageSize, Integer userId);

    /**
     *
     * @param v
     * @return
     */
    int insertArticleEvaluate(ArticleEvaluateVo v);

    /**
     *
     * @param userId
     * @param articleId
     * @return
     */
    List<ArticleEvaluate> getArticleEvaluate(Integer userId, Integer articleId);

    /**
     *
     * @param userId
     * @param articleId
     * @return
     */
    int deleteArticleEvaluate(Integer userId, Integer articleId);
}
