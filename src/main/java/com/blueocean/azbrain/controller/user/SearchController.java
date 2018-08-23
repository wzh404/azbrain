package com.blueocean.azbrain.controller.user;


import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.model.EventLog;
import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.service.ArticleService;
import com.blueocean.azbrain.service.DictService;
import com.blueocean.azbrain.service.UserService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.github.pagehelper.Page;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private DictService dictService;

    @Autowired
    private UserService userService;

    @RequestMapping(value="/search/article", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject question(@RequestParam("key")String key, @RequestParam("page") Integer page){
        Page<Article> pageQuestion = articleService.search(page, AZBrainConstants.PAGE_SIZE, key);

        return ResultObject.ok("article", pageQuestion.getResult());
    }

    /**
     * 记录前端埋点数据
     *
     * @param request
     * @param log
     * @return
     */
    @RequestMapping(value="/user/event", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject eventLog(HttpServletRequest request, @RequestBody EventLog log){
        Integer userId = (Integer) request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, AZBrainConstants.PLEASE_LOG_IN);

        User user = userService.get(userId);
        log.setCreateTime(LocalDateTime.now());
        log.setLevel(0);
        log.setWho(user.getName());

        int rows = dictService.insertEvent(log);
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }
}
