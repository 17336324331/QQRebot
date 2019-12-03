package com.forte.demo.robot.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author 陈瑞扬
 * @date 2019年12月01日 11:51
 * @description 逻辑处理
 */
public class LogicUtil {

    private static final Logger logger = LoggerFactory.getLogger("LogicUtil");

    // 今日人品
    public static String jrrp() throws Exception {

        // 读取配置文件
        PropertiesUtil props = new PropertiesUtil("param.properties");
        // 获取1-100随机数
        int intJrrp = getRandom(100);
        // 今日人品前缀
        String resultMsg = props.getProperty("jrrp_prefix");
        String renpinMessage = props.getProperty("renpinMessage");

        // 今日人品分数
        resultMsg += intJrrp;
//        // 今日人品后缀
//        if (intJrrp >= 90){
//            resultMsg += props.getProperty("jrrp_unlucky");
//        }else if (intJrrp >= 40 ){
//            resultMsg += props.getProperty("jrrp_suffix");
//        }else {
//            resultMsg += props.getProperty("jrrp_lucky");
//        }

        return resultMsg+renpinMessage;
        //+"\n"
    }

    // 查看帮助
    public static String helpzhiling() throws Exception {
        // 读取配置文件
        PropertiesUtil props = new PropertiesUtil("param.properties");
        // 获取配置文件中帮助菜单的内容
        String resultMsg = props.getProperty("helpzhingling");

        return resultMsg;
    }

    // 查看帮助
    public static String help() throws Exception {
        // 读取配置文件
        PropertiesUtil props = new PropertiesUtil("param.properties");
        // 获取配置文件中帮助菜单的内容
        String resultMsg = props.getProperty("help");

        return resultMsg;
    }

    // 技能鉴定
    public static String ra(String strMsg){
        // 只有长度大于3 才是正常命令  (.ra谈判 1 )
        if (strMsg.trim().length() >3){
            // 获取 命令和分数  (谈判 1)
            String strCommand = strMsg.substring(strMsg.indexOf('a')+1).trim();
            // 如果包含 英文空格就以英文空格断开
            String[] arrCommand = strCommand.split("\\s+");
            return "进行"+arrCommand[0]+"检定:"+skillcheck(arrCommand[1]);
        }
        // 命令不正确时返回""
        else{
            return  "";
        }

    }

    // 惩罚骰
    public static String rp(String strMsg){
        // 只有长度大于3 才是正常命令  (.ra谈判 1 )
        if (strMsg.trim().length() >=3){
            String strP = strMsg.substring(strMsg.indexOf('p')+1).trim();
            int random100 = getRandom(100);
            int random10 = getRandom(10);
            String strSesult = "";
            if (random100<10&&random10<10){
                if (random100>=random10){
                    strSesult += random100;
                    strSesult += random10;
                }else{
                    strSesult += random10;
                    strSesult += random100;

                }

            }else{
                if (random100>=random10){
                    strSesult += random100;

                }else{
                    strSesult += random10;
                }

            }

            if (StringUtils.isNotBlank(strP)){
                return  "由于 "+ strP +" 骰出了:P="+random100+"[惩罚骰:"+random10+"]="+strSesult;
            }else{
                return  "骰出了:P="+random100+"[惩罚骰:"+random10+"]="+strSesult;
            }
        }
        // 命令不正确时返回""
        else{
            return  "";
        }

    }

    // 奖励骰
    public static String rb(String strMsg){
        // 只有长度大于3 才是正常命令  (.ra谈判 1 )
        if (strMsg.trim().length() >=3){
            String strB = strMsg.substring(strMsg.indexOf('b')+1).trim();
            int random100 = getRandom(99);
            int random10 = getRandom(9);
            String strSesult = "";
            if (random100<10){
                if (random100>=random10){
                    strSesult += random10;
                }else{
                    strSesult += random10;

                }
            }else{
                if (random100/10>=random10){
                    strSesult += random10;
                    strSesult +=random100%10;

                }else{
                    strSesult += random100;
                }

            }

            if (StringUtils.isNotBlank(strB)){
                return  "由于 "+ strB +" 骰出了:P="+random100+"[奖励骰:"+random10+"]="+strSesult;
            }else{
                return  "骰出了:P="+random100+"[奖励骰:"+random10+"]="+strSesult;
            }
        }
        // 命令不正确时返回""
        else{
            return  "";
        }

    }


    // 普通投掷
    public static String r(String strMsg){

        int rIndex = strMsg.indexOf("r");
        int inta = strMsg.indexOf("+");
        int intb = strMsg.indexOf("*");
        String substring = "";
        if (inta==-1&&intb==-1){
             substring = strMsg.substring(rIndex+1);
        }else if (inta!=-1&&intb!=-1){
            if (inta<intb){
                substring = strMsg.substring(rIndex+1,inta);
            }else{
                substring = strMsg.substring(rIndex+1,intb);
            }
        }else{
            if (inta<intb){
                substring = strMsg.substring(rIndex+1,intb);
            }else{
                substring = strMsg.substring(rIndex+1,inta);
            }
        }

        if (substring.contains("d")){
            List<Integer> intList = new ArrayList<Integer>();
            String str = null;
            String[] arrNum = substring.split("d");
            // 投掷多少次
            int num = Integer.valueOf(arrNum[0]);
            for (int i = 0; i < num; i++) {
                // 几个面的骰子
                Integer face = Integer.valueOf(arrNum[1]);
                int intResult = (int) (Math.random() * (face-1) + 1);
                intList.add(intResult);
            }

            String strSum = "";
            for (Object o : intList) {
                strSum+="+"+o.toString();
            }

            Integer sum = 0;
            for (Integer o : intList) {
                sum+=o;
            }

            return "骰出了"+substring.toUpperCase()+" : "+strSum.substring(1)+"="+sum;
        }else{
            // 获取100以内随机数
            int randomSocre = getRandom(100);
            // 拼接返回语句
            String strResultMsg = "D100="+randomSocre;
            return "骰出了"+strResultMsg;
        }

    }



    // 向master发送消息
    public static String send(String strMsg){
        // 只有长度大于3 才是正常命令  (.ra谈判 1    .rc 逃跑 3)
        if (strMsg.trim().length() >5){
            // 获取 命令和分数  (谈判 1)
            String strCommand = strMsg.substring(5).trim();
            // 如果包含 英文空格就以英文空格断开
            if (strCommand.contains(" ")){
                String[] arrCommand = strCommand.split(" ");
                return "进行"+arrCommand[0]+"检定:"+skillcheck(arrCommand[1]);
            }
            // 如果包含 中文空格就以中文空格断开
            else if(strCommand.contains("　")){
                String[] arrCommand = strCommand.split(" ");
                return "进行"+arrCommand[0]+"检定:"+skillcheck(arrCommand[1]);
            }else {
                return "";
            }

        }
        // 命令不正确时返回""
        else{
            return  "";
        }

    }



    // 获取指定范围的随机数
    public static int getRandom(int intRandom){
        Double douJrrp = Math.random() * (intRandom+1);
        int intJrrp = douJrrp.intValue();
        return intJrrp;

    }

    // 技能鉴定结果 true|false
    public static String skillcheck(String score){
        int skillSocre = Integer.valueOf(score);
        int randomSocre = getRandom(100);
        String strResultMsg = "D100="+randomSocre+"/"+skillSocre;
        PropertiesUtil props = null;
        // 读取配置文件
        try {
            props = new PropertiesUtil("param.properties");
        }catch (Exception e){
            logger.info(e+"");
        }

        // bigFail
        if ( randomSocre > 95 ){
            strResultMsg += props.getProperty("bigFail");
            return strResultMsg;
        }
        // bigSuccess
        else if ( randomSocre < 5){
            strResultMsg += props.getProperty("bigSuccess");
            return strResultMsg;
        }
        //
        else if( skillSocre >= randomSocre*4 ){
            strResultMsg += props.getProperty("bigDiffcult");
            return strResultMsg;

        }
        //
        else if( skillSocre >= randomSocre*2 ){
            strResultMsg += props.getProperty("diffcult");
            return strResultMsg;
        }
        //
        else if( skillSocre >= randomSocre ){
            strResultMsg += props.getProperty("success");
            return strResultMsg;
        }
        //
        else if( skillSocre < randomSocre ){
            strResultMsg += props.getProperty("fail");
            return strResultMsg;
        }else {
            return strResultMsg;
        }

    }

//    public static void main(String[] args) {
//        try {
//            while (true){
//                System.out.println(jrrp());
//            }
//        }catch (Exception e){
//
//        }
//
//
//    }


}
