package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.EventLog;
import com.blueocean.azbrain.model.Label;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface DictService {
    /**
     *
     * @param conditionMap
     * @return
     */
    Page<Label> listLabel(int page, int pageSize, Map<String, Object> conditionMap);

    Page<Label> listLabel(int page, int pageSize, String classify);

    /**
     *
     * @param label
     * @return
     */
    int insertLabel(Label label);

    /**
     *
     * @param label
     * @return
     */
    int update(Label label);

    /**
     *
     * @return
     */
    int changeStatus(Integer id, String status);

    /**
     * 插入系统事件日志
     *
     * @param event
     * @return
     */
    int insertEvent(EventLog event);

    /**
     *
     * @param conditionMap
     * @return
     */
    Page<EventLog> listEvent(int page, int pageSize, Map<String, Object> conditionMap);
}
