package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.common.status.ArticleStatus;
import com.blueocean.azbrain.dao.ArticleEvaluateMapper;
import com.blueocean.azbrain.dao.ArticleMapper;
import com.blueocean.azbrain.dao.TopicMapper;
import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.model.ArticleEvaluate;
import com.blueocean.azbrain.model.UserLikeArticle;
import com.blueocean.azbrain.service.ArticleService;
import com.blueocean.azbrain.vo.ArticleEvaluateVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleEvaluateMapper articleEvaluateMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Override
    public Article get(Integer id) {
        return articleMapper.get(id);
    }

    @Override
    public Page<Article> search(int page, int pageSize, String key) {
        PageHelper.startPage(page, pageSize);
        return articleMapper.search(key);
    }

    @Override
    public Page<Article> watch(int page, int pageSize, Map<String, Object> conditionMap) {
        PageHelper.startPage(page, pageSize);
        return articleMapper.listByOrder(new HashMap<>());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int like(UserLikeArticle likeArticle) {
        return articleMapper.like(likeArticle);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int unlike(Integer articleId, Integer userId) {
        return articleMapper.unlike(articleId, userId);
    }

    @Override
    public boolean isLiked(Integer articleId, Integer userId) {
        return articleMapper.likeNum(articleId, userId) > 0 ? true : false;
    }

    @Override
    public Page<Article> specialistArticles(int page, int pageSize, Integer userId) {
        PageHelper.startPage(page, pageSize);
        return articleMapper.specialistArticles(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int insertArticleEvaluate(ArticleEvaluateVo v) {
        return articleEvaluateMapper.insertBatch(v.asArticleEvaluates());
    }

    @Override
    public List<ArticleEvaluate> getArticleEvaluate(Integer userId, Integer articleId) {
        return articleEvaluateMapper.get(userId, articleId);
    }

    @Override
    public int deleteArticleEvaluate(Integer userId, Integer articleId) {
        return articleEvaluateMapper.delete(userId, articleId);
    }

    @Override
    public Page<Article> draftList(Integer page, Integer pageSize, Map<String, Object> conditionMap) {
        conditionMap.put("status", ArticleStatus.DRAFT.getCode());

        PageHelper.startPage(page, pageSize);
        return articleMapper.pageList(conditionMap);
    }

    @Override
    public Page<Article> list(Integer page, Integer pageSize, Map<String, Object> conditionMap) {
        conditionMap.put("status", ArticleStatus.NORMAL.getCode());

        PageHelper.startPage(page, pageSize);
        return articleMapper.pageList(conditionMap);
    }

    @Override
    public Page<Article> topList(Integer page, Integer pageSize, Map<String, Object> conditionMap) {
        conditionMap.put("status", ArticleStatus.NORMAL.getCode());
        conditionMap.put("top", 1);

        PageHelper.startPage(page, pageSize);
        return articleMapper.pageList(conditionMap);
    }

    @Override
    public int publish(Integer articleId) {
        return articleMapper.changeStatus(articleId, "00");
    }

    @Override
    public int top(Integer articleId) {
        return articleMapper.top(articleId);
    }

    @Override
    public int untop(Integer articleId) {
        return articleMapper.untop(articleId);
    }

    @Override
    public int edit(Article record) {
        return articleMapper.edit(record);
    }

    @Override
    public int insert(Article record) {
        topicMapper.increaseUpdatedArticleNum(record.getTopicId());
        return articleMapper.insert(record);
    }

    @Override
    public Page<Map<String, Object>> evaluateOnArticle(int page, int pageSize, Integer articleId) {
        PageHelper.startPage(page, pageSize);
        return articleEvaluateMapper.evaluateOnArticle(articleId);
    }

    @Override
    public Page<Map<String, Object>> summaryArticleEvaluation(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return articleEvaluateMapper.summaryArticleEvaluation();
    }

    @Override
    public int changeStatusBatch(List<Integer> ids) {
        return articleMapper.changeStatusBatch(ids);
    }
}
