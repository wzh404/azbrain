package com.blueocean.azbrain.controller;

import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Answer;
import com.blueocean.azbrain.model.Question;
import com.blueocean.azbrain.service.QuestionService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    /**
     * 用户关注的问题列表
     *
     * @return
     */
    @RequestMapping(value="/user/question/followers", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject listUserFollowQuestion(HttpServletRequest request, @RequestParam("page") Integer page){
        int userId = (int)request.getAttribute("userId");
        Page<Question> r = questionService.getUserFollowQuestions(page, AZBrainConstants.PAGE_SIZE, userId);
        return ResultObject.ok(r.getResult());
    }

    /**
     * 给用户推荐的问题列表
     *
     * @return
     */
    @RequestMapping(value="/user/question/recommends", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject listUserRecommendQuestion(@RequestParam("page") Integer page){
        Page<Question> r = questionService.getUserRecommendQuestions(page, AZBrainConstants.PAGE_SIZE);
        return ResultObject.ok(r.getResult());
    }

    /**
     * 问题答案详情并返回第一个答案
     *
     * @param questionId
     * @return
     */
    @RequestMapping(value="/question/detail", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject answers(HttpServletRequest request,
                                @RequestParam("question_id") Integer questionId,
                                @RequestParam(value = "order_by", required = false) String orderBy){
        int userId = (int)request.getAttribute("userId");
        Question q = questionService.get(questionId);
        Map map = new HashMap<String, Object>();
        map.put("follow_flag", questionService.isFollowed(userId, questionId));
        map.put("question", q);
        if (orderBy == null){
            orderBy = "like";
        }
        Page<Answer> pageAnswer = questionService.getQuestionAnswers(1, AZBrainConstants.PAGE_SIZE, questionId, orderBy);
        map.put("answers", pageAnswer.getResult());
        return ResultObject.ok(map);
    }

    /**
     * 问题答案下拉刷新
     *
     * @param questionId
     * @return
     */
    @RequestMapping(value="/question/answers", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject answers(@RequestParam("question_id") Integer questionId,
                                @RequestParam(value = "order_by", required = false) String orderBy,
                                @RequestParam("page") Integer page){
        if (orderBy == null){
            orderBy = "like";
        }
        Page<Answer> pageAnswer = questionService.getQuestionAnswers(page, AZBrainConstants.PAGE_SIZE, questionId, orderBy);
        return ResultObject.ok("answers", pageAnswer.getResult());
    }
}
