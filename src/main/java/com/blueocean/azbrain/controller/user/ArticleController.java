package com.blueocean.azbrain.controller.user;


import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.service.ArticleService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class ArticleController {
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value="/usr/watch/articles", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject watch(@RequestParam("page") Integer page){
        Page<Article> pageArticle = articleService.watch(page, AZBrainConstants.PAGE_SIZE, new HashMap<>());

        return ResultObject.ok("article", pageArticle.getResult());
    }
}
