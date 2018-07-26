package com.blueocean.azbrain.util;

import com.blueocean.azbrain.common.ResultObject;
import com.github.pagehelper.Page;
import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StringUtil {
    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    public static List<Map<String, String>> split(String s){
        //logger.info(s);
        return Splitter.on(",").withKeyValueSeparator(":")
                .split(s).entrySet().stream()
                .map(m ->{
                    Map<String, String> map = new HashMap<>();
                    map.put("name", m.getKey());
                    map.put("value", m.getValue());
                    return map;
                })
                .sorted(Comparator.comparing(o -> o.get("name")))
                .collect(Collectors.toList());
    }

    public static void evaluation(Page<Map<String, Object>> page){
        for (Map<String, Object> m : page.getResult()) {
            String eval = m.get("evaluation").toString();
            m.put("labels", StringUtil.split(eval));
            m.remove("evaluation");
        }
    }

    public static Map<String, Object> pageToMap(String key, Page page){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(key, page.getResult());
        resultMap.put("page", ResultObject.pageMap(page));
        return resultMap;
    }
}
