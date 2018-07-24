package com.blueocean.azbrain.controller.manager;

import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(value="/stat", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject stat(){
        return ResultObject.ok(statisticsService.stat());
    }
}
