package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface UserMapper {
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
    User getUserByName(@Param("name")String login);

    /**
     * paging query user.
     *
     * @return
     */
    Page<User> findByPage(Map<String, Object> conditionMap);
}