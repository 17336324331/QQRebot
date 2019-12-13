package com.forte.demo.robot.model;

import lombok.Data;
import org.quartz.PersistJobDataAfterExecution;

import java.util.Date;

/**
 * @author 陈瑞扬
 * @date 2019年12月13日 23:24
 * @description
 */
@Data
public class NameModel extends  CommonModel {

    private String strNickName;

    private String strCard;

    private String strNName;

}

