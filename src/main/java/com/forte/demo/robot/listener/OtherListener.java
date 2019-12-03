package com.forte.demo.robot.listener;

import com.forte.demo.robot.utils.CommandUtil;
import com.forte.demo.robot.utils.PropertiesUtil;
import com.forte.demo.robot.utils.SystemParam;
import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.beans.messages.msgget.GroupMemberIncrease;
import com.forte.qqrobot.beans.messages.msgget.GroupMemberReduce;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.msgget.PrivateMsg;
import com.forte.qqrobot.beans.messages.result.GroupMemberInfo;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 陈瑞扬
 * @date 2019年12月01日 20:55
 * @description 其他事件监听
 */
public class OtherListener {

    private static final Logger logger = LoggerFactory.getLogger("OtherListener");

    // 新人入群事件
    @Listen(MsgGetTypes.groupMemberIncrease)
    public void welcome(GroupMemberIncrease msg, MsgSender sender, CQCodeUtil cqCodeUtil){
        String qq = msg.getQQCode();
        String group = msg.getGroupCode();
        String card = sender.GETTER.getGroupMemberInfo(group, qq).getCard();
        String resultMsg = "" ;
        try {
            // 读取配置文件
            PropertiesUtil props = new PropertiesUtil("param.properties");
            // 获取配置文件中帮助菜单的内容
            resultMsg = props.getProperty("welcome");

        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.toString());
            logger.info("welcome: qq:"+qq+"\tgroup:"+group+"\tcard"+card);
        }

        sender.SENDER.sendGroupMsg(group,cqCodeUtil.getCQCode_At(qq)+" "+resultMsg);

    }


    // 老人退群事件
    @Listen(MsgGetTypes.groupMemberReduce)
    public void goodbye(GroupMemberReduce msg, MsgSender sender, CQCodeUtil cqCodeUtil){
        String qq = msg.getQQCode();
        String beOperatedQQ = msg.getBeOperatedQQ();
        String group = msg.getGroupCode();
        GroupMemberInfo groupMemberInfo = sender.GETTER.getGroupMemberInfo(group, qq);
        String card = groupMemberInfo.getCard();
        card = card == null ? sender.GETTER.getStrangerInfo(qq).getName() : card ;

        String resultMsg = "" ;
        try {
            // 读取配置文件
            PropertiesUtil props = new PropertiesUtil("param.properties");
            // 获取配置文件中帮助菜单的内容
            resultMsg = props.getProperty("goodbye");

        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.toString());
            logger.info("welcome: qq:"+qq+"\tgroup:"+group+"\tcard"+card);
        }

        sender.SENDER.sendGroupMsg(group,card+resultMsg);

    }

    // 机器人开关事件
    @Listen(MsgGetTypes.privateMsg)
    public void switchStatus(PrivateMsg msg, MsgSender sender, CQCodeUtil cqCodeUtil){
        String strMsg = msg.getMsg();
        String qq = msg.getQQ();
        String adminName = CommandUtil.checkAdmin(qq);
        if (strMsg.contains("bot")&& StringUtils.isNotBlank(adminName)){

            String command = strMsg.substring(strMsg.indexOf('t')+2);
            if (command.equals("on")&&!SystemParam.botstatus){
                SystemParam.botstatus = true;
                sender.SENDER.sendPrivateMsg(qq,adminName+",行光前来报到");
            }else if (command.equals("off")&&SystemParam.botstatus){
                SystemParam.botstatus = false;
                sender.SENDER.sendPrivateMsg(qq,adminName+",行光先退下了");
            }else if(command.equals("on")){
                sender.SENDER.sendPrivateMsg(qq,adminName+",行光本来就处于开启状态哦");
            }else if(command.equals("off")){
                sender.SENDER.sendPrivateMsg(qq,adminName+",行光本来就处于关闭状态哦");
            }
        }

        boolean botstatus = SystemParam.botstatus;
    }

    // 机器人开关事件
    @Listen(MsgGetTypes.groupMsg)
    public void switchStatus(GroupMsg msg, MsgSender sender, CQCodeUtil cqCodeUtil){
        String strMsg = msg.getMsg();
        String qq = msg.getQQ();
        String strGroup = msg.getGroup();
        String adminName = CommandUtil.checkAdmin(qq);
        if (strMsg.contains("bot")&& StringUtils.isNotBlank(adminName)){

            String command = strMsg.substring(strMsg.indexOf('t')+2);
            if (command.equals("on")&&!SystemParam.botstatus){
                SystemParam.botstatus = true;
                sender.SENDER.sendGroupMsg(strGroup,adminName+",行光前来报到");
            }else if (command.equals("off")&&SystemParam.botstatus){
                SystemParam.botstatus = false;
                sender.SENDER.sendGroupMsg(strGroup,adminName+",行光先退下了");
            }else if(command.equals("on")){
                sender.SENDER.sendGroupMsg(strGroup,adminName+",行光本来就处于开启状态哦");
            }else if(command.equals("off")){
                sender.SENDER.sendGroupMsg(strGroup,adminName+",行光本来就处于关闭状态哦");
            }
        }

        boolean botstatus = SystemParam.botstatus;
    }

}
