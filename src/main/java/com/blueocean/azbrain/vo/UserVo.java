package com.blueocean.azbrain.vo;

import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.util.AZBrainConstants;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class UserVo {
    private Integer userId;
    @NotNull
    private String name;
    @NotNull
    private String pinyin;
    @NotNull
    private String kcode;
    @NotNull
    private String bu;

    public User asUser(){
        return new User(name, kcode, pinyin, bu);
    }
}
