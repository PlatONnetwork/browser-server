package com.platon.browser.model;

import java.util.ArrayList;
import java.util.List;

public class ColumnDao
{
    /**
     * 表注释
     */
    private String tableRemark;

    /**
     * 类名
     */
    private String className;

    /**
     * 字段列表
     */
    private List<Column> columnList = new ArrayList<Column>();

    public ColumnDao()
    {
        super();
    }

    public ColumnDao(String tableRemark, String className, List<Column> columnList)
    {
        super();
        this.tableRemark = tableRemark;
        this.className = className;
        this.columnList = columnList;
    }

    public String getTableRemark()
    {
        return tableRemark;
    }

    public void setTableRemark(String tableRemark)
    {
        this.tableRemark = tableRemark;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public List<Column> getColumnList()
    {
        return columnList;
    }

    public void setColumnList(List<Column> columnList)
    {
        this.columnList = columnList;
    }

    @Override
    public String toString()
    {
        return "ColumnDao [" + (tableRemark != null ? "tableRemark=" + tableRemark + ", " : "")
                + (className != null ? "className=" + className + ", " : "") + (columnList != null ? "columnList=" + columnList : "") + "]";
    }

}
