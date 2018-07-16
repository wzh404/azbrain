package com.blueocean.azbrain.util;

import com.google.common.base.Splitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StringUtil {
    public static List<Map<String, String>> split(String s){
        return Splitter.on(",").withKeyValueSeparator(":")
                .split(s).entrySet().stream()
                .map(m ->{
                    Map<String, String> map = new HashMap<>();
                    map.put("name", m.getKey());
                    map.put("value", m.getValue());
                    return map;
                }).collect(Collectors.toList());
    }
}
