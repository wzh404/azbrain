package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.AnswerComment;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnswerCommentMapper {
    int insert(AnswerComment record);

    AnswerComment get(@Param("id")Integer id);

    Page<AnswerComment> getAnswerComments(@Param("answerId")Integer answerId);
}