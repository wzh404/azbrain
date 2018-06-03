package com.blueocean.azbrain;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;


public class JsoupTests {

    @Test
    public void testSimple(){
        String htmlString = "<p><strong><em>Click on the Image Below to resize!</em></strong></p><p><br></p><p><img src=\"/vue-quill-editor/static/images/surmon-6.jpg\" width=\"500\"></p><p><br></p><p><strong><em>Or drag/paste an image here.</em></strong></p>";
        Document doc = Jsoup.parse(htmlString);
        System.out.println("["+ doc.body().text() + "]");
        System.out.println("["+ doc.selectFirst("img").attr("src")+ "]");
        Assert.assertTrue(1==1);
    }
}
