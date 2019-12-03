package com.forte.demo.robot.bean;

/**
 *
 * 这是一个自定义依赖对象，此类不存在@Beans注解，是不能直接就被获取到的
 * 所以我用过{@link BeanFactory} 对其进行了注入
 *
 * @author ForteScarlet <[email]ForteScarlet@163.com>
 * @since JDK1.8
 **/
public class MyBean2 {

    private String school = "xxx小学";
    private int size = 128;

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * 默认的无参构造中输出语句
     */
    public MyBean2(){
        System.out.println("创建了实例MyBean2！");
    }

}
