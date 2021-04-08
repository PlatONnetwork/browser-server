package com.platon.browser.model;

/**
 * 数据源配置
 */
public class DbConfig
{
    private String driverClass;

    private String url;

    private String username;

    private String password;

    public DbConfig()
    {
        this.driverClass = Common.driverClass;
        this.url = Common.url;
        this.username = Common.username;
        this.password = Common.password;
    }

    public DbConfig(String driverClass, String url, String username, String password)
    {
        super();
        this.driverClass = driverClass;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getDriverClass()
    {
        return driverClass;
    }

    public DbConfig setDriverClass(String driverClass)
    {
        this.driverClass = driverClass;
        return this;
    }

    public String getUrl()
    {
        return url;
    }

    public DbConfig setUrl(String url)
    {
        this.url = url;
        return this;
    }

    public String getUsername()
    {
        return username;
    }

    public DbConfig setUsername(String username)
    {
        this.username = username;
        return this;
    }

    public String getPassword()
    {
        return password;
    }

    public DbConfig setPassword(String password)
    {
        this.password = password;
        return this;
    }

    @Override
    public String toString()
    {
        return "DbConfig [" + (driverClass != null ? "driverClass=" + driverClass + ", " : "") + (url != null ? "url=" + url + ", " : "")
                + (username != null ? "username=" + username + ", " : "") + (password != null ? "password=" + password : "") + "]";
    }

}
