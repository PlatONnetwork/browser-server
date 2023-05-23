1. 安装依赖

```
npm install
```

2. 编译合约

```
npx hardhat compile
```

3. 配置

```
hardhat.config.js 文件里面  platon_prod.accounts 总设置私钥匙

```

4. 部署

```
npx hardhat run .\scripts\deploy-pScanQueryFacade.js --network platon_prod 

```