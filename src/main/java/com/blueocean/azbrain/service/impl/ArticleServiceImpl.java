package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.dao.ArticleMapper;
import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.service.ArticleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

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
}
