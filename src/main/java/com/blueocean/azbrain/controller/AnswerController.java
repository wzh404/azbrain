package com.blueocean.azbrain.controller;

import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Answer;
import com.blueocean.azbrain.model.AnswerComment;
import com.blueocean.azbrain.model.Question;
import com.blueocean.azbrain.service.AnswerService;
import com.blueocean.azbrain.service.QuestionService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;


    /**
     * 回答详情
     *
     * @param answerId
     * @return
     */
    @RequestMapping(value="/answer/detail", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject detail(HttpServletRequest request, @RequestParam("answer_id") Integer answerId){
        int userId = (int)request.getAttribute("userId");
        Map map = new HashMap<String, Object>();
        Answer answer = answerService.get(answerId);
        if (answer == null){
            return ResultObject.fail("");
        }

        Question question = questionService.get(answer.getQuestionId());
        map.put("like_flag", answerService.isLiked(userId, answerId));
        map.put("question", question);
        map.put("answer", answer);

        Page<AnswerComment> pageAnswerComment = answerService.getAnswerComments(1, AZBrainConstants.PAGE_SIZE, answerId);
        if (pageAnswerComment.getResult() != null) {
            map.put("comments", pageAnswerComment.getResult());
        }
        return ResultObject.ok(map);
    }


    /**
     * 回答评论下拉刷新
     *
     * @param answerId
     * @param page
     * @return
     */
    @RequestMapping(value="/answer/comments", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject comments(
            @RequestParam("answer_id") Integer answerId,
            @RequestParam("page") Integer page){
        Page<AnswerComment> pageAnswerComment = answerService.getAnswerComments(page, AZBrainConstants.PAGE_SIZE, answerId);
        return ResultObject.ok("comments", pageAnswerComment.getResult());
    }
}
