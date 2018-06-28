package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.Article;
import com.github.pagehelper.Page;

import java.util.Map;

public interface ArticleService {

    Page<Article> search(int page, int pageSize, String key);

    /**
     * 大家都在看
     *
     * @param conditionMap
     * @return
     */
    Page<Article> watch(int page, int pageSize, Map<String, Object> conditionMap);
}
