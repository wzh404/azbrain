package com.blueocean.azbrain.common;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
import java.io.IOException;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Value(value = "${azbrain.whitelist.uri}")
    private String[] uris;


    @Value(value = "${server.servlet.context-path}")
    private String contextPath;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            return true;
        }

        for (String uri : uris) {
            if (request.getRequestURI().startsWith(uri)) {
                return true;
            }
        }
        String managerUri = "/manager";
        if (contextPath != null && !contextPath.trim().equalsIgnoreCase("/")) {
            managerUri = contextPath + managerUri;
        }

        logger.info(managerUri);
        if (request.getRequestURI().startsWith(managerUri)) {
            return managerHandle(request, response);
        } else {
            return userHandle(request, response);
        }
    }

    /**
     * 微信客户端session处理
     *
     * @param request
     * @return
     */
    private boolean userHandle(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = request.getHeader("access_token");
        if (accessToken == null) {
            logger.info("access token is null");
            sendJsonMessage(response, ResultObject.fail(ResultCode.PLEASE_LOGIN));
            return false;
        }

        int userId = TokenUtil.getUserId(accessToken);
        if (userId <= 0) {
            logger.info("userId is null");
            sendJsonMessage(response, ResultObject.fail(ResultCode.PLEASE_LOGIN));
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
    private boolean managerHandle(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (session.getAttribute(AZBrainConstants.SESSION_USER_ID) == null ||
                session.getAttribute(AZBrainConstants.SESSION_USER_NAME) == null) {
            logger.error("please login first.");
            sendJsonMessage(response, ResultObject.fail(ResultCode.PLEASE_LOGIN));
            return false;
        }

        return true;
    }

    private void sendJsonMessage(HttpServletResponse response, Object obj) {
        response.setContentType("application/json; charset=utf-8");

        try {
            PrintWriter writer = response.getWriter();
            writer.print(JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat));
            writer.close();
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
