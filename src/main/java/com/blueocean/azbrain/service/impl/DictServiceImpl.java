package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.dao.DictMapper;
import com.blueocean.azbrain.model.Label;
import com.blueocean.azbrain.service.DictService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dictService")
public class DictServiceImpl implements DictService {
    @Autowired
    private DictMapper dictMapper;

    @Override
    public Page<Label> listLabel(int page, int pageSize, String classify) {
        PageHelper.startPage(page, pageSize);
        return dictMapper.listLabel(classify);
    }

    @Override
    public int insertLabel(Label label) {
        return dictMapper.insertLabel(label);
    }

    @Override
    public int edit(Label label) {
        return dictMapper.edit(label);
    }
}
