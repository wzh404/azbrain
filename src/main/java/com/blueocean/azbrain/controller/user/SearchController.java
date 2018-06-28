package com.blueocean.azbrain.controller.user;


import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.service.ArticleService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value="/search/article", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject question(@RequestParam("key")String key, @RequestParam("page") Integer page){
        Page<Article> pageQuestion = articleService.search(page, AZBrainConstants.PAGE_SIZE, key);

        return ResultObject.ok("article", pageQuestion.getResult());
    }
}
