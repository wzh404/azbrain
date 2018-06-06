package com.blueocean.azbrain.common;

import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.util.AZBrainConstants;
import lombok.Data;

import javax.servlet.http.HttpSession;

@Data
public class ManagerSessionObject {
    private Integer id;
    private String name;

    public static ManagerSessionObject fromSession(HttpSession session){
        ManagerSessionObject mso = new ManagerSessionObject();
        mso.setId((Integer)session.getAttribute(AZBrainConstants.SESSION_USER_ID));
        mso.setName((String)session.getAttribute(AZBrainConstants.SESSION_USER_NAME));

        //TODO
        //mso.setId(7);
        //mso.setName("admin");
        return mso;
    }

    public static void toSession(HttpSession session, User user){
        session.setAttribute(AZBrainConstants.SESSION_USER_ID, user.getId());
        session.setAttribute(AZBrainConstants.SESSION_USER_NAME, user.getLoginName());
    }
}
