package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wangzunhui on 2018/3/13.
 */
@Mapper
public interface UserMapper {
    /**
     * insert user info.
     *
     * @param user user bean
     * @return rows
     */
    int insert(User user);

    /**
     * get user by id.
     *
     * @param id
     * @return
     */
    User get(@Param("id")int id);

    /**
     * get user by login name.
     *
     * @param name login name
     * @return user
     */
    User getUserByName(@Param("name") String name);

    /**
     * set user del_flag to true 'T'.
     *
     * @param id
     * @return
     */
    int delete(@Param("id")int id);

    /**
     * change user password by id.
     *
     * @param id
     * @param password
     * @return
     */
    int changeLoginPwd(@Param("id")int id, @Param("password")String password);

    /**
     * 修改用户状态
     *
     * @param id
     * @param status
     * @return
     */
    int changeStatus(@Param("id")int id, @Param("status")String status);

    /**
     * paging query user.
     *
     * @return
     */
    Page<User> findByPage(HashMap<String, Object> conditionMap);

    /**
     * 列表全部用户
     * @return
     */
    List<User> list();

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    int update(User user);
}
