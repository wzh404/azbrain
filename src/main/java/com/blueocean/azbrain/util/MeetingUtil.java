package com.blueocean.azbrain.util;

import com.blueocean.azbrain.common.Meeting;
import com.blueocean.azbrain.model.ConsultationLog;

import java.time.LocalDateTime;
import java.util.*;

public class MeetingUtil {
    private static SortedMap<String, Meeting> meetings = new TreeMap<>();

    private MeetingUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void init(List<Map<String, String>> list){
        list.forEach(m -> {
            String host = m.get("host");
            String pwd = m.get("pwd");
            Meeting meeting = new Meeting(host, pwd);
            meetings.put(getKey(host, pwd), meeting);
        });
    }
    /**
     * 初始化会议主持人id，会议密码、会议时间
     *
     * @param logs
     */
    public static void merge(List<ConsultationLog> logs){
        logs.forEach(l -> {
            String key = getKey(l.getMeetingHost(),l.getMeetingPwd());
            Meeting meeting = meetings.get(key);
            if (meeting == null){
                return;
            }

            meeting.add(LocalDateTime.of(l.getCdate(), l.getStartTime()),
                    LocalDateTime.of(l.getCdate(), l.getEndTime()));
        });
    }

    /**
     * 获取指定会议时间可用的会议支持id及密码
     *
     * @param s
     * @param e
     * @return
     */
    public static synchronized Optional<Meeting> get(LocalDateTime s, LocalDateTime e){
        return meetings.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(m -> !m.contains(s, e))
                .findFirst();
    }

    /**
     * 根据主持人及密码，设置会议时间
     *
     * @param host
     * @param pwd
     * @param s
     * @param e
     * @return
     */
    public  static synchronized boolean set(String host, String pwd, LocalDateTime s, LocalDateTime e){
        return Optional.ofNullable(meetings.get(getKey(host, pwd)))
                .map(m->m.add(s,e))
                .isPresent();
    }

    public  static synchronized void remove(String host, String pwd, LocalDateTime s, LocalDateTime e){
        Optional.ofNullable(meetings.get(getKey(host, pwd)))
                .ifPresent(m ->m.remove(s, e));
    }

    private static String getKey(String host, String pwd){
        StringBuilder key = new StringBuilder(host);
        key.append("-");
        key.append(pwd);

        return key.toString();
    }
}
