package com.blueocean.azbrain.model;

import com.blueocean.azbrain.common.InviteCodeStatus;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Data
public class UserInviteCode {
    private Integer id;

    private String inviteCode;

    private Integer validity;

    private Date startTime;

    private Integer receiveUserId;

    private Date receiveTime;

    private String status;

    public boolean isValid(){
        if (InviteCodeStatus.RECEIVED.getCode().equalsIgnoreCase(status)){
            return false;
        }

        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(new Date());

        Calendar cal = Calendar.getInstance();
        currentCal.setTime(this.startTime);
        cal.add(Calendar.DATE, this.validity);
        if (currentCal.compareTo(cal) <= 0){
            return true;
        }

        return false;
    }
}