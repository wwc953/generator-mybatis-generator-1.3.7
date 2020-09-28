package org.mybatis.generator.jdbc;

import org.mybatis.generator.jdbc.vo.ColumnInfo;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @Description: TODO MySQL
 * @author: wangwc
 * @date: 2020/9/28 11:05
 */
public class MySqlUtils extends AbstractJdbcUtils {
    @Override
    public String getColumnsInfoSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c.ORDINAL_POSITION,c.COLUMN_NAME,c.DATA_TYPE,c.NUMERIC_PRECISION,c.NUMERIC_SCALE,c.COLUMN_COMMENT, ");
        sb.append(" CASE WHEN c.COLUMN_NAME = (SELECT c.COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE c WHERE c.table_name=? AND c.constraint_name='PRIMARY') THEN 1 ELSE 0 END as PK,");
        sb.append(" (SELECT IFNULL(t.TABLE_COMMENT,' ') FROM information_schema.TABLES t WHERE t.TABLE_NAME=?) as TABLE_COMMENT");
        sb.append(" FROM information_schema.COLUMNS c WHERE c.TABLE_NAME = ?");
        return sb.toString();
    }

    @Override
    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8",
                    "root", "123456");
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    @Override
    public String insertSQL(String tableName) {
        initColumnsInfoList(tableName);
        StringBuilder sb = new StringBuilder("insert into ");
        StringBuilder valBuf = new StringBuilder();

        sb.append(tableName.toUpperCase() + "(");
        for (int i = 0; i < columnInfos.size(); i++) {
            ColumnInfo info = columnInfos.get(i);
            if (!"1".equals(info.getPk())) {
                sb.append(info.getColName());
                valBuf.append(" ?");
                if (i != columnInfos.size() - 1) {
                    sb.append(", ");
                    valBuf.append(", ");
                }
            }
        }
        sb.append(") values (").append(valBuf).append(")");
        return sb.toString();
    }

}
