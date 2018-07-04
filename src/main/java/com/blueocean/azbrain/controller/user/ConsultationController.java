package com.blueocean.azbrain.controller.user;

import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.common.status.ConsultationStatus;
import com.blueocean.azbrain.model.Topic;
import com.blueocean.azbrain.model.ConsultationLog;
import com.blueocean.azbrain.service.ConsultationService;
import com.blueocean.azbrain.service.TopicService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.vo.ConsultationLogVo;
import com.blueocean.azbrain.vo.UserEvaluateVo;
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
     * 用户咨询
     *
     * @param request
     * @param consultationLog
     * @return
     */
    @RequestMapping(value = "/apply/consultation", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject apply(HttpServletRequest request, @RequestBody ConsultationLog consultationLog){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, AZBrainConstants.PLEASE_LOG_IN);

        // 参数检查
        if (consultationLog.getWay() == null ||
            consultationLog.getCdate() == null ||
            consultationLog.getStartTime() == null ||
            consultationLog.getEndTime() == null){
            logger.error("invalid request parameters");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 检查主题是否存在
        if (consultationLog.getTopicId() > 0){
            Topic topic = topicService.get(consultationLog.getTopicId());
            if (topic == null){
                logger.error("invalid consultation topic id");
                return ResultObject.fail(ResultCode.BAD_REQUEST);
            }
        }

        // 手机咨询检查手机号
        if (consultationLog.getWay().equalsIgnoreCase("mobile") &&
                StringUtils.isNullOrEmpty(consultationLog.getMobile())){
            logger.error("invalid consultation way mobile");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 400咨询检查会议密码
        if (consultationLog.getWay().equalsIgnoreCase("400") &&
                StringUtils.isNullOrEmpty(consultationLog.getMeetingPwd())){
            logger.error("invalid consultation way 400");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        consultationLog.setUserId(userId);
        consultationLog.setWeek(consultationLog.getCdate().getDayOfWeek().getValue());
        consultationLog.setCreateTime(LocalDateTime.now());
        consultationLog.setLastUpdated(LocalDateTime.now());
        consultationLog.setStatus(ConsultationStatus.UNCONFIRMED.getCode());

        int rows = consultationService.insert(consultationLog);
        return ResultObject.cond(rows > 0, ResultCode.CONSULTATION_CREATE_FAILED);
    }

    /**
     * 用户取消咨询
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/cancel/consultation", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject cancel(HttpServletRequest request, @RequestParam("id") Integer id){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, AZBrainConstants.PLEASE_LOG_IN);

        ConsultationLog consultationLog = consultationService.get(id);
        if (consultationLog == null){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 咨询时间已过期
        if (consultationLog.expired()){
            logger.warn("consultation expired");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 用户才能进行取消操作
        if (consultationLog.user(userId)){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 待确认或者编辑状态才能取消
        if (!(consultationLog.unconfirmed() || consultationLog.edited())){
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
    @RequestMapping(value = "/specialist/confirm/consultation", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject confirm(HttpServletRequest request,
                                @RequestParam("id") Integer id){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, AZBrainConstants.PLEASE_LOG_IN);

        ConsultationLog consultationLog = consultationService.get(id);
        if (consultationLog == null){
            logger.warn("consultation log not exists.");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 咨询时间已过期
        if (consultationLog.expired()){
            logger.warn("consultation expired");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 被咨询人员才能进行确认操作
        if (consultationLog.getByUserId().intValue() != userId){
            logger.warn("consultation by-user <> login");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 申请状态才能确认
        if (!consultationLog.unconfirmed()){
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
    @RequestMapping(value = "/specialist/reject/consultation", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject reject(HttpServletRequest request,
                                @RequestParam("id") Integer id){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, AZBrainConstants.PLEASE_LOG_IN);

        ConsultationLog consultationLog = consultationService.get(id);
        if (consultationLog == null){
            logger.warn("consultation log not exists.");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 咨询时间已过期
        if (consultationLog.expired()){
            logger.warn("consultation expired");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 被咨询人员(专家)才能进行确认操作
        if (!consultationLog.byUser(userId)){
            logger.warn("consultation by-user <> login");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 待确认状态才能拒绝
        if (!consultationLog.unconfirmed()){
            logger.warn("consultation status is not applied");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        int rows = consultationService.changeStatus(id, ConsultationStatus.REJECTED.getCode());
        return ResultObject.cond(rows > 0, ResultCode.CONSULTATION_CHANGE_STATUS_FAILED);
    }

    /**
     * 专家编辑
     *
     * @param request
     * @param consultationLogVo
     * @return
     */
    @RequestMapping(value = "/specialist/edit/consultation", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject edit(HttpServletRequest request, @RequestBody ConsultationLogVo consultationLogVo) {
        Integer userId = (Integer) request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, AZBrainConstants.PLEASE_LOG_IN);

        Integer id = consultationLogVo.getId();
        ConsultationLog consultationLog = consultationService.get(id);
        if (consultationLog == null) {
            logger.warn("consultation log not exists.");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 咨询时间已过期
        if (consultationLog.expired()){
            logger.warn("consultation expired");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 原来的周保持一致
        if (!consultationLog.checkWeek(consultationLogVo.getCdate())){
            logger.error("invalid edit cdate");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 是否专家（被咨询人）
        if (!consultationLog.byUser(userId)){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 待确认状态专家才能编辑
        if (!consultationLog.unconfirmed()){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        consultationLogVo.setStatus(ConsultationStatus.EDITED.getCode());
        consultationLogVo.setLastUpdated(LocalDateTime.now());
        // 变为已编辑状态
        int rows = consultationService.edit(consultationLogVo);
        return ResultObject.cond(rows > 0, ResultCode.CONSULTATION_CHANGE_STATUS_FAILED);
    }

    /**
     * 用户编辑
     *
     * @param request
     * @param consultationLogVo
     * @return
     */
    @RequestMapping(value = "/edit/consultation", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject userEdit(HttpServletRequest request,
                                 @RequestBody ConsultationLogVo consultationLogVo) {
        Integer userId = (Integer) request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, AZBrainConstants.PLEASE_LOG_IN);

        Integer id = consultationLogVo.getId();
        ConsultationLog consultationLog = consultationService.get(id);
        if (consultationLog == null) {
            logger.warn("consultation log not exists.");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 咨询时间已过期
        if (consultationLog.expired()){
            logger.warn("consultation expired");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 是否用户
        if (!consultationLog.user(userId)){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 编辑状态用户才能编辑
        if (!consultationLog.edited()){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        consultationLogVo.setStatus(ConsultationStatus.UNCONFIRMED.getCode());
        consultationLogVo.setLastUpdated(LocalDateTime.now());

        // 变为已编辑状态
        int rows = consultationService.edit(consultationLogVo);
        return ResultObject.cond(rows > 0, ResultCode.CONSULTATION_CHANGE_STATUS_FAILED);
    }

    /**
     * 用户确认
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/confirm/consultation", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject userConfirm(HttpServletRequest request,
                                 @RequestParam("id") Integer id) {
        Integer userId = (Integer) request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, AZBrainConstants.PLEASE_LOG_IN);

        ConsultationLog consultationLog = consultationService.get(id);
        if (consultationLog == null) {
            logger.warn("consultation log not exists.");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 咨询时间已过期
        if (consultationLog.expired()){
            logger.warn("consultation expired");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 是否用户
        if (!consultationLog.user(userId)){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 编辑状态用户才能确认
        if (!consultationLog.edited()){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 变为确认状态
        int rows = consultationService.confirm(id, consultationLog.getTopicId());
        return ResultObject.cond(rows > 0, ResultCode.CONSULTATION_CHANGE_STATUS_FAILED);
    }

    /**
     * 专家对用户评价
     *
     * @param request
     * @param userEvaluateVo
     * @return
     */
    @RequestMapping(value = "/score", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject toUser(HttpServletRequest request,
                                @RequestBody UserEvaluateVo userEvaluateVo){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, AZBrainConstants.PLEASE_LOG_IN);

        // 咨询记录存在
        ConsultationLog consultationLog = consultationService.get(userEvaluateVo.getLogId());
        if (consultationLog == null){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 咨询时间未结束
        if (!consultationLog.expired()){
            logger.warn("consultation not due");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 已评论
        if (consultationLog.byUserEvaluated()){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 状态已确认
        if (!consultationLog.confirmed()){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 当前用户是被咨询人
        if (consultationLog.byUser(userId)){
            userEvaluateVo.setUserId(consultationLog.getUserId());
            userEvaluateVo.setByUserId(userId);
            userEvaluateVo.setFlag(true);
        }
        // 当前用户是咨询人
        else if (consultationLog.user(userId)){
            userEvaluateVo.setUserId(userId);
            userEvaluateVo.setByUserId(consultationLog.getByUserId());
            userEvaluateVo.setFlag(false);
        }
        else {
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        int rows = consultationService.insertUserEvaluate(userEvaluateVo);
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
        Preconditions.checkArgument(userId != null, AZBrainConstants.PLEASE_LOG_IN);

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
        Preconditions.checkArgument(userId != null, AZBrainConstants.PLEASE_LOG_IN);

        Page<ConsultationLog> pageConsultationLog = consultationService.consultMe(page, AZBrainConstants.PAGE_SIZE, userId);
        return ResultObject.ok(pageConsultationLog.getResult());
    }
}
