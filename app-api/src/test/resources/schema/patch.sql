-- H2数据库补丁，用于兼容mysql特有函数
CREATE ALIAS UNIX_TIMESTAMP FOR "com.platon.browser.util.MySQLFunctions.unixTimestamp"
