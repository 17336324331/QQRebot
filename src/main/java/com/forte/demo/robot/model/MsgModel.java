package com.forte.demo.robot.model;

import lombok.Data;

import java.util.Date;

/**
 * @author 陈瑞扬
 * @date 2019年12月01日 11:28
 * @description 自定义的消息实体
 */
@Data
public class MsgModel {

    // 发送人的QQ
    private String strQQ;

    // 群
    private String strGroup;

    // 发送的消息
    private String senderMsg;

    // 1.私聊 2.群聊
    private String msgType;

    // 信息发送时间
    private Date sendTime;

}
