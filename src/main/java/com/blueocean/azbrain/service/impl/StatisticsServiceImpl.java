package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.dao.StatisticsMapper;
import com.blueocean.azbrain.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private StatisticsMapper statisticsMapper;

    // TODO 优化定时任务处理
    @Override
    public Map<String, Object> countQuestion() {
        HashMap<String, Object> resultMap = new HashMap<>();

        Integer cnt = statisticsMapper.totalQuestion(all());
        Integer todayCnt = statisticsMapper.totalQuestion(today());
        Integer yesterdayCnt = statisticsMapper.totalQuestion(yesterday());
        resultMap.put("question_all", cnt == null ? 0 : cnt);
        resultMap.put("question_yesterday", yesterdayCnt == null ? 0 : yesterdayCnt);
        resultMap.put("question_today", todayCnt == null ? 0 : todayCnt);

        Integer cnt1 = statisticsMapper.totalAnswer(all());
        Integer todayCnt1 = statisticsMapper.totalAnswer(today());
        Integer yesterdayCnt1 = statisticsMapper.totalAnswer(yesterday());
        resultMap.put("answer_all", cnt1 == null ? 0 : cnt1);
        resultMap.put("answer_yesterday", yesterdayCnt1 == null ? 0 : yesterdayCnt1);
        resultMap.put("answer_today", todayCnt1 == null ? 0 : todayCnt1);

        Integer cnt2 = statisticsMapper.totalLike(all());
        Integer todayCnt2 = statisticsMapper.totalLike(today());
        Integer yesterdayCnt2 = statisticsMapper.totalLike(yesterday());
        resultMap.put("like_all", cnt2 == null ? 0 : cnt2);
        resultMap.put("like_yesterday", yesterdayCnt2 == null ? 0 : yesterdayCnt2);
        resultMap.put("like_today", todayCnt2 == null ? 0 : todayCnt2);

        return resultMap;
    }

    private Map<String, Object> all() {
        return new HashMap<>();
    }

    private Map<String, Object> today() {
        LocalDate ld = LocalDate.now();
        return dayMap(ld);
    }

    private Map<String, Object> yesterday() {
        LocalDate ld = LocalDate.now().minusDays(1);
        return dayMap(ld);
    }

    private Map<String, Object> dayMap(LocalDate ld) {
        HashMap<String, Object> conditionMap = new HashMap<>();
        LocalDateTime st = LocalDateTime.of(ld, LocalTime.MIN);
        LocalDateTime et = LocalDateTime.of(ld, LocalTime.MAX);
        conditionMap.put("startTime", st);
        conditionMap.put("endTime", et);

        return conditionMap;
    }
}
