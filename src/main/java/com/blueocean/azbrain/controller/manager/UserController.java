package com.blueocean.azbrain.controller.manager;

import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.service.UserService;
import com.blueocean.azbrain.vo.SpecialistEditVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("manageUserController")
@RequestMapping("/manager")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value="/edit/specialist", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject editSpecialist(@RequestBody SpecialistEditVo vo){
        userService.editSpecialist(vo);
        return ResultObject.ok();
    }
}
