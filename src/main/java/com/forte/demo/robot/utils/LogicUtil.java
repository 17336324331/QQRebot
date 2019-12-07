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

    // 查看更新
    public static String helpgengxin() throws Exception {
        // 读取配置文件
       // PropertiesUtil props = new PropertiesUtil("param.properties");
        // 获取配置文件中帮助菜单的内容
        //String resultMsg = props.getProperty("helpzhingling");
        String resultMsg = "1.修复了@不生效的bug\n2.添加了dismiss退群指令\n3.添加了coc/dnd人物作成\n4.添加了群员禁言\n5.完善了bot和help菜单\n6.技能判定两行合并为1行";

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
    public static String rhd(String strMsg) {


            return getRandom(100)+"";



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

        substring = substring.trim();

        if (substring.contains("d")){
            List<Integer> intList = new ArrayList<Integer>();
            String str = null;
            if (substring.indexOf("d")==0){
                String strFace = substring.substring(1).trim();
                Integer face = Integer.valueOf(Integer.valueOf(strFace));
                int random = getRandom(face+1);
                return "骰出了"+substring.toUpperCase()+" = "+random;

            }else{

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

                // 没有乘号mei'yo没有减号
                if (inta==-1&&intb==-1){
                    substring = strMsg.substring(rIndex+1);
                    return "骰出了"+substring.toUpperCase()+" : "+strSum.substring(1)+"="+sum;
                }
                //有乘号并且有减号
                else if (inta!=-1&&intb!=-1){
                    // 先加再乘
                    if (inta<intb){
                        Integer factora = Integer.valueOf(strMsg.substring(inta+1,intb).trim());
                        Integer factorb = Integer.valueOf(strMsg.substring(intb+1).trim());
                        substring = strMsg.substring(rIndex+1,inta);
                        return "骰出了"+substring.toUpperCase()+" : ("+strSum.substring(1)+")+"+factora+"*"+factorb+"="+(sum+factorb*factora);
                    }
                    // 先乘再加
                    else{
                        // a 是 乘数
                        Integer factora = Integer.valueOf(strMsg.substring(intb+1,inta).trim());
                        // b 是 加数
                        Integer factorb = Integer.valueOf(strMsg.substring(inta).trim());
                        substring = strMsg.substring(rIndex+1,intb);
                        return "骰出了"+substring.toUpperCase()+" : ("+strSum.substring(1)+")*"+factora+"+"+factorb+"="+(sum*factora+factorb);
                    }
                }
                // 有其中一个
                else{
                    // 有乘号
                    if (inta<intb){
                        Integer factor = Integer.valueOf(strMsg.substring(intb+1).trim());
                        substring = strMsg.substring(rIndex+1,intb);
                        return "骰出了"+substring.toUpperCase()+" : ("+strSum.substring(1)+")*"+factor+"="+sum*factor;
                    }
                    // 有加号
                    else{
                        Integer factor = Integer.valueOf(strMsg.substring(inta+1).trim());
                        substring = strMsg.substring(rIndex+1,inta);
                        return "骰出了"+substring.toUpperCase()+" : ("+strSum.substring(1)+")+"+factor+"="+(+sum+factor);
                    }
                }

                //return "骰出了"+substring.toUpperCase()+" : "+strSum.substring(1)+"="+sum;

            }

        }else{
            // 获取100以内随机数
            int randomSocre = getRandom(100);
            // 拼接返回语句
            String strResultMsg = "D100="+randomSocre;
            return "骰出了"+strResultMsg;
        }

    }

    public static String coc(String strMsg){
        int times = 1 ;

        try {
            String str = strMsg.trim();
            String str2="";
            if(StringUtils.isNotBlank(strMsg)){
                for(int i=0;i<str.length();i++) {
                    if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                        str2 += str.charAt(i);
                    }
                }
            }
            times = Integer.valueOf(str2);

        }catch (Exception e){
            logger.info("错误指令:"+strMsg);
        }

        StringBuffer sb = new StringBuffer();
        sb.append("的人物作成:\n");
        for (int i=0;i<times;i++){
            StringBuffer sbtemp = new StringBuffer();

            int liliang = getRandom(100);
            int tizhi = getRandom(100);
            int tixing =getRandom(100);
            int minjie =getRandom(100);

            int waimao = getRandom(100);
            int zhili = getRandom(100);
            int yizhi =getRandom(100);
            int jiaoyu =getRandom(100);

            int xingyun =getRandom(100);

            int total = liliang+tizhi+tixing+minjie+waimao+zhili+yizhi+jiaoyu;
            int total2 = total+xingyun;

            sbtemp.append("力量:"+liliang);
            sbtemp.append(" 体质:"+tizhi);
            sbtemp.append(" 体型:"+tixing);
            sbtemp.append(" 敏捷:"+minjie);

            sbtemp.append(" 外貌:"+waimao);
            sbtemp.append(" 智力:"+zhili);
            sbtemp.append(" 意志:"+yizhi);
            sbtemp.append(" 教育:"+jiaoyu);

            sbtemp.append(" 幸运:"+xingyun);

            sbtemp.append(" 共计:"+total +"/"+total2);
            if (i<times-1){
                sbtemp.append("\n");
            }
            sb.append(sbtemp);
        }
        return sb.toString();
    }


    public static String dnd(String strMsg){
        int times = 1 ;

        try {
            String str = strMsg.trim();
            String str2="";
            if(StringUtils.isNotBlank(strMsg)){
                for(int i=0;i<str.length();i++) {
                    if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                        str2 += str.charAt(i);
                    }
                }
            }
            times = Integer.valueOf(str2);

        }catch (Exception e){
            logger.info("错误指令:"+strMsg);
        }

        StringBuffer sb = new StringBuffer();
        sb.append("的人物作成:\n");
        for (int i=0;i<times;i++){
            StringBuffer sbtemp = new StringBuffer();

            int liliang = getRandom(20);
            int tizhi = getRandom(20);
            int minjie =getRandom(20);


            int zhili = getRandom(20);
            int ganzhi=getRandom(20);
            int meihuo =getRandom(20);


            int total = liliang+tizhi+minjie+zhili+ganzhi+meihuo;

            sbtemp.append("力量:"+liliang);
            sbtemp.append(" 体质:"+tizhi);
            sbtemp.append(" 敏捷:"+minjie);

            sbtemp.append(" 智力:"+zhili);
            sbtemp.append(" 感知:"+ganzhi);
            sbtemp.append(" 魅惑:"+meihuo);

            sbtemp.append(" 共计:"+total );
            if (i<times-1){
                sbtemp.append("\n");
            }
            sb.append(sbtemp);
        }
        return sb.toString();
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
        Double douJrrp = Math.random() * (intRandom)+1;
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
//                System.out.println(getRandom(10));
//            }
//        }catch (Exception e){
//
//        }
//
//
//    }


}
