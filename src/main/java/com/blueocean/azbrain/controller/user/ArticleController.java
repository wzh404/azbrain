package com.blueocean.azbrain.controller.user;


import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.model.ArticleEvaluate;
import com.blueocean.azbrain.model.UserLikeArticle;
import com.blueocean.azbrain.service.ArticleService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.vo.ArticleEvaluateVo;
import com.github.pagehelper.Page;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
public class ArticleController {
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;

    /**
     * 文章详情
     *
     * @param articleId
     * @return
     */
    @RequestMapping(value="/user/article", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject article(@RequestParam("article_id") Integer articleId){
        Article article = articleService.get(articleId);
        return ResultObject.ok(article);
    }

    /**
     * 大家都在看的文章
     *
     * @param page
     * @return
     */
    @RequestMapping(value="/user/watch/articles", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject watch(@RequestParam("page") Integer page){
        Page<Article> pageArticle = articleService.watch(page, AZBrainConstants.PAGE_SIZE, new HashMap<>());

        return ResultObject.ok("article", pageArticle.getResult());
    }

    /**
     * 点赞文章
     *
     * @param request
     * @param articleId
     * @return
     */
    @RequestMapping(value="/user/like/article", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject like(HttpServletRequest request, @RequestParam("article_id")Integer articleId){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        if (articleService.isLiked(articleId, userId)){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        UserLikeArticle likeArticle = new UserLikeArticle();
        likeArticle.setUserId(userId);
        likeArticle.setArticleId(articleId);
        likeArticle.setLikeTime(LocalDateTime.now());
        int rows = articleService.like(likeArticle);
        return ResultObject.cond(rows > 0, ResultCode.USER_LIKE_ARTICLE_FAILED);
    }

    /**
     * 取消点赞
     *
     * @param request
     * @param articleId
     * @return
     */
    @RequestMapping(value="/user/unlike/article", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject unlike(HttpServletRequest request, @RequestParam("article_id")Integer articleId){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        if (!articleService.isLiked(articleId, userId)){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        int rows = articleService.unlike(articleId, userId);
        return ResultObject.cond(rows > 0, ResultCode.USER_LIKE_ARTICLE_FAILED);
    }

    /**
     * 评论文章
     *
     * @param request
     * @param evaluateVo
     * @return
     */
    @RequestMapping(value="/user/evaluate/article", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject evaluate(HttpServletRequest request, @RequestBody ArticleEvaluateVo evaluateVo){
        Integer userId = (Integer)request.getAttribute(AZBrainConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, "please log in");

        List<ArticleEvaluate> list = articleService.getArticleEvaluate(userId, evaluateVo.getArticleId());
        if (list != null && list.size() > 0){
            return ResultObject.fail(ResultCode.BAD_REQUEST);
        }

        evaluateVo.setUserId(userId);
        int rows = articleService.insertArticleEvaluate(evaluateVo);
        return ResultObject.cond(rows > 0, ResultCode.USER_ILLEGAL_STATUS);
    }
}
