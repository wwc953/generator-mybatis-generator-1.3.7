package org.mybatis.generator.jdbc;

/**
 * @Description: TODO
 * @author: wangwc
 * @date: 2020/9/28 11:22
 */
public class Test {
    public static void main(String[] args) {
        AbstractJdbcUtils utils = new MySqlUtils();
//        AbstractJdbcUtils utils = new OracleUtils();
        System.out.println(utils.updateSQL("my_test"));
    }
}
