package com.blueocean.azbrain.controller.manager;

import com.blueocean.azbrain.common.ResultCode;
import com.blueocean.azbrain.common.ResultObject;
import com.blueocean.azbrain.model.Label;
import com.blueocean.azbrain.service.DictService;
import com.blueocean.azbrain.util.AZBrainConstants;
import com.blueocean.azbrain.util.StringUtil;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
                             @RequestParam("classify") String classify,
                             @RequestParam(value="name", required = false)String name,
                             @RequestParam(value="startTime", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date startTime,
                             @RequestParam(value="endTime", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date endTime){
        int pageSize = AZBrainConstants.MANAGER_PAGE_SIZE;
        if (page == 0) pageSize = 50;

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("classify", classify);
        if (!StringUtils.isEmpty(name)) conditionMap.put("name", name);
        if (startTime != null) conditionMap.put("startTime", startTime);
        if (endTime != null) conditionMap.put("endTime", endTime);

        Page<Label> labelPage = dictService.listLabel(page, pageSize, conditionMap);
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
        int rows = dictService.update(label);
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
        label.setCreateTime(LocalDateTime.now());
        label.setValueType("star");
        label.setStatus("00");

        int rows = dictService.insertLabel(label);
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }

    @RequestMapping(value="/label/disable", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject pause(@RequestParam("id") Integer id){
        int rows = dictService.changeStatus(id, "09");
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }

    @RequestMapping(value="/label/enable", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject enable(@RequestParam("id") Integer id){
        int rows = dictService.changeStatus(id, "00");
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }

    @RequestMapping(value="/label/delete", method= {RequestMethod.POST,RequestMethod.GET})
    public ResultObject delete(@RequestParam("id") Integer id){
        int rows = dictService.changeStatus(id, "99");
        return ResultObject.cond(rows > 0, ResultCode.BAD_REQUEST);
    }
}
