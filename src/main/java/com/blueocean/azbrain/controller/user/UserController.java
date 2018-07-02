package com.blueocean.azbrain.controller.user;

import com.blueocean.azbrain.common.SessionObject;
import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.model.UserPoints;
import com.blueocean.azbrain.service.ArticleService;
import com.blueocean.azbrain.service.UserService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.util.TokenUtil;
import com.blueocean.azbrain.vo.SpecialistVo;
import com.github.pagehelper.Page;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


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

    /**
     * 获取access_token.
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/apply/access-token", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject accessToken(@RequestParam("code") String code) {
        User user = userService.getUserByName(code);
        if (user == null) {
            logger.warn("The user does not exist ");
            return ResultObject.fail(ResultCode.USER_ACCESS_TOKEN);
        }
        SessionObject sessionObject = new SessionObject(user.getId(), code);
        String token = TokenUtil.createJwtToken(sessionObject.toJson()).orElse(AZBrainConstants.EMPTY_STRING);
        return ResultObject.ok("access_token", token);
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
        return ResultObject.ok("specialist", pageMap.getResult());
    }

    /**
     * 用户详情
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/profile", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject profile(HttpServletRequest request){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        Map<String, Object> map = userService.profile(userId);
        return ResultObject.ok(map);
    }

    /**
     * 用户详情
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/specialist/profile", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject specialistProfile(HttpServletRequest request){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        Map<String, Object> map = userService.specialistProfile(userId);
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
    public ResultObject articles(HttpServletRequest request, @RequestParam("page") Integer page) {
        Integer userId = (Integer) request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

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
    public ResultObject conditions(HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

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
        Preconditions.checkArgument(userId != null, "please log in");

        Page<UserPoints> mapPage = userService.listUserPoints(page,AZBrainConstants.PAGE_SIZE, userId);
        return ResultObject.ok(mapPage.getResult());
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
}
