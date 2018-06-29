package com.blueocean.azbrain.controller.user;

import com.blueocean.azbrain.common.SessionObject;
import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.User;
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
    public ResultObject searchSpecialist(HttpServletRequest request, @RequestBody SpecialistVo specialistVo){
        //Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        //Preconditions.checkArgument(userId != null, "please log in");

        Page<User> pageMap = userService.searchSpecialist(specialistVo.getPage(), AZBrainConstants.PAGE_SIZE, specialistVo);
        return ResultObject.ok("specialist", pageMap.getResult());
    }
}
