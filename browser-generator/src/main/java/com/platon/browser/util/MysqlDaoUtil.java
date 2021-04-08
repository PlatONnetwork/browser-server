package com.platon.browser.util;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.platon.browser.model.Column;
import com.platon.browser.model.ColumnDao;
import com.platon.browser.model.DbConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MysqlDaoUtil
{

    private static final Log log = LogFactory.get();
    

    /**
     * 查询所有的表
     *
     * @param config
     * @return
     */
    public List<String> queryAllTables(DbConfig config)
    {
        List<String> list = new ArrayList<String>();
        try
        {
            checkDriver(config);
            Connection conn = getConn(config);
            ResultSet rs = createQuary(conn, "show tables");
            while (rs.next())
            {
                list.add(rs.getString(1));
            }
            rs.close();
            conn.close();
            log.info("查询所有表名成功={}", list);
        } catch (Exception e)
        {
            log.error(e, "查询所有的表失败={}", config);
        }
        return list;
    }

    /**
     * 查询表结构
     *
     * @param config
     * @param tableName
     * @return
     */
    public ColumnDao queryColumns(DbConfig config, String tableName)
    {
        ColumnDao columnDao = new ColumnDao();
        try
        {
            checkDriver(config);
            Connection conn = getConn(config);
            ResultSet rs = createQuary(conn, "show full fields from " + tableName);
            while (rs.next())
            {
                Column column = new Column();
                String javaName = toJavaName(rs.getString(1));
                String type = typesConvert(rs.getString(2));
                String isNull = rs.getString(4);
                Boolean isnull = true;
                if ("NO".equals(isNull))
                {
                    isnull = false;
                }
                String pri = rs.getString(5);
                Boolean ispri = false;
                if ("PRI".equals(pri))
                {
                    ispri = true;
                }
                String getsetJavaName = toUpperCaseFirstOne(javaName);
                column.setType(type).setName(rs.getString(1)).setJavaName(javaName).setGetsetJavaName(getsetJavaName).setIsNull(
                        isnull).setIsPrimary(ispri).setRemark(rs.getString(9));
                columnDao.getColumnList().add(column);
                String className = MysqlDaoUtil.toClassName(tableName);
                columnDao.setClassName(className);
            }
            ResultSet rs2 = createQuary(conn,
                                        "SELECT TABLE_NAME,TABLE_COMMENT FROM information_schema. TABLES WHERE TABLE_NAME = '" + tableName + "'");
            while (rs2.next())
            {
                columnDao.setTableRemark(rs2.getString(2));
            }
            rs.close();
            conn.close();
            log.info("查询表结构{}成功", tableName);
        } catch (Exception e)
        {
            log.error(e, "查询表结构{}失败", tableName);
        }
        return columnDao;
    }

    /**
     * 功能：获得列的数据类型
     *
     * @param sqlType
     * @return
     */
    public String typesConvert(String sqlType)
    {
        sqlType = sqlType.substring(0, sqlType.indexOf("(") == -1 ? sqlType.length() : sqlType.indexOf("("));
        if (sqlType.equalsIgnoreCase("bit"))
        {
            return "Boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint"))
        {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("smallint"))
        {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("int"))
        {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("bigint"))
        {
            return "Long";
        } else if (sqlType.equalsIgnoreCase("float"))
        {
            return "Float";
        } else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric") || sqlType.equalsIgnoreCase(
                "real") || sqlType.equalsIgnoreCase("money") || sqlType.equalsIgnoreCase("smallmoney") || sqlType.equalsIgnoreCase(
                "double"))
        {
            return "BigDecimal";
        } else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char") || sqlType.equalsIgnoreCase(
                "nvarchar") || sqlType.equalsIgnoreCase("nchar") || sqlType.equalsIgnoreCase("text"))
        {
            return "String";
        } else if (sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("date"))
        {
            return "Date";
        } else if (sqlType.equalsIgnoreCase("image"))
        {
            return "Blob";
        }
        return "String";
    }

    /**
     * 把数据库的列名转为java类型的字段名
     *
     * @param columnName
     * @return
     */
    public static String toJavaName(String columnName)
    {
        String patternStr = "(_[a-z,A-Z])";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(columnName.toLowerCase());
        StringBuffer buf = new StringBuffer();
        while (matcher.find())
        {
            String replaceStr = matcher.group();
            matcher.appendReplacement(buf, replaceStr.toUpperCase());
        }
        matcher.appendTail(buf);
        return buf.toString().replaceAll("_", "");
    }

    /**
     * 表名转类名
     *
     * @param tableName
     * @return
     */
    public static String toClassName(String tableName)
    {
        return capitalize(toUpper(tableName));
    }

    public static String capitalize(String str)
    {
        int strLen;
        if (str == null || (strLen = str.length()) == 0)
            return str;
        else
            return (new StringBuilder(strLen)).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString();
    }

    /**
     * 首字母转大写
     *
     * @param s
     * @return
     */
    public static String toUpperCaseFirstOne(String s)
    {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    /**
     * 首字母转小写
     *
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s)
    {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    /**
     * “_”+小写 转成大写字母
     *
     * @param str
     * @return
     */
    private static String toUpper(String str)
    {
        char[] charArr = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < charArr.length; i++)
        {
            if (charArr[i] == '_')
            {
                sb.append(String.valueOf(charArr[i + 1]).toUpperCase());
                i = i + 1;
            } else
            {
                sb.append(charArr[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 检查DriverClass
     *
     * @param config
     */
    protected void checkDriver(DbConfig config)
    {
        try
        {
            Class.forName(config.getDriverClass());
        } catch (ClassNotFoundException e)
        {
            log.error(e, "检查DriverClass错误");
        }
    }

    /**
     * 连接数据库
     *
     * @param config
     * @return
     * @throws SQLException
     */
    protected Connection getConn(DbConfig config) throws SQLException
    {
        return (Connection) DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
    }

    /**
     * 执行SQL
     *
     * @param conn
     * @param sql
     * @return
     * @throws SQLException
     */
    protected ResultSet createQuary(Connection conn, String sql) throws SQLException
    {
        return conn.createStatement().executeQuery(sql);
    }

}
