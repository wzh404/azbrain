package com.blueocean.azbrain.controller;

import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Answer;
import com.blueocean.azbrain.model.Question;
import com.blueocean.azbrain.service.AnswerService;
import com.blueocean.azbrain.service.QuestionService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(allowCredentials="true")
public class SearchController {
    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    @RequestMapping(value="/search", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject search(@RequestParam("key")String key,
                               @RequestParam("page") Integer page){
        Map map = new HashMap<String, Object>();

        Page<Answer> pageAnswer = answerService.search(page, AZBrainConstants.PAGE_SIZE, key);
        Page<Question> pageQuestion = questionService.search(page, AZBrainConstants.PAGE_SIZE, key);

        map.put("question", pageQuestion.getResult());
        map.put("answer", pageAnswer.getResult());

        return ResultObject.ok(map);
    }
}
