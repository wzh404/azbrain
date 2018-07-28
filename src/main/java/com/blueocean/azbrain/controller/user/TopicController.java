package com.blueocean.azbrain.controller.user;


import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.model.Topic;
import com.blueocean.azbrain.model.UserFollowTopic;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TopicController {
    private static final Logger logger = LoggerFactory.getLogger(TopicController.class);

    @Autowired
    private TopicService topicService;

    /**
     * 我关注的主题
     *
     * @param request
     * @param page
     * @return
     */
    @RequestMapping(value="/user/follow/topics", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject userFollowTopics(HttpServletRequest request, @RequestParam("page") Integer page){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("userId", userId);
        Page<Topic> pageTopic = topicService.myFollowTopics(page, AZBrainConstants.PAGE_SIZE, conditionMap);
        return ResultObject.ok("topic", pageTopic.getResult());
    }

    /**
     * 主题详情
     *
     * @param topicId
     * @return
     */
    @RequestMapping(value="/user/topic/details", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject details(HttpServletRequest request, @RequestParam("topic_id") Integer topicId){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        topicService.resetUpdatedArticleNum(userId, topicId);

        Topic topic = topicService.get(topicId);
        Page<Article> articlePage = topicService.listArticlesByTopic(1, AZBrainConstants.PAGE_SIZE, topicId);

        Map<String, Object> map = new HashMap<>();
        map.put("topic", topic);
        map.put("topicArticleNum", articlePage.getTotal());
        map.put("isfollowed", topicService.isFollowed(userId, topicId));
        map.put("articles", articlePage.getResult());

        return ResultObject.ok(map);
    }

    /**
     * 主题文章分页查询
     *
     * @param topicId
     * @param page
     * @return
     */
    @RequestMapping(value="/user/topic/articles", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject articles(@RequestParam("topic_id") Integer topicId,
                                 @RequestParam("page") Integer page){
        Page<Article> articlePage = topicService.listArticlesByTopic(page, AZBrainConstants.PAGE_SIZE, topicId);
        return ResultObject.ok("articles", articlePage.getResult());
    }

    /**
     * 关注主题
     *
     * @param request
     * @param topicId
     * @return
     */
    @RequestMapping(value="/user/follow/topic", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject follow(HttpServletRequest request, @RequestParam("topic_id") Integer topicId){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        if (topicService.isFollowed(userId, topicId)){
            return ResultObject.fail(ResultCode.USER_FOLLOW_TOPIC_FAILED);
        }

        UserFollowTopic followTopic = new UserFollowTopic();
        followTopic.setUserId(userId);
        followTopic.setTopicId(topicId);
        followTopic.setFollowTime(LocalDateTime.now());

        int rows = topicService.follow(followTopic);
        return ResultObject.cond(rows > 0, ResultCode.USER_FOLLOW_TOPIC_FAILED);
    }

    /**
     * 取消主题
     *
     * @param request
     * @param topicId
     * @return
     */
    @RequestMapping(value="/user/unfollow/topic", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject unfollow(HttpServletRequest request,@RequestParam("topic_id") Integer topicId) {
        Integer userId = (Integer) request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        if (!topicService.isFollowed(userId, topicId)){
            return ResultObject.fail(ResultCode.USER_FOLLOW_TOPIC_FAILED);
        }

        int rows = topicService.unfollow(userId, topicId);
        return ResultObject.cond(rows > 0, ResultCode.USER_FOLLOW_TOPIC_FAILED);
    }

    /**
     * 查看所有话题
     *
     * @return
     */
    @RequestMapping(value="/user/topics", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject topics(@RequestParam("page") Integer page){
        Map<String, Object> conditionMap = new HashMap<>();
        Page<Topic> topicPage = topicService.pageTopics(page, AZBrainConstants.PAGE_SIZE, conditionMap);
        return ResultObject.ok(topicPage.getResult());
    }
}

