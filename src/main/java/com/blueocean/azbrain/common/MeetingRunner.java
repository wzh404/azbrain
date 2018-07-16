package com.blueocean.azbrain.common;

import com.blueocean.azbrain.dao.ConsultationLogMapper;
import com.blueocean.azbrain.model.ConsultationLog;
import com.blueocean.azbrain.util.MeetingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MeetingRunner implements ApplicationRunner, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(MeetingRunner.class);

    @Autowired
    private ConsultationLogMapper logMapper;

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
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
