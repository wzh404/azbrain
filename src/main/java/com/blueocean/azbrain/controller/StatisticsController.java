package com.blueocean.azbrain.controller;

import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(value="/manager/stat/question", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject statQuestion(){
        Map<String, Object> resultMap = statisticsService.countQuestion();
        return ResultObject.ok(resultMap);
    }
}
