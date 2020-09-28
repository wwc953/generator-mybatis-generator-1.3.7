package org.mybatis.generator.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @Description: TODO Oracle
 * @author: wangwc
 * @date: 2020/9/28 11:02
 */
public class OracleUtils extends AbstractJdbcUtils {

    @Override
    public String getColumnsInfoSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT a.column_id as ORDINAL_POSITION, a.column_name, a.data_type, a.data_precision as NUMERIC_PRECISION, a.data_scale as NUMERIC_SCALE, b.comments as COLUMN_COMMENT, ");
        sb.append(" CASE WHEN a.column_name = ( SELECT col.column_name FROM user_constraints con JOIN user_cons_columns col ON con.constraint_name = col.constraint_name WHERE con.table_name = ? AND con.constraint_type = 'P' ) THEN 1 ELSE 0 END AS PK,");
        sb.append(" (SELECT nvl (t.comments, ' ') FROM user_tab_comments t WHERE t.table_name = ? ) AS table_comment");
        sb.append(" from user_tab_cols a JOIN user_col_comments b ON a.column_name = b.column_name AND a.table_name = b.table_name WHERE a.table_name = ? ORDER BY a.column_id");
        return sb.toString();
    }

    @Override
    public Connection getConnection() {
        Connection conn = null;
        try {
            //初始化驱动类oracle.jdbc.OracleDriver, 加载到JVM
            Class.forName("oracle.jdbc.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.43.66:1521:orcl",
                    "test", "test");
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
