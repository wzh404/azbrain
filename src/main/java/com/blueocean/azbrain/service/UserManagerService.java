package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.User;
import com.github.pagehelper.Page;

import java.util.Map;

public interface UserManagerService {
    /**
     * paging query user.
     *
     * @param page
     * @param pageSize
     * @return
     */
    Page<User> findByPage(int page, int pageSize, Map<String, Object> conditionMap);

}
