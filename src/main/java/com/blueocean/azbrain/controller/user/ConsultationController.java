package com.blueocean.azbrain.controller.user;

import com.blueocean.azbrain.common.Meeting;
import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.common.status.ConsultationStatus;
import com.blueocean.azbrain.model.Topic;
import com.blueocean.azbrain.model.ConsultationLog;
import com.blueocean.azbrain.service.ConsultationService;
import com.blueocean.azbrain.service.TopicService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.util.MeetingUtil;
import com.blueocean.azbrain.vo.ConsultationLogVo;
import com.blueocean.azbrain.vo.UserEvaluateVo;
import com.github.pagehelper.Page;
import com.google.common.base.Preconditions;
import com.mysql.jdbc.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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
                consultationLog.getByUserId() == null ||
            consultationLog.getCode() == null){
            logger.error("invalid request parameters");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 不能自己咨询自己
        if (userId.intValue() == consultationLog.getByUserId().intValue()){
            logger.error("invalid by user id");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        if (consultationLog.getTopicId() == null){
            consultationLog.setTopicId(0);
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

        /* 400咨询检查会议密码
        if (consultationLog.getWay().equalsIgnoreCase("400") &&
                StringUtils.isNullOrEmpty(consultationLog.getMeetingPwd()) &&
                StringUtils.isNullOrEmpty(consultationLog.getMeetingHost())){
            logger.error("invalid consultation way 400");
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }*/

        int duration = Integer.parseInt(consultationLog.getCode());
        consultationLog.setEndTime(consultationLog.getStartTime().plus(duration, ChronoUnit.MINUTES));

        // 400咨询检查会议密码
        if (consultationLog.getWay().equalsIgnoreCase("400") && !MeetingUtil.get(consultationLog)) {
            return ResultObject.fail(ResultCode.MEETING_HOST_PWD_FAILED);
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
        if (!consultationLog.user(userId)){
            logger.warn("must be user id {}", userId);
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 待确认或者编辑状态才能取消
        if (!(consultationLog.unconfirmed() || consultationLog.edited())){
            logger.warn("must be status unconfirmed or edited ");
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

        // 设置咨询的会议主持人及会议密码
        if (consultationLog.getWay().equalsIgnoreCase("400") &&
                !setMeetingHostAndPwd(consultationLog)) {
            return ResultObject.fail(ResultCode.MEETING_HOST_PWD_FAILED);
        }

        int rows = consultationService.confirm(consultationLog);
        return ResultObject.cond(rows > 0, ResultCode.CONSULTATION_CHANGE_STATUS_FAILED);
    }

    /**
     * 确认电话会议咨询是否可用。
     *
     * @param consultationLog
     * @return
     */
    private boolean setMeetingHostAndPwd(ConsultationLog consultationLog){
        LocalDateTime s = LocalDateTime.of(consultationLog.getCdate(), consultationLog.getStartTime());
        LocalDateTime e = LocalDateTime.of(consultationLog.getCdate(), consultationLog.getEndTime());
        if (!MeetingUtil.set(consultationLog.getMeetingHost(), consultationLog.getMeetingPwd(), s, e)) {
            logger.warn("Set consultation meeting host & password failed.");
            return false;
        }

        return true;
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

        LocalTime e = consultationLogVo.getStartTime().plusMinutes(consultationLogVo.getDuration());
        consultationLogVo.setEndTime(e);

        // 400咨询检查会议密码
        if (consultationLog.getWay().equalsIgnoreCase("400") && !MeetingUtil.get(consultationLogVo)) {
            return ResultObject.fail(ResultCode.MEETING_HOST_PWD_FAILED);
        }

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

        // 400咨询检查会议密码
        if (consultationLog.getWay().equalsIgnoreCase("400") && !MeetingUtil.get(consultationLogVo)) {
            return ResultObject.fail(ResultCode.MEETING_HOST_PWD_FAILED);
        }

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

        // 设置咨询的会议主持人及会议密码
        if (consultationLog.getWay().equalsIgnoreCase("400") &&
                !setMeetingHostAndPwd(consultationLog)) {
            return ResultObject.fail(ResultCode.MEETING_HOST_PWD_FAILED);
        }

        // 变为确认状态
        int rows = consultationService.confirm(consultationLog);
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

        // 状态已确认
        if (!consultationLog.confirmed()){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        // 当前用户是被咨询人
        if (consultationLog.byUser(userId)){
            // 已评论
            if (consultationLog.byUserEvaluated()){
                logger.warn("by user evaluated.");
                return ResultObject.fail(ResultCode.BAD_REQUEST);
            }

            userEvaluateVo.setUserId(userId);
            userEvaluateVo.setByUserId(consultationLog.getUserId());
            // 被评论人为发起人
            userEvaluateVo.setByUserFlag(0);
        }
        // 当前用户是发起人
        else if (consultationLog.user(userId)){
            // 已评论
            if (consultationLog.userEvaluated()){
                logger.warn("user evaluated.");
                return ResultObject.fail(ResultCode.BAD_REQUEST);
            }

            userEvaluateVo.setUserId(userId);
            userEvaluateVo.setByUserId(consultationLog.getByUserId());
            // 被评论人为专家
            userEvaluateVo.setByUserFlag(1);
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

        Page<ConsultationLog> pageConsultationLog = consultationService.myConsult(page, 100, userId);
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

        Page<ConsultationLog> pageConsultationLog = consultationService.consultMe(page, 100, userId);
        return ResultObject.ok(pageConsultationLog.getResult());
    }

    /**
     * 咨询详情
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/view/consultation", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultObject viewConsultation(HttpServletRequest request, @RequestParam("log_id") Integer id){
        ConsultationLog log = consultationService.get(id);
        if (log == null){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }
        return ResultObject.ok(log);
    }
}
