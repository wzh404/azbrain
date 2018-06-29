package com.blueocean.azbrain.controller.user;

import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.ConsultationLog;
import com.blueocean.azbrain.service.ConsultationService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
public class ConsultationController {
    private static final Logger logger = LoggerFactory.getLogger(ConsultationController.class);

    @Autowired
    private ConsultationService consultationService;

    @RequestMapping(value = "/apply/consultation", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject newLog(HttpServletRequest request, @RequestBody ConsultationLog consultationLog){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        consultationLog.setUserId(userId);
        consultationLog.setCreateTime(LocalDateTime.now());
        consultationLog.setStatus("00");
        int res = consultationService.insert(consultationLog);
        return res > 0 ? ResultObject.ok() : ResultObject.fail(ResultCode.CONSULTATION_CREATE_FAILED);
    }
}
