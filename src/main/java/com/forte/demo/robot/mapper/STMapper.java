package com.forte.demo.robot.mapper;

import com.forte.demo.robot.model.STModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 陈瑞扬
 * @date 2019年12月10日 23:05
 * @description
 */
public interface STMapper {

    void insertOne(STModel stModel);

    List<STModel> selectItem(@Param("strQQ")String strQQ, @Param("strGroup")String strGroup);

}
