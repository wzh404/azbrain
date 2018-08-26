package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.model.EventLog;
import com.blueocean.azbrain.model.UserLikeArticle;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
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
    Article get(@Param("id")Integer id);

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

    /**********************Manager *********************/

    /**
     * 分页列表文章
     * 草稿09，
     * 已发布00，
     * 置顶00 && top_flag = 1
     *
     * @param conditionMap
     * @return
     */
    Page<Article> pageList(Map<String, Object> conditionMap);

    /**
     *
     * @param articleId
     * @param status
     * @return
     */
    int changeStatus(@Param("articleId")Integer articleId, @Param("status")String status);

    /**
     *
     * @param articleId
     * @return
     */
    int top(@Param("articleId")Integer articleId);

    /**
     *
     * @param articleId
     * @return
     */
    int untop(@Param("articleId")Integer articleId);

    /**
     *
     * @param record
     * @return
     */
    int edit(Article record);

    /**
     *
     * @param ids
     * @return
     */
    int changeStatusBatch(@Param("ids")List<Integer> ids);

    /**
     * 分组汇总文章数
     *
     * @return
     */
    List<Map<String, Object>> totalNum();

    /**
     * 汇总置顶文章数量
     *
     * @return
     */
    Long totalTopNum();

    /**
     * 统计文章点击数
     *
     * @param articleId
     * @param clickedNum
     * @return
     */
    int changeClickedNum(@Param("articleId")Integer articleId, @Param("clickedNum")Integer clickedNum);

    /**
     * 文章点击数下载
     *
     * @return
     */
    List<Article> listArticleClickNum();
}

