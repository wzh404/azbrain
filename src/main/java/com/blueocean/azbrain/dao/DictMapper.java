package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.Dict;
import com.blueocean.azbrain.model.Label;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface DictMapper {
    int insert(Dict record);

    Dict get(Integer id);

    /**
     *
     * @param conditionMap
     * @return
     */
    Page<Label> listLabel(Map<String, Object> conditionMap);

    /**
     *
     * @param label
     * @return
     */
    int insertLabel(Label label);

    /**
     *
     * @param label
     * @return
     */
    int update(Label label);
    /**
     *
     * @return
     */
    int changeStatus(@Param("id")Integer id, @Param("status")String status);

    /**
     *
     * @param classify
     * @param code
     * @return
     */
    String maxCode(@Param("classify")String classify, @Param("code")String code);

    Label getLabelByName(@Param("name")String name);
}