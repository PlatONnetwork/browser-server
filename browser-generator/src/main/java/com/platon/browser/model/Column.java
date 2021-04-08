package com.platon.browser.model;

import java.util.List;

/**
 * 字段
 */
public class Column
{

    /**
     * 列数据类型
     */
    private String type;

    /**
     * 表字段名
     */
    private String name;

    /**
     * javaName
     */
    private String javaName;

    /**
     * 用于getset的java字段名
     */
    private String getsetJavaName;

    /**
     * 数据库字段是否为Null
     */
    private Boolean isNull;

    /**
     * 是否是主键
     */
    private Boolean isPrimary;

    /**
     * 字段备注，注释
     */
    private String remark;

    public Column()
    {
        super();
    }

    /**
     * 判断columns里面是否有type类型
     * 
     * @param columns
     * @param type
     * @return
     */
    public static boolean typeContains(List<Column> columns, String type)
    {
        for (Column c : columns)
        {
            if (c.getType().equals(type))
            {
                return true;
            }
        }
        return false;
    }

    public String getType()
    {
        return type;
    }

    public Column setType(String type)
    {
        this.type = type;
        return this;
    }

    public String getName()
    {
        return name;
    }

    public Column setName(String name)
    {
        this.name = name;
        return this;
    }

    public String getJavaName()
    {
        return javaName;
    }

    public Column setJavaName(String javaName)
    {
        this.javaName = javaName;
        return this;
    }

    public String getGetsetJavaName()
    {
        return getsetJavaName;
    }

    public Column setGetsetJavaName(String getsetJavaName)
    {
        this.getsetJavaName = getsetJavaName;
        return this;
    }

    public String getRemark()
    {
        return remark;
    }

    public Column setRemark(String remark)
    {
        this.remark = remark;
        return this;
    }

    public Boolean getIsPrimary()
    {
        return isPrimary;
    }

    public Column setIsPrimary(Boolean isPrimary)
    {
        this.isPrimary = isPrimary;
        return this;
    }

    public Boolean getIsNull()
    {
        return isNull;
    }

    public Column setIsNull(Boolean isNull)
    {
        this.isNull = isNull;
        return this;
    }

    @Override
    public String toString()
    {
        return "Column [" + (type != null ? "type=" + type + ", " : "") + (name != null ? "name=" + name + ", " : "")
                + (javaName != null ? "javaName=" + javaName + ", " : "") + (getsetJavaName != null ? "getsetJavaName=" + getsetJavaName + ", " : "")
                + (isNull != null ? "isNull=" + isNull + ", " : "") + (isPrimary != null ? "isPrimary=" + isPrimary + ", " : "")
                + (remark != null ? "remark=" + remark : "") + "]";
    }

}
