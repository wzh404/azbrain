package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.ArticleEvaluate;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ArticleEvaluateMapper {
    /**
     * 用户评价
     *
     * @param records
     * @return
     */
    int insertBatch(List<ArticleEvaluate> records);

    /**
     * 获取用户对文章的评价
     *
     * @param userId
     * @return
     */
    List<ArticleEvaluate> get(@Param("userId")Integer userId, @Param("articleId")Integer articleId);

    /**
     * 删除用户对文章的评价
     *
     * @param userId
     * @param articleId
     * @return
     */
    int delete(@Param("userId")Integer userId, @Param("articleId")Integer articleId);

    /**
     *　文章用户评价列表
     *
     * @param articleId
     * @return
     */
    Page<Map<String, Object>> evaluateOnArticle(@Param("articleId")Integer articleId);

    /**
     * 文章评价汇总
     *
     * @return
     */
    Page<Map<String, Object>> summaryArticleEvaluation();
}