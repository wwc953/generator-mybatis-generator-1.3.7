package org.mybatis.generator.jdbc;

import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getGetterMethodName;

/**
 * @Description: TODO
 * @author: wangwc
 * @date: 2020/9/28 11:22
 */
public class Test {
    public static void main(String[] args) {
//        AbstractJdbcUtils utils = new MySqlUtils();
//        AbstractJdbcUtils utils = new OracleUtils();
//        System.out.println(utils.updateSQL("my_test"));

        System.out.println(System.getProperty("os.name"));
        System.out.println(System.getProperty("file.separator"));
        System.out.println(System.getProperty("line.separator"));
        System.out.println(System.getProperty("path.separator"));
        System.out.println("当前程序所在目录 ====> "+System.getProperty("user.dir"));
    }

}
