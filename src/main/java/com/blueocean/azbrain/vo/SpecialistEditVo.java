package com.blueocean.azbrain.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class SpecialistEditVo {
    private Integer userId;

    // 一次可咨询时长
    private Integer duration;

    // 专家标签
    private List<Map<String, Object>> labels;

    // 可预约时间
    private List<Map<String, Object>> times;

    // 咨询方式
    private List<Map<String, Object>> ways;

    public String userLabel(){
        return labels.stream().map(m -> m.get("name").toString())
                .collect(Collectors.joining(","));
    }
}
