package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.Label;
import com.github.pagehelper.Page;

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
}
