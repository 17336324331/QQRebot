package com.forte.demo.robot.model;

import lombok.Data;

import java.util.Date;

/**
 * @author 陈瑞扬
 * @date 2019年12月16日 0:15
 * @description 系统码工具类
 */
@Data
public class SystemCodeModel extends CommonModel{

    /**
     * 系统参数序号
     */
    private Integer intId;

    /**
     * 系统参数code码
     */
    private String strCode;

    /**
     * 系统参数值
     */
    private String strValue;


}
