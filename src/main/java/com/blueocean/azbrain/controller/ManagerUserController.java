package com.blueocean.azbrain.controller;

import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager/user")
public class ManagerUserController {
    private final Logger logger = LoggerFactory.getLogger(ManagerUserController.class);

    @Autowired
    private UserService userService;

    /**
     * find user by page.
     *
     * @return
     */
    @RequestMapping(value="/list", method= {RequestMethod.GET})
    public ResultObject list(){
        Page<User> users = userService.findByPage(1, 3);
        logger.info("pages is {}", users.getTotal());
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return ResultObject.ok(pageInfo);
    }
}
