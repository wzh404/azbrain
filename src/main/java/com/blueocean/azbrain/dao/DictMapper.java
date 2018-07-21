package com.blueocean.azbrain.dao;

import com.blueocean.azbrain.model.Dict;
import com.blueocean.azbrain.model.Label;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DictMapper {
    int insert(Dict record);

    Dict get(Integer id);

    /**
     *
     * @param classify
     * @return
     */
    Page<Label> listLabel(@Param("classify")String classify);

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
}