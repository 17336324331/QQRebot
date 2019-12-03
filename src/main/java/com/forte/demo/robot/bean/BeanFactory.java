package com.forte.demo.robot.bean;

import com.forte.qqrobot.anno.depend.Beans;

/**
 *
 * beans工厂，用来创建一个MyBean2对象并放入依赖管理中心
 * 不论是什么类，但凡需要注入到依赖中，其类自身就必须首先注入进去
 * 也就是说，只要在类上存在@Beans注解，这个类下的方法上的@Beans注解才会生效
 *
 * @author ForteScarlet <[email]ForteScarlet@163.com>
 * @since JDK1.8
 **/
@Beans
public class BeanFactory {


    /**
     * 这个方法携带了@Beans注解，并且有一个参数：single=false
     * single代表这个依赖是否是一个单例对象，如果为false则在每次需要此对象的时候都会进行一次创建
     * 具体区别可以通过构造函数中的输出语句自己体会下~
     *
     * 这个方法返回值是MyBean2类型，所以MyBean2在此处被注入至依赖管理中心
     */
    @Beans(single = false)
    public MyBean2 getMyBean2(){
        MyBean2 myBean2 = new MyBean2();
        myBean2.setSchool("yyyyy中学");
        myBean2.setSize(9999);
        return myBean2;
    }



}
