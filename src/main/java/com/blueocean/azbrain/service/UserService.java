package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.vo.SpecialistVo;
import com.github.pagehelper.Page;

import java.util.Map;

public interface UserService {
    /**
     * get user by id.
     *
     * @param id
     * @return
     */
    User get(Integer id);

    /**
     * get user by login name.
     *
     * @param login login name
     * @return user
     */
    User getUserByName(String login);

    /**
     * paging query user.
     *
     * @param page
     * @param pageSize
     * @return
     */
    Page<User> findByPage(int page, int pageSize, Map<String, Object> conditionMap);

    /**
     * 查找专家
     *
     * @param vo
     * @return
     */
    Page<User> searchSpecialist(int page, int pageSize, SpecialistVo vo);
}

