package com.forte.demo.robot.mapper;

import com.forte.demo.robot.model.NameModel;
import org.apache.ibatis.annotations.Param;

/**
 * @author 陈瑞扬
 * @date 2019年12月13日 23:27
 * @description
 */
public interface NameMapper {

    void saveName(NameModel nameModel);

    void updateName(NameModel nameModel);

    String selectNameByQQGroup(@Param("strQQ")String strQQ,@Param("strGroup")String strGroup);

    NameModel selectByQQGroup();
}
