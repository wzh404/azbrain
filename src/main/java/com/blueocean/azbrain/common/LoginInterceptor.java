package com.blueocean.azbrain.common;

import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Value(value = "${azbrain.whitelist.uri}")
    private String[] uris;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("----------{}", request.getRequestURI());
        if (request.getMethod().equalsIgnoreCase("OPTIONS")){
            return true;
        }

        for (String uri : uris){
            if (request.getRequestURI().startsWith(uri)){
                return true;
            }
        }

        if (request.getRequestURI().startsWith("/manager")){
            return true; //managerHandle(request);
        } else {
            return userHandle(request);
        }
    }

    /**
     * 微信客户端session处理
     *
     * @param request
     * @return
     */
    private boolean userHandle(HttpServletRequest request){
        String accessToken = request.getHeader("access_token");
        if (accessToken == null){
            logger.info("access token is null");
            return false;
        }

        int userId = TokenUtil.getUserId(accessToken);
        if (userId <= 0){
            logger.info("userId is null");
            return false;
        }

        request.setAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID, userId);
        return true;
    }

    /**
     * PC运营端session处理
     *
     * @param request
     * @return
     */
    private boolean managerHandle(HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session.getAttribute(AZBrainConstants.SESSION_USER_ID) == null ||
            session.getAttribute(AZBrainConstants.SESSION_USER_NAME) == null){
            logger.error("please login first.");
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Do nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Do nothing
    }
}
