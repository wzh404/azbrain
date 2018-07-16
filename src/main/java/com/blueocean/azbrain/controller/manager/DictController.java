package com.blueocean.azbrain.controller.manager;

import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Label;
import com.blueocean.azbrain.service.DictService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.util.StringUtil;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/manager")
public class DictController {
    @Autowired
    private DictService dictService;

    /**
     * 按类型分页显示标签
     *
     * @param page
     * @param classify
     * @return
     */
    @RequestMapping(value="/labels", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject pageLabel(@RequestParam("page") Integer page,
                             @RequestParam("classify") String classify){
        int pageSize = AZBrainConstants.MANAGER_PAGE_SIZE;
        if (page == 0) pageSize = 50;
        Page<Label> labelPage = dictService.listLabel(page, pageSize, classify);
        //return ResultObject.ok("labels", labelPage.getResult());
        return ResultObject.ok(StringUtil.pageToMap("labels", labelPage));
    }

    /**
     * 编辑标签
     *
     * @param label
     * @return
     */
    @RequestMapping(value="/label/edit", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject editLabel(@RequestBody Label label){
        int rows = dictService.edit(label);
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }

    /**
     * 新增标签
     *
     * @param label
     * @return
     */
    @RequestMapping(value="/label/new", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject InsertLabel(@RequestBody Label label){
        int rows = dictService.edit(label);
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }
}
