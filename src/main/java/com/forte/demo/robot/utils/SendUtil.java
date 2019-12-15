package com.forte.demo.robot.utils;

import com.forte.demo.robot.mapper.MsgMapper;
import com.forte.demo.robot.model.MsgModel;
import com.forte.qqrobot.sender.MsgSender;
import org.apache.ibatis.session.SqlSession;

/**
 * @author 陈瑞扬
 * @date 2019年12月15日 21:25
 * @description 发送信息工具类
 */
public class SendUtil {



    public static void sendGroupMsg(MsgSender sender,String strGroup, String strMsg){
        SqlSession sqlSession = SqlSessionFactoryUtil.openSqlSession();
        MsgMapper mapper = sqlSession.getMapper(MsgMapper.class);
        MsgModel msgModel = new MsgModel();
        ///mapper.saveMsg(strMsg);

        sender.SENDER.sendGroupMsg(strGroup,strMsg);

    }
}
