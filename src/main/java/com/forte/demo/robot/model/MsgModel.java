package com.forte.demo.robot.model;

import lombok.Data;

import java.util.Date;

/**
 * @author 陈瑞扬
 * @date 2019年12月01日 11:28
 * @description 自定义的消息实体
 */
@Data
public class MsgModel extends CommonModel{

    /**
     *   发送的消息的内容
     */
    private String strMsg;

    /**
     *    发送的 消息的类别 1.私聊 2.群聊
     */
    private Integer intMsgType;


}
