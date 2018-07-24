package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.dao.StatisticsMapper;
import com.blueocean.azbrain.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private StatisticsMapper statisticsMapper;

    private static ConcurrentHashMap<String, Integer> cacheMap = new ConcurrentHashMap<>(32);

    @Override
    public Map<String, Object> stat() {
        Map<String, Object> map = new HashMap<>();

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        Integer articleCnt = statisticsMapper.statArticle(LocalDateTime.of(today, LocalTime.of(0,0,0)),
                LocalDateTime.of(today, LocalTime.of(23, 59, 59)));
        map.put("articleCnt", articleCnt);

        Integer topicCnt = statisticsMapper.statTopic(LocalDateTime.of(today, LocalTime.of(0,0,0)),
                LocalDateTime.of(today, LocalTime.of(23, 59, 59)));
        map.put("topicCnt", topicCnt);

        Integer consultationCnt = statisticsMapper.statConsultation(LocalDateTime.of(today, LocalTime.of(0,0,0)),
                LocalDateTime.of(today, LocalTime.of(23, 59, 59)));
        map.put("consultationCnt", consultationCnt);

        Integer likesCnt = statisticsMapper.statConsultation(LocalDateTime.of(today, LocalTime.of(0,0,0)),
                LocalDateTime.of(today, LocalTime.of(23, 59, 59)));
        map.put("likesCnt", likesCnt);

        Integer followsCnt = statisticsMapper.statConsultation(LocalDateTime.of(today, LocalTime.of(0,0,0)),
                LocalDateTime.of(today, LocalTime.of(23, 59, 59)));
        map.put("followsCnt", followsCnt);

        // Article
        String key = "articleYesterdayCnt";
        if (!cacheMap.contains(key)){
            Integer cnt = statisticsMapper.statArticle(LocalDateTime.of(yesterday, LocalTime.of(0,0,0)),
                    LocalDateTime.of(yesterday, LocalTime.of(23, 59, 59)));
            map.put(key, cnt);
            cacheMap.putIfAbsent(key, cnt);
        }
        else {
            map.put(key, cacheMap.get(key));
        }

        Integer yesterdayCnt = cacheMap.get(key);
        if (yesterdayCnt > 0) {
            DecimalFormat df = new DecimalFormat("##.00%");
            map.put("articlePercent", df.format((articleCnt - yesterdayCnt) * 1.0 / yesterdayCnt));
        } else{
            map.put("articlePercent", "-");
        }

        key = "articleTotalCnt";
        if (!cacheMap.contains(key)){
            Integer cnt = statisticsMapper.statArticle(null, LocalDateTime.of(today, LocalTime.of(0, 0, 0)));
            map.put(key, cnt + articleCnt);
            cacheMap.putIfAbsent(key, cnt);
        } else {
            map.put(key, cacheMap.get(key) + articleCnt);
        }

        // Topic
        key = "topicYesterdayCnt";
        if (!cacheMap.contains(key)){
            Integer cnt = statisticsMapper.statTopic(LocalDateTime.of(yesterday, LocalTime.of(0,0,0)),
                    LocalDateTime.of(yesterday, LocalTime.of(23, 59, 59)));
            map.put(key, cnt);
            cacheMap.putIfAbsent(key, cnt);
        }
        else {
            map.put(key, cacheMap.get(key));
        }

        yesterdayCnt = cacheMap.get(key);
        if (yesterdayCnt > 0) {
            DecimalFormat df = new DecimalFormat("##.00%");
            map.put("topicPercent", df.format((topicCnt - yesterdayCnt) * 1.0 / yesterdayCnt));
        }else{
            map.put("topicPercent", "-");
        }

        key = "topicTotalCnt";
        if (!cacheMap.contains(key)){
            Integer cnt = statisticsMapper.statTopic(null, LocalDateTime.of(today, LocalTime.of(0, 0, 0)));
            map.put(key, cnt + topicCnt);
            cacheMap.putIfAbsent(key, cnt);
        } else {
            map.put(key, cacheMap.get(key) + topicCnt);
        }

        // consultation
        key = "consultationYesterdayCnt";
        if (!cacheMap.contains(key)){
            Integer cnt = statisticsMapper.statConsultation(LocalDateTime.of(yesterday, LocalTime.of(0,0,0)),
                    LocalDateTime.of(yesterday, LocalTime.of(23, 59, 59)));
            map.put(key, cnt);
            cacheMap.putIfAbsent(key, cnt);
        }
        else {
            map.put(key, cacheMap.get(key));
        }

        yesterdayCnt = cacheMap.get(key);
        if (yesterdayCnt > 0) {
            DecimalFormat df = new DecimalFormat("##.00%");
            map.put("consultationPercent", df.format((consultationCnt - yesterdayCnt) * 1.0 / yesterdayCnt));
        }else{
            map.put("consultationPercent", "-");
        }

        key = "consultationTotalCnt";
        if (!cacheMap.contains(key)){
            Integer cnt = statisticsMapper.statConsultation(null, LocalDateTime.of(today, LocalTime.of(0, 0, 0)));
            map.put(key, cnt + consultationCnt);
            cacheMap.putIfAbsent(key, cnt);
        } else {
            map.put(key, cacheMap.get(key) + consultationCnt);
        }

        // likes
        key = "likesYesterdayCnt";
        if (!cacheMap.contains(key)){
            Integer cnt = statisticsMapper.statLikes(LocalDateTime.of(yesterday, LocalTime.of(0,0,0)),
                    LocalDateTime.of(yesterday, LocalTime.of(23, 59, 59)));
            map.put(key, cnt);
            cacheMap.putIfAbsent(key, cnt);
        }
        else {
            map.put(key, cacheMap.get(key));
        }

        yesterdayCnt = cacheMap.get(key);
        if (yesterdayCnt > 0) {
            DecimalFormat df = new DecimalFormat("##.00%");
            map.put("likesPercent", df.format((likesCnt - yesterdayCnt) * 1.0 / yesterdayCnt));
        }else{
            map.put("likesPercent", "-");
        }

        key = "likesTotalCnt";
        if (!cacheMap.contains(key)){
            Integer cnt = statisticsMapper.statLikes(null, LocalDateTime.of(today, LocalTime.of(0, 0, 0)));
            map.put(key, cnt + likesCnt);
            cacheMap.putIfAbsent(key, cnt);
        } else {
            map.put(key, cacheMap.get(key) + likesCnt);
        }

        // follows
        key = "followsYesterdayCnt";
        if (!cacheMap.contains(key)){
            Integer cnt = statisticsMapper.statFollows(LocalDateTime.of(yesterday, LocalTime.of(0,0,0)),
                    LocalDateTime.of(yesterday, LocalTime.of(23, 59, 59)));
            map.put(key, cnt);
            cacheMap.putIfAbsent(key, cnt);
        }
        else {
            map.put(key, cacheMap.get(key));
        }

        yesterdayCnt = cacheMap.get(key);
        if (yesterdayCnt > 0) {
            DecimalFormat df = new DecimalFormat("##.00%");
            map.put("followsPercent", df.format((followsCnt - yesterdayCnt) * 1.0 / yesterdayCnt));
        }else{
            map.put("followsPercent", "-");
        }

        key = "followsTotalCnt";
        if (!cacheMap.contains(key)){
            Integer cnt = statisticsMapper.statFollows(null, LocalDateTime.of(today, LocalTime.of(0, 0, 0)));
            map.put(key, cnt + followsCnt);
            cacheMap.putIfAbsent(key, cnt);
        } else {
            map.put(key, cacheMap.get(key) + followsCnt);
        }

        return map;
    }
}
