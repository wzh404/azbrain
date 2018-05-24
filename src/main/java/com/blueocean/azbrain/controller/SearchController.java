package com.blueocean.azbrain.controller;

import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Answer;
import com.blueocean.azbrain.model.Question;
import com.blueocean.azbrain.service.AnswerService;
import com.blueocean.azbrain.service.QuestionService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.util.TokenUtil;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    @RequestMapping(value="/search/question", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject question(@RequestParam("key")String key, @RequestParam("page") Integer page){

        Page<Question> pageQuestion = questionService.search(page, AZBrainConstants.PAGE_SIZE, key);

        return ResultObject.ok("question", pageQuestion.getResult());
    }

    @RequestMapping(value="/search/answer", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject answer(@RequestParam("key")String key, @RequestParam("page") Integer page){


        Page<Answer> pageAnswer = answerService.search(page, AZBrainConstants.PAGE_SIZE, key);
        return ResultObject.ok("answer", pageAnswer.getResult());
    }
}
