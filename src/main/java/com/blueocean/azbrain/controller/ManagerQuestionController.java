package com.blueocean.azbrain.controller;

import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Question;
import com.blueocean.azbrain.service.AnswerService;
import com.blueocean.azbrain.service.QuestionService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.vo.QuestionVo;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/manager/question")
public class ManagerQuestionController {
    private final Logger logger = LoggerFactory.getLogger(ManagerQuestionController.class);

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    /**
     * 根据条件搜索问题
     *
     * @param title 问题标题
     * @param page 当前页
     * @param startTime 开始发布时间
     * @param endTime 结束发布时间
     * @return
     */
    @RequestMapping(value="/search", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject search(@RequestParam(value="title", required = false)  String title,
        @RequestParam("page") Integer page,
        @RequestParam(value="start_time", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
        @RequestParam(value="end_time", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime){
        HashMap<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("title", title);
        conditionMap.put("startTime", startTime);
        conditionMap.put("endTime", endTime);
        Page<Question> questions = questionService.searchByCondition(page, AZBrainConstants.MANAGER_PAGE_SIZE, conditionMap);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("questions", questions);
        resultMap.put("page", ResultObject.pageMap(questions));

        return ResultObject.ok(resultMap);
    }

    /**
     * 关闭问题
     *
     * @param questionId
     * @return
     */
    @RequestMapping(value="/close", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject close(@RequestParam(value="question_id")  int questionId){
        int ret = questionService.close(questionId);
        return ret == 1 ? ResultObject.ok(): ResultObject.fail(ResultCode.MANAGE_CLOSED_QUESTION_FAILED);
    }

    @RequestMapping(value="/add", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject add(@RequestBody QuestionVo questionVo){
        logger.info("{}, {}", questionVo.getTitle(),questionVo.getAnswers().size());
        return ResultObject.ok();
    }
}
