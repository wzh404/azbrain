package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.dao.ConsultationLogMapper;
import com.blueocean.azbrain.model.ConsultationLog;
import com.blueocean.azbrain.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("consultationService")
public class ConsultationServiceImpl implements ConsultationService {
    @Autowired
    private ConsultationLogMapper consultationLogMapper;

    @Override
    public int insert(ConsultationLog record) {
        return consultationLogMapper.insert(record);
    }

    @Override
    public ConsultationLog get(Integer id) {
        return consultationLogMapper.get(id);
    }
}
