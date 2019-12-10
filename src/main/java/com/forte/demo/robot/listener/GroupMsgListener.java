package com.forte.demo.robot.listener;

import com.forte.demo.robot.mapper.MsgMapper;
import com.forte.demo.robot.mapper.RuleMapper;
import com.forte.demo.robot.mapper.STMapper;
import com.forte.demo.robot.model.MsgModel;
import com.forte.demo.robot.model.RuleModel;
import com.forte.demo.robot.model.STModel;
import com.forte.demo.robot.utils.*;
import com.forte.qqrobot.anno.Ignore;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.result.GroupMemberInfo;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.javassist.compiler.ast.Stmnt;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author 陈瑞扬
 * @date 2019年12月01日 0:49
 * @description 群聊消息监听器
 */
public class GroupMsgListener {

    private static final Logger logger = LoggerFactory.getLogger("OtherListener");

    @Listen(MsgGetTypes.groupMsg)
    public void listen1(GroupMsg msg,  MsgSender sender,CQCodeUtil cqCodeUtil,GroupMemberInfo groupMemberInfo){

        // 获取发言人的QQ号
        String strQQ = msg.getQQ();
        // 获取发言的群
        String strGroup = msg.getGroup();
        // 获取发言人的群昵称
        GroupMemberInfo memberInfo = sender.GETTER.getGroupMemberInfo(strGroup, strQQ, true);
        String card = memberInfo.getCard()==null?memberInfo.getNickName():memberInfo.getCard();

        SqlSession sqlSession = null ;
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            MsgMapper msgMapper = sqlSession.getMapper(MsgMapper.class);
            MsgModel msgModel = new MsgModel();
            msgModel.setMsgType("2");
            msgModel.setStrQQ(strQQ);
            msgModel.setStrGroup(strGroup);
            msgModel.setSenderMsg(msg.getMsg());
            msgMapper.saveMsg(msgModel);
            sqlSession.commit();
        }catch (Exception e){
            e.printStackTrace();
            sqlSession.rollback();

        }




        // 获取群成员发布的消息
        String strMsg = msg.getMsg().trim();
        if (strMsg.contains("at,qq=1730707275")){
            strMsg = strMsg.substring(strMsg.indexOf("]") + 1).trim();
            if (!CommandUtil.checkCommand(strMsg)){
                strMsg = "."+strMsg;
            }
        }

        try {
            // 如果是命令,执行操作
            if(CommandUtil.checkCommand(strMsg)&&SystemParam.botstatus){

                // 帮助指令明细
                if (strMsg.contains("help")&&strMsg.contains("指令")){

                    String resultMsg = LogicUtil.helpzhiling();
                    sender.SENDER.sendGroupMsg(strGroup,resultMsg);

                    logger.info(card+"查看了指令明细:"+strMsg);
                    return ;
                }

                // 帮助指令明细
                if (strMsg.contains("help")&&strMsg.contains("更新")){

                    String resultMsg = LogicUtil.helpgengxin();
                    sender.SENDER.sendGroupMsg(strGroup,resultMsg);

                    logger.info(card+"查看了指令明细:"+strMsg);
                    return ;
                }

                // 帮助指令
                if (strMsg.contains("help")&&(!strMsg.contains("更新"))&&(!strMsg.contains("指令"))){
                    String resultMsg = LogicUtil.help();
                    sender.SENDER.sendGroupMsg(strGroup,resultMsg);

                    logger.info(card+"查看了指令明细:"+strMsg);
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

                // 随即姓名

                if (strMsg.contains("name")){
                    String resultMsg = NameUtil.getName();
                    addNameSendMsg(strGroup, strQQ,"的随机名称:\n"+resultMsg,sender);

//                    /sender.SENDER.sendGroupMsg(strGroup,card+","+resultMsg);
                    logger.info(card+"生成了随机名字:"+strMsg);
                    return ;
                }
                // 技能鉴定
                if (strMsg.contains("ra")||strMsg.contains("rc")){
                    String resultMsg = LogicUtil.ra(strMsg);
                    if (resultMsg.contains("*")){
                        //String[] split = resultMsg.split("\\*");
                        addNameSendMsg(strGroup, strQQ,resultMsg.replace('*','\n'),sender);
                        //sender.SENDER.sendGroupMsg(strGroup,split[1]);

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
                if (strMsg.contains("rhd")){
                    String resultMsg = LogicUtil.rhd(strMsg);
                    String reason = strMsg.substring(strMsg.indexOf('d'));
                    addNameSendMsg(strGroup,strQQ, "进行了一次暗骰",sender);
                    if (StringUtils.isNotBlank(reason)){
                        sender.SENDER.sendPrivateMsg(strQQ,"在群"+strGroup+"中由于"+reason+"骰出了"+resultMsg);
                    }else{
                        sender.SENDER.sendPrivateMsg(strQQ,"在群"+strGroup+"中骰出了"+resultMsg);
                    }

                    return ;
                }


                if (strMsg.contains("st")&&(!strMsg.contains("show"))) {
                    String str = strMsg.substring(4).trim();
                    String score= "";
                    int endIndex = -1 ;
                    for(int i=0;i<str.length();i++) {
                        if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                            if (endIndex == -1 ){
                                endIndex = i;
                            }
                            score += str.substring(i,i+1);
                        }
                    }

                    String name = str.substring(0,endIndex);
                    try {
                        STMapper stMapper = sqlSession.getMapper(STMapper.class);

                        STModel stModel = new STModel();

                        stModel.setStName(name);
                        stModel.setStScore(score);
                        stModel.setStrQQ(strQQ);
                        stModel.setStrGroup(strGroup);

                        stMapper.insertOne(stModel);
                        sqlSession.commit();
                        String st = LogicUtil.st(strMsg);
                        sender.SENDER.sendGroupMsg(strGroup, st);
                        return;
                    } catch (Exception e) {
                        sqlSession.rollback();
                        e.printStackTrace();
                        sender.SENDER.sendGroupMsg(strGroup,LogicUtil.getErrorMsg());
                        return;
                    }
                }

                if (strMsg.contains("st")&&strMsg.contains("show")) {
                    try {
                        STMapper stMapper = sqlSession.getMapper(STMapper.class);
                        List<STModel> stModelList = stMapper.selectItem(strQQ,strGroup);

                        String resultMsg = card+"的属性列表: \n";

                        for (int i = 0; i < stModelList.size(); i++) {
                            STModel stModel = stModelList.get(i);
                            if (i!=stModelList.size()-1){
                                resultMsg+=stModel.getStName()+":"+stModel.getStScore()+"\n";
                            }else{
                                resultMsg+=stModel.getStName()+":"+stModel.getStScore();
                            }

                        }

                        sender.SENDER.sendGroupMsg(strGroup, resultMsg);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.SENDER.sendGroupMsg(strGroup,LogicUtil.getErrorMsg());
                        return;
                    }
                }

                if (strMsg.contains("rules")){
                    String strName = strMsg.substring(strMsg.indexOf('s')+1).trim();
                    try {
                        RuleMapper ruleDao = sqlSession.getMapper(RuleMapper.class);
                        RuleModel ruleModel = ruleDao.selectContentByName(strName);

                        sender.SENDER.sendGroupMsg(strGroup,ruleModel.getStrContent());
                        return ;

                    }catch (Exception e){
                        e.printStackTrace();
                        sender.SENDER.sendGroupMsg(strGroup,LogicUtil.getErrorMsg());
                        return;
                    }
                }

                if (strMsg.contains("summon")) {
                    //SqlSession sqlSession = null;
                    String strName = strMsg.substring(strMsg.indexOf('n')+1).trim();
                    try {
                        //sqlSession = SqlSessionFactoryUtil.openSqlSession();
                        RuleMapper ruleDao = sqlSession.getMapper(RuleMapper.class);
                        RuleModel ruleModel = ruleDao.selectAllByName(strName);
                        ruleModel = LogicUtil.createModelByRule(ruleModel);
                        sender.SENDER.sendGroupMsg(strGroup,ruleModel.toString());

                        return ;


                    }catch (Exception e){
                        e.printStackTrace();
                        sender.SENDER.sendGroupMsg(strGroup,LogicUtil.getErrorMsg());
                        return;
                    }


                }




                // 8.禁言指令
                if (strMsg.contains("group")&&strMsg.contains("ban")){

                    sender.SETTER.setGroupWholeBan(strGroup, false);
                    return ;
                }

                // 8.退群指令
                if (strMsg.contains("dismiss")){
                    //String resultMsg = LogicUtil.dismiss(strMsg);
                    sender.SETTER.setGroupLeave(strGroup);
                    return ;
                }

                // 9.人物作成
                if (strMsg.contains("coc")){
                    String coc = LogicUtil.coc(strMsg);
                    addNameSendMsg(strGroup,strQQ,coc,sender);
                    return ;
                }

                // 9.人物作成
                if (strMsg.contains("dnd")){
                    String coc = LogicUtil.dnd(strMsg);
                    addNameSendMsg(strGroup,strQQ,coc,sender);
                    return ;
                }

                // 10.向master发送消息
                if (strMsg.contains("send")){
                    sender.SENDER.sendPrivateMsg("1571650839", strMsg);
                    return ;
                }

                // 4.普通投掷
                if (strMsg.contains("r")){
                    String resultMsg = LogicUtil.r(strMsg);
                    addNameSendMsg(strGroup,strQQ, resultMsg,sender);
                    return ;
                }

                if (strMsg.contains("晚安")){
                    //String resultMsg = LogicUtil.r(strMsg);
                    //cqCodeUtil.getCQCode(CQCodeTypes.emoji,)
                    addNameSendMsg(strGroup,strQQ, "晚安",sender);
                    return ;
                }
                if (strMsg.contains("早上好")){
                    //String resultMsg = LogicUtil.r(strMsg);
                    //cqCodeUtil.getCQCode(CQCodeTypes.emoji,)
                    //cqCodeUtil.getCQCode_Face("176");
                    addNameSendMsg(strGroup,strQQ, "早上好",sender);
                    return ;
                }
                if (strMsg.contains("会发表情吗")){
                    addNameSendMsg(strGroup,strQQ, " 你这是在刁难我"+cqCodeUtil.getCQCode_Face("176"),sender);
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
            //resultMsg= resultMsg.replace("xxx",adminName);
            resultMsg = adminName + resultMsg;
        }else{
            GroupMemberInfo memberInfo = sender.GETTER.getGroupMemberInfo(strGroup, strQQ, true);
            String card = memberInfo.getCard()==null?memberInfo.getNickName():memberInfo.getCard();
            resultMsg = card + resultMsg;
        }
        sender.SENDER.sendGroupMsg(strGroup,resultMsg);

    }






}
