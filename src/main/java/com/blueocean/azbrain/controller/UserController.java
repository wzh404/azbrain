package com.blueocean.azbrain.controller;

import com.blueocean.azbrain.common.SessionObject;
import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.UserFeedback;
import com.blueocean.azbrain.service.UserService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by @author wangzunhui on 2018/3/12.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 关注问题
     *
     * @param request
     * @param questionId
     * @return
     */
    @RequestMapping(value="/follow-question", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject follow(HttpServletRequest request,
            @RequestParam("question_id") Integer questionId){
        int userId = (int)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        int ret = userService.follow(userId, questionId);
        if (ret < 0){
            return ResultObject.fail(ResultCode.USER_QUESTION_FOLLOWED);
        } else {
            return ResultObject.ok();
        }
    }

    /**
     * 取消关注
     *
     * @param request
     * @param questionId
     * @return
     */
    @RequestMapping(value="/unfollow-question", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject unfollow(HttpServletRequest request,
            @RequestParam("question_id") Integer questionId){
        int userId = (int)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        int ret = userService.unfollow(userId, questionId);
        if (ret < 0){
            return ResultObject.fail(ResultCode.USER_QUESTION_FOLLOWED);
        } else {
            return ResultObject.ok();
        }
    }

    /**
     * 点赞回答
     *
     * @param request
     * @param answerId
     * @return
     */
    @RequestMapping(value="/like-answer", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject like(HttpServletRequest request,
            @RequestParam("answer_id") Integer answerId){
        int userId = (int)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        int ret = userService.like(userId, answerId);
        if (ret < 0){
            return ResultObject.fail(ResultCode.USER_ANSWER_LIKED);
        } else {
            return ResultObject.ok();
        }
    }

    /**
     * 取消点赞
     *
     * @param request
     * @param answerId
     * @return
     */
    @RequestMapping(value="/unlike-answer", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject unlike(HttpServletRequest request,
            @RequestParam("answer_id") Integer answerId){
        int userId = (int)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        int ret = userService.unlike(userId, answerId);
        if (ret < 0){
            return ResultObject.fail(ResultCode.USER_ANSWER_LIKED);
        } else {
            return ResultObject.ok();
        }
    }

    /**
     * 获取access_token.
     *
     * @param code
     * @return
     */
    @RequestMapping(value="/apply/access-token", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject accessToken(@RequestParam("code") String code){
        User user = userService.getUserByName(code);
        if (user == null){
            logger.warn("The user does not exist ");
            return ResultObject.fail(ResultCode.USER_ACCESS_TOKEN);
        }
        SessionObject sessionObject = new SessionObject(user.getId(), code);
        String token = TokenUtil.createJwtToken(sessionObject.toJson()).orElse(AZBrainConstants.EMPTY_STRING);
        return ResultObject.ok("access_token", token);
    }

    /**
     * 意见反馈
     *
     * @param request
     * @param feedback
     * @param classification
     * @param photo
     * @return
     */
    @RequestMapping(value="/apply/feedback", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject feedback(HttpServletRequest request,
                                 @RequestParam("feedback") String feedback,
                                 @RequestParam("classification") String classification,
                                 @RequestParam(value="photo", required = false) String photo){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        User user = userService.get(userId);
        UserFeedback userFeedback = new UserFeedback(userId, user.getName(), feedback);
        userFeedback.setPhoto(photo);
        userFeedback.setClassification(classification);
        int rows = userService.insertUserFeedback(userFeedback);

        return rows > 0 ? ResultObject.ok() : ResultObject.fail(ResultCode.USER_ILLEGAL_STATUS);
    }
}
