package com.forte.demo.robot.mapper;

import com.forte.demo.robot.model.InitModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 陈瑞扬
 * @date 2019年12月13日 23:09
 * @description
 */
public interface InitMapper {

    // 存储 即插入一条数据
    void saveInit(InitModel initModel);

    List<InitModel> selectInit();

    void deleteInit(@Param("strQQ")String strQQ, @Param("strGroup")String strGroup);
}
