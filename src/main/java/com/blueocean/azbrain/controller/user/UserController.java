package com.blueocean.azbrain.controller.user;

import com.blueocean.azbrain.common.MeetingRunner;
import com.blueocean.azbrain.common.SessionObject;
import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.*;
import com.blueocean.azbrain.service.ArticleService;
import com.blueocean.azbrain.service.DictService;
import com.blueocean.azbrain.service.UserService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.util.StringUtil;
import com.blueocean.azbrain.util.TokenUtil;
import com.blueocean.azbrain.util.WxUtils;
import com.blueocean.azbrain.vo.SpecialistEditVo;
import com.blueocean.azbrain.vo.SpecialistVo;
import com.github.pagehelper.Page;
import com.google.common.base.Preconditions;
import jdk.nashorn.internal.runtime.options.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * Created by @author wangzunhui on 2018/3/12.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private DictService dictService;
    /**
     * 获取access_token.
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/apply/access-token", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject accessToken(@RequestParam("code") String code) {
        String kcode = null;
        try {
            kcode = new String(Base64.getDecoder().decode(code), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }
        logger.info("{} kcode is {}", code, kcode);

        User user = userService.getUserByKCode(kcode);
        if (user == null) {
            final EventLog event = new EventLog();
            event.setWho(kcode);
            event.setLevel(5);
            event.setType("0100");
            event.setContent("Login failed");
            event.setEventId(0);
            event.setDuration(0);
            event.setCreateTime(LocalDateTime.now());
            MeetingRunner.execute(()->{
                dictService.insertEvent(event);
            });

            logger.warn("The user does not exist ");
            return ResultObject.fail(ResultCode.USER_ACCESS_TOKEN);
        }
        SessionObject sessionObject = new SessionObject(user.getId(), code);
        String token = TokenUtil.createJwtToken(sessionObject.toJson()).orElse(AZBrainConstants.EMPTY_STRING);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("access_token", token);
        resultMap.put("user_type", user.getUserType());
        resultMap.put("name", user.getName());
        resultMap.put("mobile", user.getMobile());
        resultMap.put("login_flag", user.getLoginFlag());
        resultMap.put("message_flag", user.getMessageFlag());
        if (user.getLoginFlag().intValue() == 1){
            userService.updateLogin(user.getId());
        }

        final EventLog event = new EventLog();
        event.setWho(user.getName());
        event.setLevel(0);
        event.setType("0100");
        event.setContent("Login OK");
        event.setEventId(0);
        event.setDuration(0);
        event.setCreateTime(LocalDateTime.now());
        MeetingRunner.execute(()->{
            dictService.insertEvent(event);
        });
        return ResultObject.ok(resultMap);
    }

    /**
     * 查找专家
     *
     * @param request
     * @param specialistVo
     * @return
     */
    @RequestMapping(value="/search/specialist", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject searchSpecialist(HttpServletRequest request,
                                         @RequestParam("page") Integer page,
                                         @RequestBody SpecialistVo specialistVo){
        Page<User> pageMap = userService.searchSpecialist(page, AZBrainConstants.PAGE_SIZE, specialistVo);
        StringUtil.notUserRealName(pageMap.getResult());
        return ResultObject.ok("specialist", pageMap.getResult());
    }

    /**
     * 用户详情
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/profile", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject profile(HttpServletRequest request,
                                @RequestParam(value="user_id", required = false) Integer userId,
                                @RequestParam(value="useRealName", required = false)Integer useRealName){
        if (userId == null) {
            userId = (Integer) request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
            Preconditions.checkArgument(userId != null, AZBrainConstants.PLEASE_LOG_IN);
        }

        Map<String, Object> map = userService.profile(userId, useRealName==null ? 0 : 1);
        return ResultObject.ok(map);
    }

    /**
     * 详情
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/specialist/profile", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject specialistProfile(HttpServletRequest request,
                                          @RequestParam(value="user_id", required = false) Integer userId,
                                          @RequestParam(value="useRealName", required = false)Integer useRealName){
        if (userId == null) {
            userId = (Integer) request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
            Preconditions.checkArgument(userId != null, AZBrainConstants.PLEASE_LOG_IN);
        }

        Map<String, Object> map = userService.specialistProfile(userId, useRealName==null ? 0 : 1);
        return ResultObject.ok(map);
    }

    /**
     * 专家文章列表分页
     *
     * @param request
     * @param page
     * @return
     */
    @RequestMapping(value="/specialist/articles", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject articles(HttpServletRequest request, @RequestParam("page") Integer page,
                                 @RequestParam(value="user_id", required = false) Integer userId) {
        if (userId == null) {
            userId = (Integer) request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
            Preconditions.checkArgument(userId != null, AZBrainConstants.PLEASE_LOG_IN);
        }

        Page<Article> articlePage = articleService.specialistArticles(page, AZBrainConstants.PAGE_SIZE, userId);
        return ResultObject.ok("articles", articlePage.getResult());
    }


    /**
     * 可咨询条件
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/consultation/conditions", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject conditions(HttpServletRequest request, @RequestParam(value="user_id", required = false) Integer userId){
        if (userId == null) {
            userId = (Integer) request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
            Preconditions.checkArgument(userId != null, AZBrainConstants.PLEASE_LOG_IN);
        }

        Map<String, Object> map = userService.consultationConditions(userId);
        return ResultObject.ok(map);
    }

    /**
     * 用户积分列表
     *
     * @param request
     * @param page
     * @return
     */
    @RequestMapping(value="/points", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject listUserPoints(HttpServletRequest request, @RequestParam("page") Integer page){
        Integer userId = (Integer) request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, AZBrainConstants.PLEASE_LOG_IN);

        Integer total = userService.myPoint(userId);
        Page<UserPoints> mapPage = userService.listUserPoints(page,AZBrainConstants.PAGE_SIZE, userId);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("points",mapPage.getResult());
        return ResultObject.ok(map);
    }

    /**
     * 主题专家列表
     *
     * @param topicId
     * @param page
     * @return
     */
    @RequestMapping(value="/topic/specialists", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject topicSpecialists(@RequestParam("topic_id") Integer topicId,
                                         @RequestParam("page") Integer page){
        Page<User> userPage = userService.topicSpecialists(page, AZBrainConstants.PAGE_SIZE, topicId);
        return ResultObject.ok(userPage.getResult());
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
        Preconditions.checkArgument(userId != null, "please log in");

        User user = userService.get(userId);
        UserFeedback userFeedback = new UserFeedback(userId, user.getName(), feedback);
        userFeedback.setPhoto(photo);
        userFeedback.setClassification(classification);

        int rows = userService.insertUserFeedback(userFeedback);
        return ResultObject.cond(rows > 0, ResultCode.USER_ILLEGAL_STATUS);
    }

    /**
     * 获取专家标签
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/specialist/labels", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject specialistLabels(HttpServletRequest request){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");
        return ResultObject.ok(userService.getSpecialistLabels(userId));
    }

    /**
     * 修改用户为非首次登陆
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/specialist/logged", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject specialistLogged(HttpServletRequest request){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        userService.updateLogin(userId);
        return ResultObject.ok();
    }

    /**
     * 专家修改标签
     *
     * @param request
     * @param vo
     * @return
     */
    @RequestMapping(value="/specialist/change/labels", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject specialistChangeLabels(HttpServletRequest request, @RequestBody SpecialistEditVo vo){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        vo.setUserId(userId);
        userService.changeSpecialistLabel(vo);
        return ResultObject.ok();
    }

    /**
     * 取消通知
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/cancel/notify", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject cancelNotify(HttpServletRequest request){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        userService.cancelNotify(userId);
        return ResultObject.ok();
    }
}
