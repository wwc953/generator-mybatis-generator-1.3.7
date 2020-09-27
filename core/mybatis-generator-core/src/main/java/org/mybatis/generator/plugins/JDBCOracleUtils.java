package org.mybatis.generator.plugins;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO 自动生成JDBC代码
 * @author: wangwc
 * @date: 2020/9/27 17:27
 */
public class JDBCOracleUtils {

    private static List<ColumnInfo> columnInfos;

    /**
     * Oracle表信息SQL
     *
     * @return
     */
    private static String getColumnsInfoSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT a.column_id, a.column_name, a.data_type, a.data_precision, a.data_scale, b.comments, ");
        sb.append(" CASE WHEN a.column_name = ( SELECT col.column_name FROM user_constraints con JOIN user_cons_columns col ON con.constraint_name = col.constraint_name WHERE con.table_name = ? AND con.constraint_type = 'P' ) THEN 1 ELSE 0 END AS PK,");
        sb.append(" (SELECT nvl (t.comments, ' ') FROM user_tab_comments t WHERE t.table_name = ? ) AS table_comments");
        sb.append(" from user_tab_cols a JOIN user_col_comments b ON a.column_name = b.column_name AND a.table_name = b.table_name WHERE a.table_name = ? ORDER BY a.column_id");
        return sb.toString();
    }


    public static List<ColumnInfo> getColumns(String tableName) {
        List<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String sql = getColumnsInfoSQL();
            ps = conn.prepareStatement(sql);
            ps.setString(1, tableName);
            ps.setString(2, tableName);
            ps.setString(3, tableName);

            rs = ps.executeQuery();
            while (rs.next()) {
                ColumnInfo c = new ColumnInfo();
                c.setColId(rs.getInt("COLUMN_ID"));
                c.setColName(rs.getString("COLUMN_NAME"));
                c.setColType(rs.getString("DATA_TYPE"));
                c.setNumBefor(rs.getInt("DATA_PRECISION"));
                c.setNumAfter(rs.getInt("DATA_SCALE"));
                c.setComments(rs.getString("COMMENTS"));
                c.setPk(rs.getString("PK"));
                c.setTableComments(rs.getString("TABLE_COMMENTS"));
                columnInfos.add(c);
            }
//            columnInfos.forEach(v -> v.setJavaProperty(camelCaseString(v.getColName())));
            for (ColumnInfo cinfo : columnInfos) {
                cinfo.setJavaProperty(camelCaseString(cinfo.getColName()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            colse(rs, ps, conn);
        }
        return columnInfos;
    }

    public static void colse(ResultSet rs, PreparedStatement ps, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 驼峰
     *
     * @param columnName
     * @return
     */
    public static String camelCaseString(String columnName) {
        char[] charArray = columnName.toLowerCase().toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '_')
                charArray[i + 1] -= 32;
        }
        return new String(charArray).replace("_", "");
    }

    /**
     * 获取get/set方法名称
     *
     * @param property
     * @param methodType
     * @return
     */
    public static String getGetOrSetJavaMethodName(String property, String methodType) {
        char[] charArray = property.toCharArray();
        char c = charArray[1];
        if (!(c >= 'A' && c <= 'Z')) {
            charArray[0] -= 32;
        }
        StringBuilder sb = new StringBuilder(methodType);
        sb.append(charArray);
        return sb.toString();
    }

    public static String insertSQL(String tableName) {
        if (columnInfos == null || columnInfos.isEmpty()) {
            columnInfos = getColumns(tableName);
        }
        int size = columnInfos.size();
        StringBuilder sb = new StringBuilder("insert into ");
        tableName = tableName.toUpperCase();
        sb.append(tableName + "(");
        for (int i = 0; i < size; i++) {
            ColumnInfo info = columnInfos.get(i);
            sb.append(info.getColName());
            if (i != size - 1) {
                sb.append(", ");
            }
        }
        sb.append(" ) values (");
        for (int i = 0; i < size; i++) {
            ColumnInfo info = columnInfos.get(i);
            if ("1".equals(info.getPk())) {
                sb.append(tableName + "_seq.nextval");
            } else {
                sb.append(" ?");
            }
            if (i != size - 1) {
                sb.append(", ");
            }
        }
        sb.append(" )");
        return sb.toString();
    }

    public static String updateSQL(String tableName) {
        if (columnInfos == null || columnInfos.isEmpty()) {
            columnInfos = getColumns(tableName);
        }
        ColumnInfo pkInfo = null;
        int size = columnInfos.size();
        StringBuilder sb = new StringBuilder("update  " + tableName + " set ");
        for (int i = 0; i < size; i++) {
            ColumnInfo info = columnInfos.get(i);
            if (!"1".equals(info.getPk())) {
                sb.append(info.getColName() + " = ?");
                if (i != size - 1) {
                    sb.append(", ");
                }
            } else {
                pkInfo = info;
            }
        }

        sb.append(" where " + pkInfo.getColName() + " = ?");
        return sb.toString();
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.43.66:1521:orcl",
                    "test", "test");
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) throws SQLException {
//        List<ColumnInfo> my_test = getColumns("my_test");
        System.out.println(updateSQL("my_test"));
    }

    private static class ColumnInfo implements Serializable {
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

}
