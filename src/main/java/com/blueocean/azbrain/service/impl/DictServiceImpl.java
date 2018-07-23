package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.dao.DictMapper;
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
        String otherCode;
        if (label.getClassify().equalsIgnoreCase("byuser")){
            otherCode = "019999";
        } else if (label.getClassify().equalsIgnoreCase("user")){
            otherCode = "029999";
        } else if (label.getClassify().equalsIgnoreCase("user")){
            otherCode = "000000";
        } else {
            return 0;
        }
        String maxcode = dictMapper.maxCode(label.getClassify(), otherCode);
        label.setCode(String.valueOf(Integer.valueOf(maxcode) + 1));
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
}
