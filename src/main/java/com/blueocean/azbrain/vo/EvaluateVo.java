package com.blueocean.azbrain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EvaluateVo {
    private String code;
    private String name;
    private String value;
    private String type;
}
