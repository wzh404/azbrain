package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.ConsultationComment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConsultationCommentMapper {

    int insert(ConsultationComment record);

    ConsultationComment get(Integer id);
}