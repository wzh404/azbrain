package com.blueocean.azbrain.vo;

import lombok.Data;

import java.util.List;

@Data
public class SpecialistLabelVo {
    private Integer page;
    List<String> labels;
}
