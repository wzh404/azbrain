package com.blueocean.azbrain;

import com.blueocean.azbrain.common.Meeting;
import com.blueocean.azbrain.model.ConsultationLog;
import com.blueocean.azbrain.util.MeetingUtil;
import com.google.common.base.Splitter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDate.*;


public class JsoupTests {
    /*
    public Document getDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String escapeHtml(String html){
        html = html.replaceAll("<[^>]*>", "");
        html = html.replaceAll("&nbsp;", "");

        return html;
    }

    @Test
    public void testSimple() {
        Document doc = getDocument("http://www.yanglao.com.cn/resthome/228005.html");
        System.out.println("电话:" + doc.getElementsByClass("inst-info")
                .select(".cont")
                .select("span.phone")
                .text()
                .replace("&nbsp", ""));

        String[] names = {
                "所在地区",
                "机构类型",
                "机构性质",
                "成立时间",
                "占地面积",
                "建筑面积",
                "床位数",
                "收住对象",
                "收费区间",
                "特色服务",
                "地 址",
                "联 系 人"
        };

        Map<String, Object> map = new HashMap<>();
        Arrays.stream(names).forEach(s -> map.put(s, "a"));

        Elements els = doc.getElementsByClass("base-info").select(".cont").select("li");

        for (int i = 0; i < els.size(); i++) {
            Element el = els.get(i);
            String[] a = el.text().split("：");
            if (map.get(a[0]) != null) {
                System.out.print("*");
            } else {
                continue;
            }

            if (a.length > 1)
                System.out.println(a[0] + " [" + a[1] + "]\n");
            else
                System.out.println(a[0] + "[" + "]\n");
        }

        els = doc.getElementsByClass("contact-info").select(".cont").select("li");
        for (int i = 0; i < els.size(); i++) {
            Element el = els.get(i);
            String[] a = el.text().split("：");
            if (map.get(a[0]) != null) {
                System.out.print("*");
            } else if (i == els.size() - 1) {
                System.out.println(el.text());
                continue;
            } else {
                continue;
            }

            if (a.length > 1)
                System.out.println(a[0] + "[" + a[1] + "]\n");
            else
                System.out.println(a[0] + "[" + a[0] + "]\n");
        }
        System.out.println("--------------------------------------------------");

        String html = escapeHtml((doc.getElementsByClass("inst-intro").select(".cont").html()));
        System.out.println(html);
        System.out.println("--------------------------------------------------");

        html = escapeHtml(doc.getElementsByClass("inst-charge").select(".cont").html());
        System.out.println(html);
        System.out.println("--------------------------------------------------");

        html = escapeHtml(doc.getElementsByClass("facilities").select(".cont").html());
        System.out.println(html);
        System.out.println("--------------------------------------------------");

        html = escapeHtml(doc.getElementsByClass("service-content").select(".cont").select("p").html());
        System.out.println(html);
        System.out.println("--------------------------------------------------");

        html = escapeHtml(doc.getElementsByClass("inst-notes").select(".cont").html());
        System.out.println(html);

        Assert.assertTrue(1 == 1);
    }
*/
    /*
    @Test
    public void testDate(){
        LocalDate ld = LocalDate.now();
        LocalDate yesterday = ld.minusDays(1);
        System.out.println(ld.toString() + " - " + yesterday);
        LocalDateTime dt = LocalDateTime.of(ld, LocalTime.MIN);
        System.out.println(dt);
    }

    @Test
    public void testCode(){
        String code = "10001030";

        LocalTime s = LocalTime.parse(code.substring(0, 2) + ":" + code.substring(2,4));
        LocalTime e = LocalTime.parse(code.substring(4, 6) + ":" + code.substring(6,8));
        System.out.println(s);
        System.out.println(e);
        Assert.assertTrue(1==1);
    }*/

    @Test
    public void testOrder(){
        //Set<String> s = new HashSet<>(100000);

        int uid = 3948849 % 100;
        long sequence = 1000L;
        long lastDays = 0;
        long lastSecs = 0;

        for (int i =0; i < 1; i++) {
            long days = ChronoUnit.DAYS.between(of(2015, 10, 1), now());
            int secs = LocalTime.now().toSecondOfDay();

            if (lastDays == days && lastSecs == secs){
                sequence++;
            } else{
                sequence = 1001;
            }
            String orderId = String.format("%d%d%04d%d", days, secs, sequence%10000, uid);
            lastDays = days;
            lastSecs = secs;

            System.out.println(orderId);
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            System.out.println(URLEncoder.encode("1531997005418_泰颐春-老少同乐3.jpg","utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(1==1);
    }

    @Test
    public void testBase64(){
        String b = "a2hqbDk3Ng==";
        String c = new String(Base64.getDecoder().decode(b));
        System.out.println(c);
        Assert.assertTrue("khjl976".equalsIgnoreCase(c));
    }
/*
    @Test
    public void testMeeting(){
        List<ConsultationLog> logs = new ArrayList<>();
        ConsultationLog log = new ConsultationLog();
        log.setMeetingHost("host1");
        log.setMeetingPwd("pwd1");
        log.setCdate(LocalDate.of(2018, 7, 14));
        log.setStartTime(LocalTime.of(10,0));
        log.setEndTime(LocalTime.of(10,30));
        logs.add(log);

        ConsultationLog log2 = new ConsultationLog();
        log2.setMeetingHost("host2");
        log2.setMeetingPwd("pwd2");
        log2.setCdate(LocalDate.of(2018, 7, 14));
        log2.setStartTime(LocalTime.of(10,31));
        log2.setEndTime(LocalTime.of(11,01));
        logs.add(log2);

        //MeetingUtil.init(logs);
        //Meeting m = new Meeting("host1", "pwd1");
        //boolean b = m.add(LocalDateTime.of(2018, 7, 14, 10, 0),
        //        LocalDateTime.of(2018, 7, 14, 10, 30));
        //boolean c= m.add(LocalDateTime.of(2018, 7, 14, 10, 10),
        //        LocalDateTime.of(2018, 7, 14, 11, 30));
        LocalDateTime s = LocalDateTime.of(2018, 7, 14, 11, 32);
        LocalDateTime e = LocalDateTime.of(2018, 7, 14, 12, 30);
        Optional<Meeting> om = MeetingUtil.get(s, e);
        om.ifPresent(m->{
            boolean b = MeetingUtil.set(m.getHost(), m.getPwd(),s, e);
            System.out.println(m.getHost() + "-" + b);
        });
        Assert.assertTrue(om.isPresent());
        //Assert.assertTrue(c);
    }

    @Test
    public void testSplitter(){
        List<Map<String, String>> l = Splitter.on(",").withKeyValueSeparator(":")
                .split("礼貌:5,态度:4,准时:4").entrySet().stream()
                .map(m ->{
                    Map<String, String> map = new HashMap<>();
                    map.put("name", m.getKey());
                    map.put("value", m.getValue());
                    return map;
                }).collect(Collectors.toList());
        System.out.println(l);
        Assert.assertTrue(1==1);
    }*/
}
