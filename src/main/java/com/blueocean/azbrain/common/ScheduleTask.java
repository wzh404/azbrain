package com.blueocean.azbrain.common;

import com.blueocean.azbrain.dao.UserMapper;
import com.blueocean.azbrain.model.ConsultationLog;
import com.blueocean.azbrain.util.WxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduleTask {
    private final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    // 结算调度
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);

    @Autowired
    private UserMapper userMapper;

    public void submit(ConsultationLog log){
        LocalDateTime time = LocalDateTime.of(log.getCdate(), log.getStartTime());
        // 提醒用户
        String kcode = userMapper.getKCode(log.getUserId());
        submit(kcode,"咨询提醒", time);

        // 提醒专家
        kcode = userMapper.getKCode(log.getByUserId());
        submit(kcode,"咨询提醒", time);
    }

    /**
     * 延后执行
     *
     * @param kcode
     */
    private void submit(final String kcode, final String content, LocalDateTime time){
        if (StringUtils.isEmpty(kcode) || StringUtils.isEmpty(content)){
            logger.warn("kcode or content is null");
            return;
        }

        if (time.isBefore(LocalDateTime.now())) {
            logger.warn("consultation time is before now");
            return;
        }
        long minutes = Duration.between(LocalDateTime.now(), time).toMinutes();
        if (minutes <= 0) {
            logger.warn("invalid schedule minutes");
            return;
        }

        String message = String.format("%s, 咨询开始时间:为%s,请提前做好准备。", content, time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")));
        if (minutes > 30) {
            logger.info("wx message to {}, after {} minutes", kcode, minutes - 30);
            submit(kcode, message, minutes - 30);
        }

        if (minutes > 5) {
            logger.info("wx message to {}, after {} minutes", kcode, minutes - 5);
            submit(kcode, message, minutes - 5);
        }
    }

    /**
     *
     * @param kcode
     * @param minutes
     */
    private void submit(final String kcode, final String content, final long minutes){
        scheduledThreadPool.schedule(
                () -> WxUtils.wxMessage(kcode, content),
                minutes, TimeUnit.MINUTES);
    }
}
