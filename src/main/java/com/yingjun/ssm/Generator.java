package com.yingjun.ssm;

import com.yingjun.ssm.util.MybatisGeneratorUtil;
import com.yingjun.ssm.util.PropertiesFileUtil;

/**
 * Created by Administrator on 2017/3/20.
 */
public class Generator {
    private static String JDBC_DRIVER = PropertiesFileUtil.getInstance("generator").get("jdbc.driverClassName");
    private static String JDBC_URL = PropertiesFileUtil.getInstance("generator").get("jdbc.url");
    private static String JDBC_USERNAME = PropertiesFileUtil.getInstance("generator").get("jdbc.username");
    private static String JDBC_PASSWORD = PropertiesFileUtil.getInstance("generator").get("jdbc.password");

    public static void main(String[] args) {
        MybatisGeneratorUtil.generator(JDBC_DRIVER,JDBC_URL,JDBC_USERNAME,JDBC_PASSWORD);
    }

}
