package com.blueocean.azbrain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by @author wangzunhui on 2018/3/15.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultObject {
    private Boolean flag;
    private String errorcode;
    private String message;
    private Object result;

    public ResultObject(Boolean flag, String code, String error, Object result){
        this.flag = flag;
        this.errorcode = code;
        this.message = error;
        this.result = result;
    }

    public static ResultObject ok(String key, Object val){
        Map<String, Object> map = new HashMap();
        map.put(key, val);
        return new ResultObject(true, ResultCode.OK.getCode(), null, map);
    }

    public static Map<String, Object> pageMap(Page page){
        HashMap<String, Object> pageMap = new HashMap<>();
        pageMap.put("total", page.getTotal());
        pageMap.put("pages", page.getPages());
        pageMap.put("pageNum", page.getPageNum());

        return pageMap;
    }

    public static ResultObject ok(){
        return new ResultObject(true, ResultCode.OK.getCode(), null, null);
    }

    public static ResultObject ok(Object r){
        return new ResultObject(true, ResultCode.OK.getCode(), null, r);
    }

    public static ResultObject fail(ResultCode type){
        return fail(type.getCode(), null);
    }

    public static ResultObject fail(String code){
        return new ResultObject(false, code, null, null);
    }

    public static ResultObject fail(String code, String error){
        return new ResultObject(false, code, error, null);
    }
}
