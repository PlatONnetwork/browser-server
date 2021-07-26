### 每个版本递增SQL

文件命名格式：
```text
scan-v{previousVersion}-to-v{nextVersion}-{cleanData}.sql

$previousVersion:前一个版本号
$nextVersion:下一个版本号
$cleanData:是否需要清库，如果为true，则需要把库下的所有表数据清除，再导入初始化数据；如果为false，则导入脚本即可
```
