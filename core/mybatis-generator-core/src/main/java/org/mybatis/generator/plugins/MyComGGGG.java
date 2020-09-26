package org.mybatis.generator.plugins;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Properties;
import java.util.Set;

/**
 * @Description: TODO
 * @author: wangwc
 * @date: 2020/9/26 16:54
 */
public class MyComGGGG extends DefaultCommentGenerator {

    //自定义属性注释
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
//        /**
//         * 表字段 : APPLETABLE.MY_DATE
//         */
        StringBuilder sb = new StringBuilder();
        field.addJavaDocLine("/**");
        if(StringUtility.stringHasValue(introspectedColumn.getRemarks())){
            field.addJavaDocLine(" * "+introspectedColumn.getRemarks());
        }
        sb.append(" * 表字段：");
        sb.append(introspectedTable.getFullyQualifiedTable());//表名
        sb.append(".");
        sb.append(introspectedColumn.getActualColumnName());//字段名
        field.addJavaDocLine(sb.toString());
        field.addJavaDocLine(" */");
    }

}
