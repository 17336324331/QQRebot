package com.forte.demo.robot.mapper;

import com.forte.demo.robot.model.RuleModel;

/**
 * @author 陈瑞扬
 * @date 2019年12月08日 11:13
 * @description
 */
public interface RuleMapper {

    RuleModel selectAllByName(String strMsg);

    RuleModel selectContentByName(String strMsg);

}
