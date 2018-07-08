package com.blueocean.azbrain.controller.manager;

import com.blueocean.azbrain.common.ManagerSessionObject;
import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.common.status.UserStatus;
import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.service.UserManagerService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.util.CryptoUtil;
import com.blueocean.azbrain.vo.LoginVo;
import com.blueocean.azbrain.vo.SpecialistEditVo;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("manageUserController")
@RequestMapping("/manager")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserManagerService userService;

    /**
     * 运营端登录
     *
     * @param request
     * @return
     */
    @RequestMapping(value="/login", method= {RequestMethod.POST})
    public ResultObject login(HttpServletRequest request,
                              @Valid @RequestBody LoginVo loginVo){
        User user = userService.getUserByName(loginVo.getName());
        if (user == null){
            logger.warn("user {} not exsits", loginVo.getName());
            return ResultObject.fail(ResultCode.USER_LOGIN_FAILED);
        }

        String signedPwd = CryptoUtil.signature(loginVo.getPwd());
        logger.info("[{}]", signedPwd);
        if (!user.getPassword().equalsIgnoreCase(signedPwd)){
            logger.warn("{} incorrect password", loginVo.getName());
            return ResultObject.fail(ResultCode.USER_LOGIN_FAILED);
        }

        ManagerSessionObject.toSession(request.getSession(true), user);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", user.getId());
        resultMap.put("name", user.getName());

        return ResultObject.ok("user", resultMap);
    }

    /**
     * 编辑专家
     *
     * @param vo
     * @return
     */
    @RequestMapping(value="/edit/specialist", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject editSpecialist(@RequestBody SpecialistEditVo vo){
        int rows = userService.editSpecialist(vo);
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }

    /**
     * 根据手机号或名称搜索专家
     *
     * @param key
     * @return
     */
    @RequestMapping(value="/search/specialist", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject findSpecialist(@RequestParam("key") String key){
        List<User> list = userService.findSpecialistByKey(key);
        return ResultObject.ok("specialists", list);
    }

    /**
     * 用户列表
     *
     * @param page
     * @param name
     * @param startTime
     * @param endTime
     * @param keycode
     * @return
     */
    @RequestMapping(value="/users", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject listUser(@RequestParam("page") Integer page,
                                 @RequestParam(value="name", required = false)String name,
                                 @RequestParam(value="startTime", required = false)LocalDateTime startTime,
                                 @RequestParam(value="endTime", required = false)LocalDateTime endTime,
                                 @RequestParam(value="keycode", required = false)String keycode){
        Map<String, Object> conditionMap = new HashMap<>();
        if (name != null) conditionMap.put("name", name);
        if (startTime != null) conditionMap.put("startTime", startTime);
        if (endTime != null) conditionMap.put("startTime", endTime);
        if (keycode != null) conditionMap.put("keycode", keycode);

        Page<User> userPage = userService.findByPage(page, AZBrainConstants.MANAGER_PAGE_SIZE, conditionMap);
        return ResultObject.ok(userPage.getResult());
    }

    /**
     * 专家列表
     *
     * @param page
     * @param name
     * @param startTime
     * @param endTime
     * @param keycode
     * @return
     */
    @RequestMapping(value="/specialists", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject listSpecialist(@RequestParam("page") Integer page,
                                 @RequestParam(value="name", required = false)String name,
                                 @RequestParam(value="startTime", required = false)LocalDateTime startTime,
                                 @RequestParam(value="endTime", required = false)LocalDateTime endTime,
                                 @RequestParam(value="keycode", required = false)String keycode){
        Map<String, Object> conditionMap = new HashMap<>();
        if (name != null) conditionMap.put("name", name);
        if (startTime != null) conditionMap.put("startTime", startTime);
        if (endTime != null) conditionMap.put("startTime", endTime);
        if (keycode != null) conditionMap.put("keycode", keycode);

        Page<User> userPage = userService.listSpecialist(page, AZBrainConstants.MANAGER_PAGE_SIZE, conditionMap);
        return ResultObject.ok(userPage.getResult());
    }

    /**
     * 查看用户
     *
     * @param userId
     * @return
     */
    @RequestMapping(value="/view/user", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject viewUser(@RequestParam("user_id") Integer userId){
        User user = userService.viewUser(userId);
        if (user == null){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        return ResultObject.ok("user", user);
    }

    /**
     * 查看用户
     *
     * @param userId
     * @return
     */
    @RequestMapping(value="/view/specialist", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject viewSpecialist(@RequestParam("user_id") Integer userId){
        return ResultObject.ok(userService.viewSpecialist(userId));
    }

    /**
     * 停用用户
     *
     * @param userId
     * @return
     */
    @RequestMapping(value="/user/disable", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject disable(@RequestParam("user_id") Integer userId){
        int rows = userService.changeStatus(userId, UserStatus.DISABLED.getCode());
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }

    /**
     * 启用用户
     *
     * @param userId
     * @return
     */
    @RequestMapping(value="/user/enable", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject enable(@RequestParam("user_id") Integer userId){
        int rows = userService.changeStatus(userId, UserStatus.NORMAL.getCode());
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @RequestMapping(value="/user/delete", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject delete(@RequestParam("user_id") Integer userId){
        int rows = userService.changeStatus(userId, UserStatus.DELETED.getCode());
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }

    /**
     * 根据标签查找专家
     *
     * @param page
     * @param labels
     * @return
     */
    @RequestMapping(value="/search/label/specialist", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject findSpecialistByLabel(@RequestParam("page") Integer page,
                                 @RequestParam("labels[]") List<String> labels){
        Page<User> userPage = userService.findSpecialistByLabel(page, AZBrainConstants.MANAGER_PAGE_SIZE, labels);
        return ResultObject.ok("specialists", userPage.getResult());
    }
}
