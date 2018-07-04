package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.dao.UserMapper;
import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.service.UserManagerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class UserManagerServiceImpl implements UserManagerService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Page<User> findByPage(int page, int pageSize, Map<String, Object> conditionMap) {
        PageHelper.startPage(page, pageSize);
        return userMapper.findByPage(conditionMap);
    }
}
