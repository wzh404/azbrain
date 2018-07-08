package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.Label;
import com.github.pagehelper.Page;

public interface DictService {
    /**
     *
     * @param classify
     * @return
     */
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
    int edit(Label label);
}
