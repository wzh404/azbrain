package com.blueocean.azbrain;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


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
        SnowFlakeWorker s = new SnowFlakeWorker(1);
        int uid = 3948849 % 100;
        long t = System.currentTimeMillis();
        System.out.println("t = " + t);
        long s1 = 365*24*60*60;
        System.out.println((1<<30)/s1);
        System.out.println(s.nextId());
        Assert.assertTrue(1==1);
    }
}
