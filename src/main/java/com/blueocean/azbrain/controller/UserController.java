package com.blueocean.azbrain.controller;

import com.blueocean.azbrain.common.SessionObject;
import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.util.CryptoUtil;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.service.UserService;
import com.blueocean.azbrain.util.TokenUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @Resource(name="smsCache")
    private Cache<String, String> smsCache;
    /**
     * add new user.
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value="/new", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject newUser(@RequestParam("mobile")String mobile,
                                @RequestParam("invite_code")String inviteCode,
                                @RequestParam("invite_token")String inviteToken) {
        String savedMobile = smsCache.getIfPresent(inviteToken);
        if (savedMobile == null || !savedMobile.equalsIgnoreCase(mobile)){
            return ResultObject.fail(ResultCode.USER_LOGIN_FAILED);
        }
        String kcode = "";
        String bu = "";
        String pinyin="";
        User user = new User(mobile, kcode, AZBrainConstants.DEFAULT_COMPANY_ID, pinyin, bu);

        int rows = userService.insert(user, inviteCode);
        if (rows == 1){
            return ResultObject.ok();
        }

        return ResultObject.fail(ResultCode.USER_ADD_FAILED);
    }

    /**
     * User login.
     *
     * @param mobile login name.
     * @param smscode user password.
     * @return
     */
    @RequestMapping(value="/login", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject login(@RequestParam("mobile") String mobile,
            @RequestParam("sms_code") String smscode){

        String code = smsCache.getIfPresent(mobile);
        if (code == null || !code.equalsIgnoreCase(smscode)){
            return ResultObject.fail(ResultCode.USER_LOGIN_FAILED);
        }

        User user = userService.getUserByName(mobile);
        if (user == null){
            // 需要输入邀请码注册
            StringBuilder seed = new StringBuilder(randomCode());
            seed.append(System.currentTimeMillis());
            String inviteToken = CryptoUtil.signature(seed.toString());
            logger.info("invite_token = {}", inviteToken);
            smsCache.put(inviteToken, mobile);
            return ResultObject.ok("invite_token", inviteToken);
        }

        if (user.deleted()){
            return ResultObject.fail(ResultCode.USER_LOGIN_FAILED);
        }

        if (!user.normal()){
            return ResultObject.fail(ResultCode.USER_LOGIN_FAILED);
        }

        return ResultObject.ok();
    }

    /**
     * 生成6位随机数
     */
    public static int randomCode() {
        return (int) ((Math.random() * 9 + 1) * 100000);
    }

    /**
     * 发送短信码
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value="/sms", method= {RequestMethod.GET})
    public ResultObject sendSms(
            @RequestParam("mobile") String mobile){
        String code = new Integer(randomCode()).toString();
        logger.info("code is {}", code);
        smsCache.put(mobile, code);
        return ResultObject.ok("code",code);
    }

    /**
     * 关注问题
     *
     * @param request
     * @param questionId
     * @return
     */
    @RequestMapping(value="/follow-question", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject follow(
            HttpServletRequest request,
            @RequestParam("question_id") Integer questionId){
        int userId = (int)request.getAttribute("userId");
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
        int userId = (int)request.getAttribute("userId");
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
        int userId = (int)request.getAttribute("userId");
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
        int userId = (int)request.getAttribute("userId");
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
            return ResultObject.fail(ResultCode.USER_ACCESS_TOKEN);
        }
        SessionObject sessionObject = new SessionObject(user.getId(), code);
        String token = TokenUtil.createJwtToken(sessionObject.toJson()).get();
        return ResultObject.ok("access_token", token);
    }
}
