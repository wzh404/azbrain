package com.blueocean.azbrain.vo;

import com.blueocean.azbrain.model.User;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserVo {
    private Integer id;
    private String name;
    private String gender;
    private String jobNumber;
    private String businessUnit;
    private String mobile;
    private String photo;

    public User asUser(){
        User user = new User();
        user.setId(this.id);
        user.setName(this.name);
        user.setJobNumber(this.jobNumber);
        user.setBusinessUnit(this.businessUnit);
        user.setMobile(this.mobile);
        user.setGender(this.gender);
        user.setPhoto(this.photo);
        user.setUserType("01");
        user.setPassword("");
        user.setPinyin("");
        user.setConsultedStar(new BigDecimal(0));
        user.setConsultStar(new BigDecimal(0));
        user.setCreateTime(LocalDateTime.now());
        user.setStatus("00");
        user.setCreateBy(0);
        user.setCreateTime(LocalDateTime.now());
        if (StringUtils.isEmpty(user.getPhoto())) {
            if ("F".equalsIgnoreCase(user.getGender())) {
                user.setPhoto("http://community.blueocean-health.com/static/user_logo_f.jpg");
            } else {
                user.setPhoto("http://community.blueocean-health.com/static/user_logo_m.jpg");
            }
        }

        return user;
    }
}
