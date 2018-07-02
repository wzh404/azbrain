package com.blueocean.azbrain.controller.user;

import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.common.status.ConsultationStatus;
import com.blueocean.azbrain.model.Topic;
import com.blueocean.azbrain.model.UserScoreLog;
import com.blueocean.azbrain.model.SpecialistScoreLog;
import com.blueocean.azbrain.model.ConsultationLog;
import com.blueocean.azbrain.service.ConsultationService;
import com.blueocean.azbrain.service.TopicService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.github.pagehelper.Page;
import com.google.common.base.Preconditions;
import com.mysql.jdbc.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
public class ConsultationController {
    private static final Logger logger = LoggerFactory.getLogger(ConsultationController.class);

    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private TopicService topicService;

    /**
     * 申请咨询
     *
     * @param request
     * @param consultationLog
     * @return
     */
    @RequestMapping(value = "/apply/consultation", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject apply(HttpServletRequest request, @RequestBody ConsultationLog consultationLog){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        if (consultationLog.getWay() == null ||
            consultationLog.getCdate() == null ||
            consultationLog.getStartTime() == null ||
            consultationLog.getEndTime() == null){
            logger.error("invalid request parameters");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        if (consultationLog.getTopicId() > 0){
            Topic topic = topicService.get(consultationLog.getTopicId());
            if (topic == null){
                logger.error("invalid consultation topic id");
                return ResultObject.fail(ResultCode.BAD_REQUEST);
            }
        }

        if (consultationLog.getWay().equalsIgnoreCase("mobile") &&
                StringUtils.isNullOrEmpty(consultationLog.getMobile())){
            logger.error("invalid consultation way mobile");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        if (consultationLog.getWay().equalsIgnoreCase("400") &&
                StringUtils.isNullOrEmpty(consultationLog.getMeetingPwd())){
            logger.error("invalid consultation way 400");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        consultationLog.setUserId(userId);
        consultationLog.setWeek(consultationLog.getCdate().getDayOfWeek().getValue());
        consultationLog.setCreateTime(LocalDateTime.now());
        consultationLog.setLastUpdated(LocalDateTime.now());
        consultationLog.setStatus(ConsultationStatus.APPLIED.getCode());

        int rows = consultationService.insert(consultationLog);
        return ResultObject.cond(rows > 0, ResultCode.CONSULTATION_CREATE_FAILED);
    }

    /**
     * 取消咨询
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/cancel/consultation", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject cancel(HttpServletRequest request, @RequestParam("id") Integer id){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        ConsultationLog consultationLog = consultationService.get(id);
        if (consultationLog == null){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 咨询人员才能进行取消操作
        if (consultationLog.getUserId().intValue() != userId.intValue()){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 申请状态才能取消
        if (!consultationLog.isApplied()){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        int rows = consultationService.changeStatus(id, ConsultationStatus.CANCELED.getCode());
        return ResultObject.cond(rows > 0, ResultCode.CONSULTATION_CHANGE_STATUS_FAILED);
    }

    /**
     * 专家确认咨询
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/confirm/consultation", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject confirm(HttpServletRequest request,
                                @RequestParam("id") Integer id){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        ConsultationLog consultationLog = consultationService.get(id);
        if (consultationLog == null){
            logger.warn("consultation log not exists.");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 被咨询人员才能进行确认操作
        if (consultationLog.getByUserId().intValue() != userId){
            logger.warn("consultation by-user <> login");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 申请状态才能确认
        if (!consultationLog.getStatus().equals(ConsultationStatus.APPLIED.getCode())){
            logger.warn("consultation status is not applied");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        int rows = consultationService.confirm(id, consultationLog.getTopicId());
        return ResultObject.cond(rows > 0, ResultCode.CONSULTATION_CHANGE_STATUS_FAILED);
    }

    /**
     * 专家拒绝咨询
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/reject/consultation", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject reject(HttpServletRequest request,
                                @RequestParam("id") Integer id){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        ConsultationLog consultationLog = consultationService.get(id);
        if (consultationLog == null){
            logger.warn("consultation log not exists.");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 被咨询人员才能进行确认操作
        if (consultationLog.getByUserId().intValue() != userId){
            logger.warn("consultation by-user <> login");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 申请状态才能拒绝
        if (!consultationLog.isApplied()){
            logger.warn("consultation status is not applied");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        int rows = consultationService.changeStatus(id, ConsultationStatus.REJECTED.getCode());
        return ResultObject.cond(rows > 0, ResultCode.CONSULTATION_CHANGE_STATUS_FAILED);
    }

    /**
     * 对用户评价
     *
     * @param request
     * @param scoreLog
     * @return
     */
    @RequestMapping(value = "/score", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject userScore(HttpServletRequest request,
                                @RequestBody UserScoreLog scoreLog){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        // 咨询记录存在
        ConsultationLog consultationLog = consultationService.get(scoreLog.getLogId());
        if (consultationLog == null){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        if (consultationLog.getSpecialistCommentFlag().intValue() != 0){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 状态已确认
        if (!consultationLog.isConfirmed()){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 当前用户不是专家
        if (!consultationLog.isSpecialist(userId)){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 得分人
        scoreLog.setUserId(consultationLog.getUserId());
        scoreLog.setByUserId(0);
        scoreLog.setCreateTime(LocalDateTime.now());

        int rows = consultationService.insertUserScoreLog(scoreLog);
        return ResultObject.cond(rows > 0, ResultCode.CONSULTATION_COMMENT_FAILED);
    }

    /**
     * 对专家的评价
     *
     * @param request
     * @param scoreLog
     * @return
     */
    @RequestMapping(value = "/specialist/score", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject specialistScore(HttpServletRequest request,
                                @RequestBody SpecialistScoreLog scoreLog){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        // 咨询记录存在
        ConsultationLog consultationLog = consultationService.get(scoreLog.getLogId());
        if (consultationLog == null){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 状态已确认
        if (!consultationLog.isConfirmed()){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 当前用户不是咨询者
        if (!consultationLog.isConsultant(userId)){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 得分人
        scoreLog.setUserId(consultationLog.getByUserId());
        scoreLog.setByUserId(0);
        scoreLog.setCreateTime(LocalDateTime.now());

        int rows = consultationService.insertSpecialistScoreLog(scoreLog);
        return ResultObject.cond(rows > 0, ResultCode.CONSULTATION_COMMENT_FAILED);
    }

    /**
     * 我咨询的
     *
     * @param request
     * @param page
     * @return
     */
    @RequestMapping(value = "/my/consult", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject myConsult(HttpServletRequest request, @RequestParam("page") Integer page){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        Page<ConsultationLog> pageConsultationLog = consultationService.myConsult(page, AZBrainConstants.PAGE_SIZE, userId);
        return ResultObject.ok(pageConsultationLog.getResult());
    }

    /**
     * 咨询我的
     *
     * @param request
     * @param page
     * @return
     */
    @RequestMapping(value = "/consult/me", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject consultMe(HttpServletRequest request, @RequestParam("page") Integer page){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        Page<ConsultationLog> pageConsultationLog = consultationService.consultMe(page, AZBrainConstants.PAGE_SIZE, userId);
        return ResultObject.ok(pageConsultationLog.getResult());
    }
}
