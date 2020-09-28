package org.mybatis.generator.jdbc;

import org.mybatis.generator.jdbc.vo.ColumnInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 自动生成JDBC代码
 * @author: wangwc
 * @date: 2020/9/27 17:27
 */
public abstract class AbstractJdbcUtils {

    protected List<ColumnInfo> columnInfos;

    /**
     * get方法
     */
    public static final String GET_METHOD = "get";
    /**
     * set方法
     */
    public static final String SET_METHOD = "set";

    public static final String INSERT = "insert";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    public static final String SELECT = "select";

    /**
     * 表信息SQL
     * @return
     */
    public abstract String getColumnsInfoSQL();

    /**
     * 获取连接
     * @return
     */
    public abstract Connection getConnection();

    /**
     * 获取表字段信息
     *
     * @param tableName
     * @return
     */
    public List<ColumnInfo> getColumns(String tableName) {
        List<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(getColumnsInfoSQL());
            ps.setString(1, tableName);
            ps.setString(2, tableName);
            ps.setString(3, tableName);

            rs = ps.executeQuery();
            while (rs.next()) {
                ColumnInfo info = new ColumnInfo();
                info.setColId(rs.getInt("ORDINAL_POSITION"));
                info.setColName(rs.getString("COLUMN_NAME"));
                info.setColType(rs.getString("DATA_TYPE"));
                info.setNumBefor(rs.getInt("NUMERIC_PRECISION"));
                info.setNumAfter(rs.getInt("NUMERIC_SCALE"));
                info.setComments(rs.getString("COLUMN_COMMENT"));
                info.setPk(rs.getString("PK"));
                info.setTableComments(rs.getString("TABLE_COMMENT"));
                columnInfos.add(info);
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

    public void colse(ResultSet rs, PreparedStatement ps, Connection conn) {
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
    public String camelCaseString(String columnName) {
        if (isEmpty(columnName)) return null;
        char[] charArray = columnName.toLowerCase().toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '_')
                charArray[i + 1] -= 32;
        }
        return new String(charArray).replace("_", "");
    }

    public boolean isEmpty(String inputString) {
        if (inputString == null || inputString.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取get/set方法名称
     *
     * @param property
     * @param methodType
     * @return
     */
    public String getGetOrSetJavaMethodName(String property, String methodType) {
        char[] charArray = property.toCharArray();
        char c = charArray[1];
        if (!(c >= 'A' && c <= 'Z')) {
            charArray[0] -= 32;
        }
        StringBuilder sb = new StringBuilder(methodType);
        sb.append(charArray);
        return sb.toString();
    }

    public abstract String insertSQL(String tableName);

    public String updateSQL(String tableName) {
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

    protected void initColumnsInfoList(String tableName){
        if (columnInfos == null || columnInfos.isEmpty()) {
            columnInfos = getColumns(tableName);
        }
    }


}
