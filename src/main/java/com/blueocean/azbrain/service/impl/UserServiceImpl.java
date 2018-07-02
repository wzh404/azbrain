package com.blueocean.azbrain.service.impl;

import com.blueocean.azbrain.dao.ArticleMapper;
import com.blueocean.azbrain.dao.UserMapper;
import com.blueocean.azbrain.model.Article;
import com.blueocean.azbrain.model.User;
import com.blueocean.azbrain.model.UserPoints;
import com.blueocean.azbrain.service.ArticleService;
import com.blueocean.azbrain.service.UserService;
import com.blueocean.azbrain.vo.SpecialistVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleService articleService;

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

    @Override
    public Page<User> searchSpecialist(int page, int pageSize, SpecialistVo vo) {
        PageHelper.startPage(page, pageSize);
        return userMapper.searchSpecialist(vo);
    }

    @Override
    public Map<String, Object> profile(Integer userId) {
        Map<String, Object> map = new HashMap<>();
        User user = userMapper.get(userId);
        if (user == null) {
            return map;
        }

        Map<String, Object> score = userMapper.userAvgScore(userId);
        if (score == null) {
            score = new HashMap<>();
        }

        map.put("profile", user);
        map.put("score", score);
        return map;
    }

    @Override
    public Map<String, Object> specialistProfile(Integer userId) {
        Map<String, Object> map = new HashMap<>();
        User user = userMapper.get(userId);
        if (user == null) {
            return map;
        }

        Map<String, Object> score = userMapper.specialistAvgScore(userId);
        if (score == null) {
            score = new HashMap<>();
        }

        Page<Article> articlePage = articleService.specialistArticles(1, 4, userId);
        map.put("articles", articlePage.getResult());
        map.put("profile", user);
        map.put("score", score);

        return map;
    }

    @Override
    public Map<String, Object> consultationConditions(Integer userId) {
        Map<String, Object> map = new HashMap<>();
        User user = userMapper.get(userId);
        map.put("duration", user.getOnceConsultationDuration());

        List<Map<String, Object>> times = userMapper.appointmentTime(userId);
        Map<String, Set<String>> m2 = times.stream()
                .collect(groupingBy(m -> m.get("week").toString(),
                        Collectors.mapping(
                                m -> m.get("code").toString(),
                                Collectors.toSet())));
        map.put("week", m2);

        List<Map<String, Object>> ways = userMapper.consultWay(userId);
        List<String> m3 = ways.stream()
                .map(m -> m.get("way").toString())
                .collect(Collectors.toList());
        map.put("ways", m3);
        return map;
    }

    @Override
    public Page<UserPoints> listUserPoints(int page, int pageSize, Integer userId) {
        PageHelper.startPage(page, pageSize);
        return userMapper.listUserPoints(userId);
    }

    @Override
    public Page<User> topicSpecialists(int page, int pageSize, Integer topicId) {
        PageHelper.startPage(page, pageSize);
        return userMapper.topicSpecialists(topicId);
    }
}
