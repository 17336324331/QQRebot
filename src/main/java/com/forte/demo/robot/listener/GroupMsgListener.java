package com.forte.demo.robot.listener;

import com.forte.demo.robot.mapper.*;
import com.forte.demo.robot.model.*;
import com.forte.demo.robot.utils.*;
import com.forte.qqrobot.anno.Ignore;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.depend.Depend;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.result.GroupMemberInfo;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;

/**
 * @author 陈瑞扬
 * @date 2019年12月01日 0:49
 * @description 群聊消息监听器
 */
public class GroupMsgListener {

    private static final Logger logger = LoggerFactory.getLogger(GroupMsgListener.class);

    @Depend
    SqlSession sqlSession = SqlSessionFactoryUtil.openSqlSession();


    @Listen(MsgGetTypes.groupMsg)
    public void listen1(GroupMsg msg,  MsgSender sender,CQCodeUtil cqCodeUtil,GroupMemberInfo groupMemberInfo){
        //SystemCodeMapper systemCodeMapperxxx = sqlSession.getMapper(SystemCodeMapper.class);

        //SqlSession sqlSession = SqlSessionFactoryUtil.openSqlSession();


        // 获取发言人的QQ号
        String strQQ = msg.getQQ();
        // 获取发言的群
        String strGroup = msg.getGroup();
        // 获取发言人的群昵称
        GroupMemberInfo memberInfo = sender.GETTER.getGroupMemberInfo(strGroup, strQQ, true);
        String strCard = memberInfo.getCard();
        String strNickName = memberInfo.getNickName();
        String card = strCard==null?strNickName:strCard;


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

                // 欢迎词修改
                if (strMsg.contains("welcome")&&StringUtils.isNotBlank(CommandUtil.checkAdmin(strQQ))){
                    SystemCodeMapper systemCodeMapper = sqlSession.getMapper(SystemCodeMapper.class);
                    SystemCodeModel codeModel = new SystemCodeModel();
                    codeModel.setStrCode("welcome");
                    codeModel.setStrValue(strMsg.substring(9).trim());
                    systemCodeMapper.saveSystemCode(codeModel);
                    sqlSession.commit();

                    sender.SENDER.sendGroupMsg(strGroup,"行光已经更新本群的入群欢迎词√");
                    logger.info(card+"更新本群的入群欢迎词√");
                    return ;
                }

                // 随即姓名

                if (strMsg.contains("name")){
                    Integer fori = 0;
                    int length = strMsg.length();
                    String resultMsg = "";
                    if(  strMsg.indexOf('e')+1 < length){
                        fori = Integer.valueOf(strMsg.substring(strMsg.indexOf("e")+1 ).trim());
                    }
                    for (int i = 0; i < fori; i++) {
                        resultMsg += NameUtil.getName();
                    }

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

                // 先攻骰
                if (strMsg.contains("ri")){

                    InitMapper mapper = sqlSession.getMapper(InitMapper.class);
                    mapper.deleteInit(strQQ,strGroup);

                    String resultMsg = LogicUtil.ri(strMsg);
                    if (resultMsg.contains("=")){
                        String score = resultMsg.substring(resultMsg.indexOf("=")+1);
                        InitModel initModel = new InitModel();
                        initModel.setStrQQ(strQQ);
                        initModel.setStrGroup(strGroup);
                        initModel.setIntScore(Integer.valueOf(score));

                        InitMapper initMapper = sqlSession.getMapper(InitMapper.class);
                        initMapper.saveInit(initModel);
                        sqlSession.commit();
                        if (resultMsg.charAt(0) == '*'){
                            addNameSendMsg(strGroup, strQQ,resultMsg.substring(1),sender);
                        }else{
                            sender.SENDER.sendGroupMsg(strGroup,resultMsg);
                        }



                    }else {
                        addNameSendMsg(strGroup, strQQ,resultMsg,sender);
                        logger.info(card+":"+strMsg);
                    }

                    return ;
                }

                // 先攻列表
                if (strMsg.contains("init")){
                    InitMapper mapper = sqlSession.getMapper(InitMapper.class);
                    List<InitModel> inits = mapper.selectInit();
                    String result = "先攻顺序:\n";
                    for (int i = 0; i < inits.size(); i++) {
                        InitModel initModel = inits.get(i);
                        String tempName = initModel.getStrName();
                        Integer intScore = initModel.getIntScore();
                        result+=(i+1+". "+tempName+":"+intScore+"\n");
                    }
                    sender.SENDER.sendGroupMsg(strGroup,result);

                }

                if (strMsg.contains("nn")){
                    NameMapper nameMapper = sqlSession.getMapper(NameMapper.class);

                    String oldName = nameMapper.selectNameByQQGroup(strQQ, strGroup);

                    NameModel nameModel = new NameModel();
                    nameModel.setStrQQ(strQQ);
                    nameModel.setStrGroup(strGroup);
                    nameModel.setStrCard(strCard);
                    nameModel.setStrNickName(strNickName);
                    strMsg = strMsg.replaceFirst("n", "*");
                    String nname= strMsg.substring(strMsg.indexOf('n') + 1).trim();
                    nameModel.setStrNName(nname);
                    nameMapper.saveName(nameModel);
                    sqlSession.commit();

                    String resultMsg = "已将"+oldName==null?card:oldName+"的名称更改为"+nname;
                    sender.SENDER.sendGroupMsg(strGroup,resultMsg);

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
                if (strMsg.substring(1).trim().substring(0,1).equals("r")){
                    String resultMsg = LogicUtil.r(strMsg);
                    addNameSendMsg(strGroup,strQQ, resultMsg,sender);
                    return ;
                }

                // 4.成长鉴定
                if (strMsg.substring(1).trim().substring(0,2).equals("en")){
                    String resultMsg = LogicUtil.en(strMsg);
                    addNameSendMsg(strGroup,strQQ, resultMsg,sender);
                    return ;
                }

                if (strMsg.substring(1).trim().substring(0,1).equals("n")){

                    NameMapper nameMapper = sqlSession.getMapper(NameMapper.class);

                    String oldName = nameMapper.selectNameByQQGroup(strQQ, strGroup);

                    NameModel nameModel = new NameModel();
                    nameModel.setStrQQ(strQQ);
                    nameModel.setStrGroup(strGroup);
                    nameModel.setStrCard(strCard);
                    nameModel.setStrNickName(strNickName);
//                    strMsg = strMsg.replaceFirst("n", "*");
                    String nname= strMsg.substring(strMsg.indexOf('n') + 1).trim();
                    nameModel.setStrNName(nname);
                    nameMapper.saveName(nameModel);
                    sqlSession.commit();

                    String resultMsg = "已将"+(oldName==null?card:oldName)+"的名称更改为"+nname;
                    sender.SENDER.sendGroupMsg(strGroup,resultMsg);

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
                    return;
                }

            }
            // 如果不是命令,日志存储
            else {
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
                    return;
                }
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


    @Listen(MsgGetTypes.groupMsg)
    public void listenRepeat(GroupMsg msg,  MsgSender sender,CQCodeUtil cqCodeUtil,GroupMemberInfo groupMemberInfo) {
        // 获取发言人的QQ号
        String strQQ = msg.getQQ();
        // 获取发言的群
        String strGroup = msg.getGroup();
        // 获取消息
        String strMSg = msg.getMsg().trim();
        SqlSession sqlSession = null ;
        MsgMapper msgMapper = null ;
        try {


            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            msgMapper = sqlSession.getMapper(MsgMapper.class);
            List<MsgModel> msgModels = msgMapper.selectRepeat(strGroup, 2);
            boolean repeatFlag = true;
            boolean repeatPersonFlag = true;
            if (msgModels.size()==2){
                for (int i = 0; i < msgModels.size(); i++) {
                    MsgModel currentModel = msgModels.get(i);
                    if(!strMSg.equals(currentModel.getStrMsg())){
                        repeatFlag = false;
                    }
                }
                if (repeatFlag){
                    // 说明重复
                    for (int i = 1; i < msgModels.size(); i++) {
                        MsgModel currentModel = msgModels.get(i);
                        if(!strQQ.equals(currentModel.getStrQQ())){
                            repeatPersonFlag = false;
                        }
                    }
                    if (!repeatPersonFlag){
                        // 说明是三个不同的人发布的消息
                        //SendUtil.sendGroupMsg(sender,strGroup,msg.getMsg());
                        sender.SENDER.sendGroupMsg(strGroup,msg.getMsg());
                    }else{
                        //SendUtil.sendGroupMsg(sender,strGroup,cqCodeUtil.getCQCode_At(strQQ)+"刷屏警告");
                        sender.SENDER.sendGroupMsg(strGroup,cqCodeUtil.getCQCode_At(strQQ)+"刷屏警告");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            sqlSession.rollback();
        }

        try {
            MsgModel msgModel = new MsgModel();
            msgModel.setIntMsgType(2);
            msgModel.setStrQQ(strQQ);
            msgModel.setStrGroup(strGroup);
            msgModel.setStrMsg(msg.getMsg());
            msgMapper.saveMsg(msgModel);
            sqlSession.commit();
        }catch (Exception e){
            e.printStackTrace();
            sqlSession.rollback();

        }



    }






}
