package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.dao.UserMapper;
import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User get(Integer id) {
        return userMapper.get(id);
    }

    @Override
    public User getUserByName(String login) {
        return userMapper.getUserByName(login);
    }

    @Override
    public Page<User> findByPage(int page, int pageSize, Map<String, Object> conditionMap) {
        PageHelper.startPage(page, pageSize);
        return userMapper.findByPage(conditionMap);
    }
}
