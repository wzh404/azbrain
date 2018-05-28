package com.blueocean.azbrain.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginVo {
    @NotNull
    private String name;
    @NotNull
    private String pwd;
}
