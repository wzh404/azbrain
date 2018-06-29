package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.ConsultationLog;

public interface ConsultationService {
    /**
     *
     * @param record
     * @return
     */
    int insert(ConsultationLog record);

    /**
     *
     * @param id
     * @return
     */
    ConsultationLog get(Integer id);
}
