package com.blueocean.azbrain.vo;

import com.blueocean.azbrain.model.UserEvaluate;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

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

        return u;
    };

    public List<UserEvaluate> asUserEvaluates(){
        if (evaluates == null || evaluates.isEmpty()){
            return new ArrayList<>();
        }

        return evaluates.stream()
                .map(f)
                .collect(Collectors.toList());
    }
}
