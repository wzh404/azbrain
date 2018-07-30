package com.blueocean.azbrain.common;

import com.blueocean.azbrain.dao.ConsultationLogMapper;
import com.blueocean.azbrain.dao.UserMapper;
import com.blueocean.azbrain.model.ConsultationLog;
import com.blueocean.azbrain.util.MeetingUtil;
import com.blueocean.azbrain.util.WxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class MeetingRunner implements ApplicationRunner, Ordered, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(MeetingRunner.class);

    @Autowired
    private ConsultationLogMapper logMapper;

    private static ThreadPoolExecutor executor;

    @Override
    public void run(ApplicationArguments args) {
        List<Map<String, String>> list = logMapper.listMeeting();
        logger.info(String.format("loading meeting host and password %d rows", list.size()));
        MeetingUtil.init(list);

        List<ConsultationLog> logs = logMapper.selectHostAndPwd();
        if (logs != null && !logs.isEmpty()) {
            logger.info(String.format("merge meeting host and password %d rows", logs.size()));
            MeetingUtil.merge(logs);
        }

        executor = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(150));
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public void destroy() throws Exception {
        executor.shutdownNow();
    }

    public static void wxNotify(final String  kcode, final String content){
        if (StringUtils.isEmpty(kcode) || StringUtils.isEmpty(content)){
            logger.warn("invalid weixin message notify.");
            return;
        }

        logger.info("send message '{}' to [{}]", content, kcode);
        executor.execute(() -> WxUtils.wxMessage(kcode, content));
    }
}
