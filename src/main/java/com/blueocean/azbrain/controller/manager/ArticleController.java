package com.blueocean.azbrain.controller.manager;

import com.blueocean.azbrain.common.ManagerSessionObject;
import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.common.status.ArticleStatus;
import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.service.ArticleService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.util.StringUtil;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("manageArticleController")
@RequestMapping("/manager")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @RequestMapping(value="/draft/articles", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject draftList(@RequestParam("page") Integer page,
        @RequestParam(value="name", required = false)String name,
        @RequestParam(value="source", required = false)String source,
        @RequestParam(value="startTime", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date startTime,
        @RequestParam(value="endTime", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date endTime){
        Map<String, Object> conditionMap = new HashMap<>();
        if (name != null) conditionMap.put("name", name);
        if (source != null) conditionMap.put("source", source);
        if (startTime != null) conditionMap.put("startTime", startTime);
        if (endTime != null) conditionMap.put("endTime", endTime);

        Page<Article> pageArticle = articleService.draftList(page, AZBrainConstants.MANAGER_PAGE_SIZE, conditionMap);
        //return ResultObject.ok("articles", pageArticle.getResult());
        return ResultObject.ok(StringUtil.pageToMap("articles", pageArticle));
    }

    @RequestMapping(value="/articles", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject list(@RequestParam("page") Integer page,
                             @RequestParam(value="name", required = false)String name,
                             @RequestParam(value="source", required = false)String source,
                             @RequestParam(value="startTime", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date startTime,
                             @RequestParam(value="endTime", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date endTime){
        Map<String, Object> conditionMap = new HashMap<>();
        if (name != null) conditionMap.put("name", name);
        if (source != null) conditionMap.put("source", source);
        if (startTime != null) conditionMap.put("startTime", startTime);
        if (endTime != null) conditionMap.put("endTime", endTime);

        Page<Article> pageArticle = articleService.list(page, AZBrainConstants.MANAGER_PAGE_SIZE, conditionMap);
        //return ResultObject.ok("articles", pageArticle.getResult());
        return ResultObject.ok(StringUtil.pageToMap("articles", pageArticle));
    }

    @RequestMapping(value="/top/articles", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject topList(@RequestParam("page") Integer page,
                                @RequestParam(value="name", required = false)String name,
                                @RequestParam(value="source", required = false)String source,
                                @RequestParam(value="startTime", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date startTime,
                                @RequestParam(value="endTime", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date endTime){
        Map<String, Object> conditionMap = new HashMap<>();
        if (name != null) conditionMap.put("name", name);
        if (source != null) conditionMap.put("source", source);
        if (startTime != null) conditionMap.put("startTime", startTime);
        if (endTime != null) conditionMap.put("endTime", endTime);

        Page<Article> pageArticle = articleService.topList(page, AZBrainConstants.MANAGER_PAGE_SIZE, conditionMap);
        //return ResultObject.ok("articles", pageArticle.getResult());
        return ResultObject.ok(StringUtil.pageToMap("articles", pageArticle));
    }

    @RequestMapping(value="/view/article", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject viewArticle(@RequestParam("article_id") Integer articleId){
        Article article = articleService.get(articleId);
        if (article == null){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }
        return ResultObject.ok(article);
    }

    @RequestMapping(value="/new/article", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject newArticle(HttpServletRequest request, @RequestBody Article article){
        Integer userId = ManagerSessionObject.fromSession(request.getSession()).getId();
        article.setCreateTime(LocalDateTime.now());
        if (article.getCreateBy() == null || article.getCreateBy().intValue() == 0){
            article.setCreateBy(userId);
        }
        int rows = articleService.insert(article);
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }

    @RequestMapping(value="/edit/article", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject editArticle(@RequestBody Article article){
        int rows = articleService.edit(article);
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }

    @RequestMapping(value="/top/article", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject top(@RequestParam("article_id") Integer articleId){
        int rows = articleService.top(articleId);
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }

    @RequestMapping(value="/untop/article", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject untop(@RequestParam("article_id") Integer articleId){
        int rows = articleService.untop(articleId);
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }

    /**
     * 文章的所有用户评价
     *
     * @param page
     * @param articleId
     * @return
     */
    @RequestMapping(value="/article/evaluation", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject evaluateOnArticle(@RequestParam("page") Integer page,
                                          @RequestParam("article_id") Integer articleId,
                                          @RequestParam(value = "name", required = false) String name){
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("articleId", articleId);
        if (name != null) {
            conditionMap.put("name", name);
        }

        Page<Map<String, Object>> pages = articleService.evaluateOnArticle(page, AZBrainConstants.MANAGER_PAGE_SIZE, conditionMap);
        StringUtil.evaluation(pages);

        return ResultObject.ok(StringUtil.pageToMap("evaluation", pages));
    }

    /**
     * 文章的汇总评价
     *
     * @param page
     * @return
     */
    @RequestMapping(value="/article/summary/evaluation", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject summaryArticleEvaluation(@RequestParam("page") Integer page,
                                                 @RequestParam(value = "name", required = false) String name){
        Map<String, Object> conditionMap = new HashMap<>();
        if (name != null) {
            conditionMap.put("title", name);
        }
        Page<Map<String, Object>> pages = articleService.summaryArticleEvaluation(page, AZBrainConstants.MANAGER_PAGE_SIZE, conditionMap);
        StringUtil.evaluation(pages);

        return ResultObject.ok(StringUtil.pageToMap("evaluation", pages));
    }

    /**
     * 批量逻辑删除文章
     *
     * @param ids
     * @return
     */
    @RequestMapping(value="/articles/delete", method= {RequestMethod.POST})
    public ResultObject deleteArticles(@RequestBody List<Integer> ids){
        if (ids.isEmpty()){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        int rows = articleService.changeStatusBatch(ids);
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }

    /**
     * 分组汇总文章数量
     *
     * @return
     */
    @RequestMapping(value="/articles/total", method= {RequestMethod.POST, RequestMethod.GET})
    public ResultObject totalArticles(){
        return ResultObject.ok(articleService.totalNum());
    }

    /**
     * 删除文章评价
     *
     * @param userId
     * @param articleId
     * @return
     */
    @RequestMapping(value="/delete/article/evaluation", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject deleteEvaluation(@RequestParam(value="user_id", required = false)Integer userId,
                                         @RequestParam("article_id")Integer articleId){
        int rows = articleService.deleteArticleEvaluate(userId, articleId);
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }
}
