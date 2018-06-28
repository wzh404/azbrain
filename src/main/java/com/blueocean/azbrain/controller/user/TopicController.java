package com.blueocean.azbrain.controller.user;


import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.model.Topic;
import com.blueocean.azbrain.service.ArticleService;
import com.blueocean.azbrain.service.TopicService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.github.pagehelper.Page;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TopicController {
    private static final Logger logger = LoggerFactory.getLogger(TopicController.class);

    @Autowired
    private TopicService topicService;

    @RequestMapping(value="/usr/follow/topics", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject userFollowTopics(HttpServletRequest request, @RequestParam("page") Integer page){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("userId", userId);
        Page<Topic> pageTopic = topicService.userFollowTopics(page, AZBrainConstants.PAGE_SIZE, conditionMap);
        return ResultObject.ok("topic", pageTopic.getResult());
    }
}
