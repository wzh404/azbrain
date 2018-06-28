package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.Dict;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DictMapper {
    int insert(Dict record);

    Dict get(Integer id);
}