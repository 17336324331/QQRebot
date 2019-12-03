package com.forte.demo.other;

import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.beans.messages.msgget.PrivateMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;

/**
 * 这个类理论上并不会被扫描到。
 * 因为我在{@link com.forte.demo.MyRobotRunApplication}中的配置里已经指定了包扫描路径
 *
 * @author ForteScarlet <[email]ForteScarlet@163.com>
 * @since JDK1.8
 **/
@Beans
public class OutListener {

    /**
     * 监听私信消息，但是此监听并不会被扫描进去
     */
    @Listen(MsgGetTypes.privateMsg)
    public void listen(PrivateMsg privateMsg){
        System.out.println(privateMsg.getMsg());
    }

}
