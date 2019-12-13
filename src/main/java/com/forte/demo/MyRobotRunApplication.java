package com.forte.demo;

//import com.forte.demo.robot.bean.MyBean1;
import com.forte.qqrobot.beans.messages.result.GroupList;
import com.forte.qqrobot.beans.types.CacheTypes;
import com.forte.qqrobot.component.forhttpapi.HttpApp;
import com.forte.qqrobot.component.forhttpapi.HttpApplication;
import com.forte.qqrobot.component.forhttpapi.HttpConfiguration;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;

/**
 * 这个是整个机器人的启动器，也是mian函数所在的位置。
 * 此demo使用HTTP API组件进行示例。<br>
 * 1、实现{@link com.forte.qqrobot.component.forhttpapi.HttpApp} 接口 ,
 * 并实现接口中的两个方法:
 *  {@link #before(HttpConfiguration)}  <br>
 *  {@link #after(CQCodeUtil, MsgSender)}   <br>
 *  其中，before方法是对参数的配置，通过方法中传递的{@link HttpConfiguration}进行配置    <br>
 *  after方法是在启动成功后，提供了一个回调函数以供测试    <br>
 *
 *  configuration中的配置，每个不同的组件之间存在一些差异，也存在一些相似之处。
 *
 *
 * @author ForteScarlet <[email]ForteScarlet@163.com>
 * @since JDK1.8
 **/
public class MyRobotRunApplication implements HttpApp {

    /**
     * main函数，用于执行程序
     */
    public static void main(String[] args) {
        //此处新建一个我们实现了HttpApp接口的启动器对象
        MyRobotRunApplication myRobotRunApplication = new MyRobotRunApplication();
        //新建一个Http所提供的启动器对象
        HttpApplication httpApplication = new HttpApplication();
        /*
            每个组件所提供的启动器接口与启动器类都是不一样的，但是大部分来讲，他们的名称有着共同点
            例如HTTP API组件的启动器接口叫做 HttpApp, 启动器类叫做HttpApplication
            而Lemoc组件的启动器接口叫做 LemocApp, 启动器类叫做LemocApplication
         */

        //调用Http启动器，将我们实现好的启动器对象实例放入
        //我们实现的启动器对象就像是一把钥匙，用来开启机器人服务
        httpApplication.run(myRobotRunApplication);

//        MyBean1 myBean1 = httpApplication.getDependGetter().get(MyBean1.class);
//        System.out.println(myBean1.getBean2().getSchool());


        /*
            以上就是流程最短的启动过程。
            每个启动器中还存在一些API，例如Close()、getDependGetter()之类的，更加详细的功能请查阅文档或者入群咨询
         */

    }

    /**
     * 启动前的配置
     * @param configuration 配置对象
     */
    @Override
    public void before(HttpConfiguration configuration) {
        //从核心的1.3-BETA版本开始，配置类支持链式配置
        configuration
                //此处为酷Q所在的IP地址，默认为127.0.0.1
                //.setIp("127.241.105.27")
                //.setIp("35.241.105.27")
                .setIp("127.0.0.1")
                //此处为你java程序执行的时候，程序中所启动的服务器端口
                //此端口用于接收酷Q插件(HTTP API)所提交的信息
                //默认值为15514
                // 对应的插件端配置请查看resources目录下的酷Q配置1.png中，提交方式地址中的端口号
                .setJavaPort(15514)

                //此处为酷Q插件(HTTP API)向Java服务器的请求路径
                //默认值为coolq
                //对应插件端的配置请查看resources目录下的酷Q配置1.png中，
                // 提交方式地址中的从端口号结尾处开始至最后部分的路径
                .setServerPath("/coolq")

                //此处为酷Q插件(HTTP API)接受我们(java)端所发送的请求的时候使用的端口
                //默认值为8877
                //对应插件端的配置请查看resources目录下的酷Q配置2.png中的监听端口中的端口号
                .setServerPort(8877)

                //此处配置酷Q的根目录
                //可以不进行配置
//                .setCqPath("F://a//b//coolq")

                //此处指定需要进行扫描的包路径，可以是多个，也可以不进行配置
                //当你不进行配置的时候，默认扫描此启动器所在的目录
                .setScannerPackage("com.forte.demo.robot");
                //还有一些基本无关紧要的配置，详细配置内容请查看文档

        //配置结束之后，就可以不用管了，或者想要干点什么别的也无所谓~
    }

    /**
     * 启动成功之后，通过缓存获取全部的群列表
     *
     * 缓存功能由核心版本1.3-BETA后开始支持，你也可以选择不使用缓存
     * 关于新加入的缓存转化详情请查阅文档
     *
     * @param cqCodeUtil    CQCodeUtil工具类
     * @param sender        送信器
     */
    @Override
    public void after(CQCodeUtil cqCodeUtil, MsgSender sender) {
//        //获取群列表
//        GroupList groupList = sender.GETTER
//                //定义缓存3小时
//                .cache(3, CacheTypes.PLUS_HOURS)
//                .getGroupList();
//
//        //遍历所有的群并打印群的群号
//        groupList.forEach(group -> System.out.println(group.getCode()));

    }
}
