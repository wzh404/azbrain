package com.blueocean.azbrain.vo;

import com.blueocean.azbrain.model.UserEvaluate;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEvaluateVo {
    private Integer userId;
    private Integer byUserId;
    private Integer logId;

    // true: 被咨询人评价  false: 咨询人评价
    private Boolean flag;

    List<EvaluateVo> evaluates;

    Function<EvaluateVo, UserEvaluate> f = v -> {
        UserEvaluate u = new UserEvaluate();
        u.setUserId(userId);
        u.setByUserId(this.byUserId);
        u.setLogId(this.logId);
        u.setCreateTime(LocalDateTime.now());
        u.setCode(v.getCode());
        u.setName(v.getName());
        u.setValue(v.getValue());
        u.setValueType(v.getType());

        return u;
    };

    public boolean empty(){
        return evaluates == null || evaluates.isEmpty();
    }

    public List<UserEvaluate> asUserEvaluates(){
        if (empty()){
            return new ArrayList<>();
        }

        return evaluates.stream()
                .map(f)
                .collect(Collectors.toList());
    }

    public BigDecimal avgStar(){
        if (empty()){
            return new BigDecimal(0);
        }

        double d = evaluates.stream()
                .filter(v -> v.getType().equalsIgnoreCase("star"))
                .mapToDouble(v->Double.parseDouble(v.getValue()))
                .average()
                .orElse(0);
        return new BigDecimal(d).setScale(1, BigDecimal.ROUND_HALF_UP);
    }

    public boolean breakContract(){
        return evaluates.stream()
                .filter(v -> v.getCode().equalsIgnoreCase("breakContract"))
                .findFirst()
                .map(v -> v.getValue().equalsIgnoreCase("1"))
                .orElse(false);
    }
}
