package com.blueocean.azbrain.controller.manager;

import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.model.ArticleEvaluate;
import com.blueocean.azbrain.model.EventLog;
import com.blueocean.azbrain.service.ArticleService;
import com.blueocean.azbrain.service.DictService;
import com.blueocean.azbrain.service.StatisticsService;
import com.blueocean.azbrain.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/manager")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private DictService dictService;

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value="/stat", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject stat(){
        return ResultObject.ok(statisticsService.stat());
    }

    /**
     * 下载埋点数据
     *
     * @param response
     * @param type 0100用户登录，0110文章详情驻留时间， 0120用户总驻留时间
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value="/stat/event", method= {RequestMethod.GET})
    public ResultObject event(HttpServletResponse response,
                              @RequestParam("type")String type,
                              @RequestParam(value="startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDateTime startTime,
                              @RequestParam(value="endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDateTime endTime){
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("type", type);
        if (startTime == null){
            startTime = LocalDateTime.now().minusDays(7L);
        }
        conditionMap.put("startTime", startTime);

        if (endTime == null){
            endTime = LocalDateTime.now();
        }
        conditionMap.put("endTime", endTime);

        List<EventLog> logList = dictService.listAllEvent(conditionMap);
        OutputStream toClient = null;
        try {
            String fn = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String finalFileName = String.format("event-%s-%s.xls", type, fn);
            response.addHeader("Content-Disposition", "attachment;filename=" + finalFileName);
            toClient = new BufferedOutputStream(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        ExcelUtil.getInstance().exportObj2Excel(toClient, logList, EventLog.class);
        return null;
    }

    /**
     * 下载文章点击数
     *
     * @param response
     * @return
     */
    @RequestMapping(value="/stat/article", method= {RequestMethod.GET})
    public ResultObject article(HttpServletResponse response){
        List<Article> articles = articleService.listArticleClickNum();

        OutputStream toClient = null;
        try {
            String fn = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String finalFileName = String.format("article-%s.xls", fn);
            response.addHeader("Content-Disposition", "attachment;filename=" + finalFileName);
            toClient = new BufferedOutputStream(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }
        ExcelUtil.getInstance().exportObj2Excel(toClient, articles, Article.class);
        return null;
    }
}
