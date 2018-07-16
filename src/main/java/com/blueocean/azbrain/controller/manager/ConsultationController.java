package com.blueocean.azbrain.controller.manager;

import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.common.status.ConsultationStatus;
import com.blueocean.azbrain.model.ConsultationLog;
import com.blueocean.azbrain.service.ConsultationService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.util.StringUtil;
import com.blueocean.azbrain.vo.ConsultationConditionVo;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController("manageConsultationController")
@RequestMapping("/manager")
public class ConsultationController {
    @Autowired
    private ConsultationService consultationService;

    /**
     * 咨询列表
     *
     * @param page
     * @param cc
     * @return
     */
    @RequestMapping(value="/list/consultation", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject listConsultation(@RequestParam("page")Integer page,
                                         @RequestBody ConsultationConditionVo cc){
        Map<String, Object> conditionMap = new HashMap<>();
        toStatus(cc.getStatus(), conditionMap);
        Page<ConsultationLog> logPage = consultationService.listConsultation(page, AZBrainConstants.MANAGER_PAGE_SIZE, conditionMap);

        //return ResultObject.ok("logs", logPage.getResult());
        return ResultObject.ok(StringUtil.pageToMap("logs", logPage));
    }

    /**
     *
     * @param logId
     * @return
     */
    @RequestMapping(value="/log/evaluation", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject getEvaluationByLog(@RequestParam("log_id")Integer logId){
        return ResultObject.ok(consultationService.getEvaluationByLog(logId));
    }
    /**
     * 根据状态查询
     *
     * @param status
     * @param conditionMap
     */
    private void toStatus(String status, Map<String, Object> conditionMap){
        if (status.equalsIgnoreCase("unconfirmed")){
            conditionMap.put("status", ConsultationStatus.UNCONFIRMED.getCode());
        } else if (status.equalsIgnoreCase("edited")){
            conditionMap.put("status", ConsultationStatus.EDITED.getCode());
        } else if (status.equalsIgnoreCase("canceled")){
            conditionMap.put("status", ConsultationStatus.CANCELED.getCode());
        } else if (status.equalsIgnoreCase("rejected")){
            conditionMap.put("status", ConsultationStatus.REJECTED.getCode());
        } else if (status.equalsIgnoreCase("uncompleted")){
            conditionMap.put("uncompleted", "ok");
        } else if (status.equalsIgnoreCase("user_unevaluated")){
            conditionMap.put("status", ConsultationStatus.CONFIRMED.getCode());
            conditionMap.put("userEvaluated", 0);
        } else if (status.equalsIgnoreCase("by_user_unevaluated")){
            conditionMap.put("status", ConsultationStatus.CONFIRMED.getCode());
            conditionMap.put("byUserEvaluated", 0);
        } else if (status.equalsIgnoreCase("completed")){
            conditionMap.put("status", ConsultationStatus.CONFIRMED.getCode());
            conditionMap.put("userEvaluated", 1);
            conditionMap.put("byUserEvaluated", 1);
        }
    }
}
