package org.mybatis.generator.jdbc;

/**
 * @Description: TODO
 * @author: wangwc
 * @date: 2020/9/28 11:22
 */
public class Test {
    public static void main(String[] args) {
        MySqlUtils sqlUtils = new MySqlUtils();
        System.out.println(sqlUtils.insertSQL("my_test"));
    }
}
