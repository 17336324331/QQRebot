package com.forte.demo.robot.mapper;

import com.forte.demo.robot.model.MsgModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 陈瑞扬
 * @date 2019年12月08日 10:32
 * @description
 */
public interface MsgMapper {

    void saveMsg(MsgModel msgModel);

    List<MsgModel> selectRepeat(@Param("strGroup")String strGroup,@Param("num") Integer num);
}
