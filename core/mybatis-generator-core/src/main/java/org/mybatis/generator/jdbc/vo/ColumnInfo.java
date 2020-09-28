package org.mybatis.generator.jdbc.vo;

import java.io.Serializable;

/**
 * @Description: 字段信息实体
 * @author: wangwc
 * @date: 2020/9/28 11:11
 */
public class ColumnInfo implements Serializable {

    private Integer colId;//字段设计顺序
    private String colName;//字段名
    private String colType;//字段类型
    private Integer numBefor;//NUMBER(a,b) ==> a
    private Integer numAfter;//NUMBER(a,b) ==> b
    private String comments;//字段注释
    private String pk;//主键 1是 0否
    private String tableComments;//表注释
    private String javaProperty;//java属性

    public String getJavaProperty() {
        return javaProperty;
    }

    public void setJavaProperty(String javaProperty) {
        this.javaProperty = javaProperty;
    }

    public Integer getColId() {
        return colId;
    }

    public void setColId(Integer colId) {
        this.colId = colId;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }

    public Integer getNumBefor() {
        return numBefor;
    }

    public void setNumBefor(Integer numBefor) {
        this.numBefor = numBefor;
    }

    public Integer getNumAfter() {
        return numAfter;
    }

    public void setNumAfter(Integer numAfter) {
        this.numAfter = numAfter;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getTableComments() {
        return tableComments;
    }

    public void setTableComments(String tableComments) {
        this.tableComments = tableComments;
    }
}
