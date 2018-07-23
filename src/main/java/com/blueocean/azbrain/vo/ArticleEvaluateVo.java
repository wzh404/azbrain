package com.blueocean.azbrain.vo;

import com.blueocean.azbrain.model.ArticleEvaluate;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class ArticleEvaluateVo {
    private Integer articleId;
    private Integer userId;

    List<EvaluateVo> evaluates;

    Function<EvaluateVo, ArticleEvaluate> f = v -> {
        ArticleEvaluate a = new ArticleEvaluate();
        a.setArticleId(this.articleId);
        a.setUserId(userId);
        a.setCreateTime(LocalDateTime.now());
        a.setCode(v.getCode());
        a.setName(v.getName());
        a.setValue(v.getValue());
        a.setStatus("00");

        return a;
    };

    public List<ArticleEvaluate> asArticleEvaluates(){
        if (evaluates == null || evaluates.isEmpty()){
            return new ArrayList<>();
        }

        return evaluates.stream()
                .map(f)
                .collect(Collectors.toList());
    }
}
