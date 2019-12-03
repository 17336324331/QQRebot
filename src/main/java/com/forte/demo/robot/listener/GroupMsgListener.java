package com.forte.demo.robot.listener;

import com.forte.demo.robot.utils.CommandUtil;
import com.forte.demo.robot.utils.LogicUtil;
import com.forte.demo.robot.utils.SystemParam;
import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Ignore;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.result.GroupMemberInfo;
import com.forte.qqrobot.beans.messages.result.StrangerInfo;
import com.forte.qqrobot.beans.messages.types.GroupMsgType;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 陈瑞扬
 * @date 2019年12月01日 0:49
 * @description 群聊消息监听器
 */
public class GroupMsgListener {

    private static final Logger logger = LoggerFactory.getLogger("OtherListener");

    @Listen(MsgGetTypes.groupMsg)
    public void listen1(GroupMsg msg,  MsgSender sender,CQCodeUtil cqCodeUtil,GroupMemberInfo groupMemberInfo){
        // 获取群成员发布的消息
        String strMsg = msg.getMsg();
        // 获取发言人的QQ号
        String strQQ = msg.getQQ();
        // 获取发言的群
        String strGroup = msg.getGroup();
        // 获取发言人的群昵称
        GroupMemberInfo memberInfo = sender.GETTER.getGroupMemberInfo(strGroup, strQQ, true);
        String card = memberInfo.getCard()==null?memberInfo.getNickName():memberInfo.getCard();

        try {
            // 如果是命令,执行操作
            if(CommandUtil.checkCommand(strMsg)&&SystemParam.botstatus){

                // 帮助指令
                if (strMsg.contains("help指令")){
                    String resultMsg = LogicUtil.helpzhiling();
                    sender.SENDER.sendGroupMsg(strGroup,resultMsg);
                    return ;
                }

                // 帮助指令
                if (strMsg.contains("help")){
                    String resultMsg = LogicUtil.help();
                    sender.SENDER.sendGroupMsg(strGroup,resultMsg);
                    return ;
                }

                // 今日人品
                if (strMsg.contains("jrrp")){
                    String resultMsg = LogicUtil.jrrp();
                    addNameSendMsg(strGroup, strQQ,resultMsg,sender);

//                    /sender.SENDER.sendGroupMsg(strGroup,card+","+resultMsg);
                    logger.info(card+"查看了今日人品:"+strMsg);
                    return ;
                }

                // 技能鉴定
                if (strMsg.contains("ra")||strMsg.contains("rc")){
                    String resultMsg = LogicUtil.ra(strMsg);
                    if (resultMsg.contains("*")){
                        String[] split = resultMsg.split("\\*");
                        addNameSendMsg(strGroup, strQQ,split[0],sender);
                        sender.SENDER.sendGroupMsg(strGroup,split[1]);

                    }else {
                        addNameSendMsg(strGroup, strQQ,resultMsg,sender);
                        logger.info(card+":"+strMsg);
                    }

                    return ;
                }

                // 惩罚骰
                if (strMsg.contains("rp")){
                    String resultMsg = LogicUtil.rp(strMsg);
                    if (resultMsg.contains("*")){
                        String[] split = resultMsg.split("\\*");
                        addNameSendMsg(strGroup, strQQ,split[0]+split[1],sender);
                        //sender.SENDER.sendGroupMsg(strGroup,split[1]);

                    }else {
                        addNameSendMsg(strGroup, strQQ,resultMsg,sender);
                        logger.info(card+":"+strMsg);
                    }

                    return ;
                }

                // 奖励骰
                if (strMsg.contains("rb")){
                    String resultMsg = LogicUtil.rb(strMsg);
                    if (resultMsg.contains("*")){
                        String[] split = resultMsg.split("\\*");
                        addNameSendMsg(strGroup, strQQ,split[0]+split[1],sender);
                        //sender.SENDER.sendGroupMsg(strGroup,split[1]);

                    }else {
                        addNameSendMsg(strGroup, strQQ,resultMsg,sender);
                        logger.info(card+":"+strMsg);
                    }

                    return ;
                }



                // 4.普通投掷
                if (strMsg.contains("r")){
                    String resultMsg = LogicUtil.r(strMsg);
                    addNameSendMsg(strGroup,strQQ, resultMsg,sender);
                }

                // 10.向master发送消息
                if (strMsg.contains("send")){
                    sender.SENDER.sendPrivateMsg(strQQ, strMsg);
                }

            }
            // 如果不是命令,日志存储
            else {
                logger.info(card+"说:"+strMsg);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.toString());
            logger.info("strMsg:"+strMsg+"\tstrQQ:"+strQQ+"\tstrGroup"+strGroup);
        }

    }



    /**
     * @date 2019/12/1 15:04
     * @author 陈瑞扬
     * @description 校验是否管理员,并按照指定内容发送
     * @param
     * @return
     */
    @Ignore
    public void addNameSendMsg(String strGroup,String strQQ, String resultMsg, MsgSender sender){
        // 获取发言人对应的管理员名称,没有则为null
        String adminName = CommandUtil.checkAdmin(strQQ);
        if(StringUtils.isNotBlank(adminName)){
            resultMsg = adminName + resultMsg;
        }else{
            GroupMemberInfo memberInfo = sender.GETTER.getGroupMemberInfo(strGroup, strQQ, true);
            String card = memberInfo.getCard()==null?memberInfo.getNickName():memberInfo.getCard();
            resultMsg = card + resultMsg;
        }
        sender.SENDER.sendGroupMsg(strGroup,resultMsg);

    }






}
