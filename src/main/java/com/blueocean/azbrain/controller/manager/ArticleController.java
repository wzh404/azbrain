package com.blueocean.azbrain.controller.manager;

import com.blueocean.azbrain.common.ManagerSessionObject;
import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.service.ArticleService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.util.StringUtil;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController("manageArticleController")
@RequestMapping("/manager")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @RequestMapping(value="/draft/articles", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject draftList(@RequestParam("page") Integer page,
        @RequestParam(value="name", required = false)String name,
        @RequestParam(value="startTime", required = false)LocalDateTime startTime,
        @RequestParam(value="endTime", required = false)LocalDateTime endTime){
        Map<String, Object> conditionMap = new HashMap<>();
        if (name != null) conditionMap.put("name", name);
        if (startTime != null) conditionMap.put("startTime", startTime);
        if (endTime != null) conditionMap.put("startTime", endTime);

        Page<Article> pageArticle = articleService.draftList(page, AZBrainConstants.MANAGER_PAGE_SIZE, conditionMap);
        //return ResultObject.ok("articles", pageArticle.getResult());
        return ResultObject.ok(StringUtil.pageToMap("articles", pageArticle));
    }

    @RequestMapping(value="/articles", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject list(@RequestParam("page") Integer page,
                             @RequestParam(value="name", required = false)String name,
                             @RequestParam(value="startTime", required = false)LocalDateTime startTime,
                             @RequestParam(value="endTime", required = false)LocalDateTime endTime){
        Map<String, Object> conditionMap = new HashMap<>();
        if (name != null) conditionMap.put("name", name);
        if (startTime != null) conditionMap.put("startTime", startTime);
        if (endTime != null) conditionMap.put("startTime", endTime);

        Page<Article> pageArticle = articleService.list(page, AZBrainConstants.MANAGER_PAGE_SIZE, conditionMap);
        //return ResultObject.ok("articles", pageArticle.getResult());
        return ResultObject.ok(StringUtil.pageToMap("articles", pageArticle));
    }

    @RequestMapping(value="/top/articles", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject topList(@RequestParam("page") Integer page,
                                @RequestParam(value="name", required = false)String name,
                                @RequestParam(value="startTime", required = false)LocalDateTime startTime,
                                @RequestParam(value="endTime", required = false)LocalDateTime endTime){
        Map<String, Object> conditionMap = new HashMap<>();
        if (name != null) conditionMap.put("name", name);
        if (startTime != null) conditionMap.put("startTime", startTime);
        if (endTime != null) conditionMap.put("startTime", endTime);

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
        article.setCreateBy(userId);
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
                                          @RequestParam("article_id") Integer articleId){
        Page<Map<String, Object>> pages = articleService.evaluateOnArticle(page, AZBrainConstants.MANAGER_PAGE_SIZE, articleId);
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
    public ResultObject summaryArticleEvaluation(@RequestParam("page") Integer page){
        Page<Map<String, Object>> pages = articleService.summaryArticleEvaluation(page, AZBrainConstants.MANAGER_PAGE_SIZE);
        StringUtil.evaluation(pages);

        return ResultObject.ok(StringUtil.pageToMap("evaluation", pages));
    }
}
