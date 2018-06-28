package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.Article;
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
     *
     * @param conditionMap
     * @return
     */
    Page<Article> listByOrder(Map<String, Object> conditionMap);
}

