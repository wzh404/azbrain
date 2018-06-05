package com.blueocean.azbrain.controller;

import com.blueocean.azbrain.common.ManagerSessionObject;
import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Answer;
import com.blueocean.azbrain.model.Question;
import com.blueocean.azbrain.service.AnswerService;
import com.blueocean.azbrain.service.QuestionService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.util.CryptoUtil;
import com.blueocean.azbrain.vo.QuestionVo;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.sql.SQLException;
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

    @Value("${spring.resources.static-locations}")
    private String resourceLocation;

    @Value("${azbrain.question.icon.url}")
    private String questionIconUrl;

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
     * 查看问题
     *
     * @param questionId
     * @return
     */
    @RequestMapping(value="/detail", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject detail(@RequestParam("question_id") Integer questionId){
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("question", questionService.get(questionId));
        Page<Answer> pageAnswer = questionService.getQuestionAnswers(1, AZBrainConstants.MANAGER_PAGE_SIZE, questionId, "time");
        resultMap.put("answers", pageAnswer.getResult());
        resultMap.put("page", ResultObject.pageMap(pageAnswer));

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

    /**
     * 新增问题及答案
     *
     * @param questionVo
     * @return
     */
    @RequestMapping(value="/add", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject add(HttpServletRequest request,
                            @RequestBody QuestionVo questionVo) throws SQLException {
        logger.info("{}, {}", questionVo.getTitle(), questionVo.getIcon());
        Question question = questionVo.asQuestion();

        HttpSession session = request.getSession();
        ManagerSessionObject mso = ManagerSessionObject.fromSession(session);
        String userName = mso.getName();
        Integer userId = mso.getId();
        logger.info("{}, {}, {}", session.getId(), userId, userName);

        question.setCreateName(userName);
        question.setCreateBy(userId);

        int ret = questionService.insert(question, questionVo.asAnswers());
        return ret == 1 ? ResultObject.ok() : ResultObject.fail(ResultCode.MANAGE_ADD_QUESTION_FAILED);
    }

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @RequestMapping(value="/upload", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject upload(@RequestParam(value = "file") MultipartFile file){
        if (file.isEmpty()) {
            logger.warn("upload file is empty");
            return ResultObject.fail(ResultCode.MANAGE_UPLOAD_FILE_FAILED);
        }

        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf('.'));

        // 过滤掉file:前缀
        String filePath = resourceLocation.substring(5);
        String destFileName = CryptoUtil.sha1(fileName, System.currentTimeMillis()+"", Math.random()+"");
        File dest = new File(filePath + destFileName + suffixName);
        logger.info("-------{}", dest.getName());
        try {
            file.transferTo(dest);
            String iconUrl = questionIconUrl + destFileName + suffixName;
            return ResultObject.ok("file", iconUrl);
        } catch (Exception e) {
            logger.error("file", e);
            return ResultObject.fail(ResultCode.MANAGE_UPLOAD_FILE_FAILED);
        }
    }

    /**
     * 推荐问题
     *
     * @param questionId
     * @return
     */
    @RequestMapping(value="/recommend", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject recommend(@RequestParam("question_id") Integer questionId){
        if (questionService.isRecommended(questionId)){
            return ResultObject.fail(ResultCode.MANAGE_QUESTION_RECOMMENDED);
        }

        if (questionService.recommend(questionId) > 0){
            return ResultObject.ok();
        } else {
            return ResultObject.fail(ResultCode.MANAGE_RECOMMEND_FAILED);
        }
    }

    /**
     * 取消推荐
     *
     * @param questionId
     * @return
     */
    @RequestMapping(value="/unrecommend", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject unrecommend(@RequestParam("question_id") Integer questionId){
        if (!questionService.isRecommended(questionId)){
            return ResultObject.fail(ResultCode.MANAGE_QUESTION_UNRECOMMENDED);
        }

        if (questionService.unrecommend(questionId) > 0){
            return ResultObject.ok();
        } else {
            return ResultObject.fail(ResultCode.MANAGE_RECOMMEND_FAILED);
        }
    }

    /**
     * 问题编辑(增加、修改问题答案)
     *
     * @param questionVo
     * @return
     */
    @RequestMapping(value="/edit", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject edit(@RequestBody QuestionVo questionVo) throws SQLException {
        questionService.update(questionVo.asQuestion(), questionVo.asAnswers(), questionVo.asNewAnswers());
        return ResultObject.ok();
    }
}
