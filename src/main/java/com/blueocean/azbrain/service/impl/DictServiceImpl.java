package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.dao.DictMapper;
import com.blueocean.azbrain.model.EventLog;
import com.blueocean.azbrain.model.Label;
import com.blueocean.azbrain.service.DictService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("dictService")
public class DictServiceImpl implements DictService {
    @Autowired
    private DictMapper dictMapper;

    @Override
    public Page<Label> listLabel(int page, int pageSize, Map<String, Object> conditionMap) {
        PageHelper.startPage(page, pageSize);
        return dictMapper.listLabel(conditionMap);
    }

    @Override
    public Page<Label> listLabel(int page, int pageSize, String classify) {
        PageHelper.startPage(page, pageSize);
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("classify", classify);
        return dictMapper.listLabel(conditionMap);
    }

    @Override
    public int insertLabel(Label label) {
        Label l = dictMapper.getLabelByName(label.getName());
        if (l != null){
            return 0;
        }

        String otherCode;
        String minCode;
        if (label.getClassify().equalsIgnoreCase("byuser")){
            otherCode = "019999";
            minCode = "010001";
        } else if (label.getClassify().equalsIgnoreCase("user")){
            otherCode = "029999";
            minCode = "020001";
        } else if (label.getClassify().equalsIgnoreCase("article")){
            otherCode = "000000";
            minCode = "030001";
        } else if (label.getClassify().equalsIgnoreCase("specialist")){
            otherCode = "000000";
            minCode = "040001";
        } else {
            return -1;
        }

        String maxcode = dictMapper.maxCode(label.getClassify(), otherCode);
        if (maxcode != null) {
            label.setCode("0" + String.valueOf(Integer.valueOf(maxcode) + 1));
        } else {
            label.setCode(minCode);
        }
        return dictMapper.insertLabel(label);
    }

    @Override
    public int update(Label label) {
        return dictMapper.update(label);
    }

    @Override
    public int changeStatus(Integer id, String status) {
        return dictMapper.changeStatus(id, status);
    }

    @Override
    public int insertEvent(EventLog event) {
        return dictMapper.insertEvent(event);
    }

    @Override
    public Page<EventLog> listEvent(int page, int pageSize, Map<String, Object> conditionMap) {
        PageHelper.startPage(page, pageSize);
        return dictMapper.listEvent(conditionMap);
    }
}
