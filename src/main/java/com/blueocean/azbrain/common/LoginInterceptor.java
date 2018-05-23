package com.blueocean.azbrain.common;

import com.blueocean.azbrain.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("---{}", request.getRequestURI());

        if (request.getRequestURI().equalsIgnoreCase("/user/apply/access-token")){
            return true;
        }

        String accessToken = request.getHeader("access_token");
        if (accessToken == null){
            logger.info("access token is null");
            return false;
        }

        int userId = TokenUtil.getUserId(accessToken);
        if (userId <= 0){
            logger.info("auserId is null");
            return false;
        }

        request.setAttribute("userId", userId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
