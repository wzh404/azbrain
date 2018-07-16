package com.blueocean.azbrain.controller.manager;

import com.blueocean.azbrain.common.ManagerSessionObject;
import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Topic;
import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.service.TopicService;
import com.blueocean.azbrain.service.UserManagerService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.util.StringUtil;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController("manageTopicController")
@RequestMapping("/manager")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @Autowired
    private UserManagerService userManagerService;

    @RequestMapping(value="/page/topics", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject pageTopics(@RequestParam("page") Integer page,
           @RequestParam(value="name", required = false)String name,
           @RequestParam(value="startTime", required = false)LocalDateTime startTime,
           @RequestParam(value="endTime", required = false)LocalDateTime endTime){
        Map<String, Object> conditionMap = new HashMap<>();
        if (name != null) conditionMap.put("name", name);
        if (startTime != null) conditionMap.put("startTime", startTime);
        if (endTime != null) conditionMap.put("startTime", endTime);

        Page<Topic> topicPage = topicService.pageTopics(page, AZBrainConstants.MANAGER_PAGE_SIZE, conditionMap);
        //return ResultObject.ok(topicPage.getResult());
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("topics", topicPage.getResult());
        resultMap.put("page", ResultObject.pageMap(topicPage));
        return ResultObject.ok(resultMap);
    }

    @RequestMapping(value="/topic/specialists", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject topicSpecialists(@RequestParam("page") Integer page,
                                   @RequestParam("topic_id")Integer topicId,
                                   @RequestParam(value="name", required = false)String name,
                                   @RequestParam(value="startTime", required = false)LocalDateTime startTime,
                                   @RequestParam(value="endTime", required = false)LocalDateTime endTime){
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("topicId", topicId);
        if (name != null) conditionMap.put("name", name);
        if (startTime != null) conditionMap.put("startTime", startTime);
        if (endTime != null) conditionMap.put("startTime", endTime);

        Page<User> userPage = userManagerService.searchTopicSpecialists(page, AZBrainConstants.MANAGER_PAGE_SIZE, conditionMap);
        //return ResultObject.ok(userPage.getResult());
        return ResultObject.ok(StringUtil.pageToMap("users", userPage));
    }

    @RequestMapping(value="/view/topic", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject viewTopic(@RequestParam("topic_id") Integer topicId){
        Topic topic = topicService.viewTopic(topicId);
        return ResultObject.ok("topic", topic);
    }

    @RequestMapping(value="/new/topic", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject newTopic(HttpServletRequest request, @RequestBody Topic topic){
        Integer userId = ManagerSessionObject.fromSession(request.getSession()).getId();

        topic.setCreateTime(LocalDateTime.now());
        topic.setCreateBy(userId);
        topic.setStatus("00");
        int rows = topicService.newTopic(topic);
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }

    @RequestMapping(value="/edit/topic", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject editTopic(@RequestBody Topic topic){
        int rows = topicService.editTopic(topic);
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }

    @RequestMapping(value="/new/topic/specialist", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject newTopicSpecialist(@RequestParam("topic_id") Integer topicId,
                                           @RequestParam("user_id") Integer userId){
        int rows = topicService.newTopicSpecialist(topicId, userId);
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }

    @RequestMapping(value="/delete/topic/specialist", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject deleteTopicSpecialist(@RequestParam("topic_id") Integer topicId,
                                           @RequestParam("user_id") Integer userId){
        int rows = topicService.deleteTopicSpecialist(topicId, userId);
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }
}
