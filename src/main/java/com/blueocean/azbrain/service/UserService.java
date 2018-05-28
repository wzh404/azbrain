package com.blueocean.azbrain.service;

import com.blueocean.azbrain.model.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;

/**
 * Created by wangzunhui on 2018/3/13.
 */
public interface UserService {
    /**
     * add user.
     *
     * @param user
     * @return 1 success, other failed.
     */
    public int insert(User user, String inviteCode);

    /**
     * set user del_flag to 'T'
     *
     * @param id
     * @return 1 success, other failed.
     */
    public int delete(Integer id);

    /**
     * get user by id.
     *
     * @param id
     * @return
     */
    public User get(Integer id);

    /**
     * get user by login name.
     *
     * @param login login name
     * @return user
     */
    User getUserByName(@Param("login") String login);

    /**
     * paging query user.
     *
     * @param page
     * @param pageSize
     * @return
     */
    Page<User> findByPage(int page, int pageSize, HashMap<String, Object> conditionMap);

    /**
     * 关注问题
     *
     * @param userId
     * @param questionId
     * @return
     */
    int follow(int userId, int questionId);

    /**
     * 取消关注
     *
     * @param userId
     * @param followQuestionId
     * @return
     */
    int unfollow(int userId, int followQuestionId);

    /**
     * 点赞回答
     *
     * @param userId
     * @param answerId
     * @return
     */
    int like(int userId, int answerId);

    /**
     * 取消点赞
     *
     * @param userId
     * @param likeAnswerId
     * @return
     */
    int unlike(int userId, int likeAnswerId);

    /**
     * 修改用户状态
     *
     * @param id
     * @param status
     * @return
     */
    int changeStatus(int id, String status);
}
