package com.platon.browser;//package com.platon.browser;


import com.platon.browser.common.enums.AppStatus;
import com.platon.browser.persistence.dao.param.*;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/7 16:35
 * @Description:
 */
public class TestBase {

    static {
        System.setProperty(AppStatus.class.getName(),AppStatus.STOP.name());
    }

    /*private static Logger logger = LoggerFactory.getLogger(TestBase.class);
    private static String prefix = "data/",suffix=".json",encode="UTF8";
    private static String[] dataFile = {
            "node",
            "block",
            "transaction",
            "staking",
            "delegation",
            "unDelegation",
            "verifier",
            "validator",
            "candidate",
            "address",
            "proposal"
    };

    public NodeCache nodeCache = new NodeCache();
    public List<CustomNode> nodes= Collections.emptyList();
    public List<CustomBlock> blocks= Collections.emptyList();
    public List<TransactionBean> transactions= Collections.emptyList();
    public List<CustomStaking> stakings= Collections.emptyList();
    public List<CustomDelegation> delegations= Collections.emptyList();
    public List<CustomUnDelegation> unDelegations= Collections.emptyList();
    public List<CustomProposal> proposals = Collections.emptyList();
    public List<Node> verifiers= new ArrayList<>();
    public List<Node> validators= new ArrayList<>();
    public List<Node> candidates= new ArrayList<>();
    public List<CustomAddress> addresses= Collections.emptyList();

    @Before
    public void init(){
        Arrays.asList(dataFile).forEach(fileName->{
            try {
                URL url = TestBase.class.getClassLoader().getResource(prefix+fileName+suffix);
                String path = url.getPath();
                String content = FileUtils.readFileToString(new File(path),encode);

                switch (fileName){
                    case "node":
                        nodes = JSON.parseArray(content,CustomNode.class);
                        break;
                    case "block":
                        blocks = JSON.parseArray(content,CustomBlock.class);
                        break;
                    case "transaction":
                        transactions = JSON.parseArray(content,TransactionBean.class);
                        break;
                    case "staking":
                        stakings = JSON.parseArray(content,CustomStaking.class);
                        break;
                    case "delegation":
                        delegations = JSON.parseArray(content,CustomDelegation.class);
                        break;
                    case "unDelegation":
                        unDelegations = JSON.parseArray(content,CustomUnDelegation.class);
                        break;
                    case "verifier":
                        List<NodeBean> verList = JSON.parseArray(content,NodeBean.class);
                        verifiers.addAll(verList);
                        break;
                    case "validator":
                        List<NodeBean> valList = JSON.parseArray(content,NodeBean.class);
                        validators.addAll(valList);
                        break;
                    case "candidate":
                        List<NodeBean> canList = JSON.parseArray(content,NodeBean.class);
                        candidates.addAll(canList);
                        break;
                    case "address":
                        addresses = JSON.parseArray(content,CustomAddress.class);
                        break;
                    case "proposal":
                        proposals = JSON.parseArray(content,CustomProposal.class);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Map<Long,List<CustomTransaction>> txMap = new HashMap<>();
        transactions.forEach(tx->{
            List<CustomTransaction> txes = txMap.computeIfAbsent(tx.getBlockNumber(), k -> new ArrayList<>());
            txes.add(tx);
        });
        blocks.forEach(block -> {
            List<CustomTransaction> txes = txMap.get(block.getNumber());
            if(txes!=null) block.setTransactionList(txes);
        });

        try {
            nodeCache.init(nodes,stakings,delegations,unDelegations);
        } catch (CacheConstructException e) {
            e.printStackTrace();
        }
        logger.info("测试数据加载完成！");
    }
*/
    public CreateStakingParam stakingParam(){
        CreateStakingParam createStakingParam = new CreateStakingParam();
        createStakingParam.setNodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18");
        createStakingParam.setStakingHes(new BigDecimal("5000"));
        createStakingParam.setNodeName("testNode01");
        createStakingParam.setExternalId("externalId01");
        createStakingParam.setBenefitAddr("0xff48d9712d8a55bf603dab28f4645b6985696a61");
        createStakingParam.setProgramVersion("1794");
        createStakingParam.setBigVersion("1700");
        createStakingParam.setWebSite("web_site01");
        createStakingParam.setDetails("details01");
        createStakingParam.setIsInit(1);
        createStakingParam.setStakingBlockNum(new BigInteger("200"));
        createStakingParam.setStakingTxIndex(0);
        createStakingParam.setStakingAddr("0xb58c7fd25437e2fcf038b948963ffb80276bd44d");
        createStakingParam.setJoinTime(new Date(System.currentTimeMillis()));
        createStakingParam.setTxHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7");
        return createStakingParam;
    }

    public ModifyStakingParam modifyStakingParam(){
        ModifyStakingParam modifyStakingParam = new ModifyStakingParam();
        modifyStakingParam.setNodeName("testNode02");
        modifyStakingParam.setExternalId("externalId02");
        modifyStakingParam.setBenefitAddr("0xff48d9712d8a55bf603dab28f4645b6985696a61");
        modifyStakingParam.setWebSite("web_site01");
        modifyStakingParam.setDetails("details01");
        modifyStakingParam.setIsInit(2);
        modifyStakingParam.setTxHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7");
        modifyStakingParam.setStakingBlockNum(new BigInteger("200"));
        modifyStakingParam.setBNum(new BigInteger("300"));
        modifyStakingParam.setTime(new java.sql.Date(System.currentTimeMillis()));
        modifyStakingParam.setNodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18");
        return modifyStakingParam;
    }

    public AddStakingParam addStakingParam(){
        AddStakingParam addStakingParam = new AddStakingParam();
        addStakingParam.setAmount(new BigDecimal("500000000000000000000000000"));
        addStakingParam.setNodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18");
        addStakingParam.setTxHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7");
        addStakingParam.setBNum(new BigInteger("300"));
        addStakingParam.setTime(new Date(System.currentTimeMillis()));
        addStakingParam.setStakingBlockNum(new BigInteger("200"));
        return addStakingParam;
    }

    public WithdrewStakingParam withdrewStakingParam(){
        WithdrewStakingParam withdrewStakingParam = new WithdrewStakingParam();
        withdrewStakingParam.setNodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18");
        withdrewStakingParam.setTxHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7");
        withdrewStakingParam.setBNum(new BigInteger("300"));
        withdrewStakingParam.setStakingBlockNum(new BigInteger("200"));
        withdrewStakingParam.setTime(new Date(System.currentTimeMillis()));
        withdrewStakingParam.setStakingReductionEpoch(3);
        return withdrewStakingParam;
    }

    public ReportDuplicateSignParam reportDuplicateSignParam(){
        ReportDuplicateSignParam reportDuplicateSignParam = new ReportDuplicateSignParam();
        reportDuplicateSignParam.setTime(new Date(System.currentTimeMillis()));
        reportDuplicateSignParam.setSettingEpoch(3);
        reportDuplicateSignParam.setNodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18");
        reportDuplicateSignParam.setStakingBlockNum(new BigInteger("200"));
        reportDuplicateSignParam.setBNum(new BigInteger("200"));
        reportDuplicateSignParam.setTxHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7");
        reportDuplicateSignParam.setSlashRate("0.5");
        reportDuplicateSignParam.setSlashData("json");
        reportDuplicateSignParam.setBenefitAddr("0x02fba14f5e72092c8fca6ced087cd4e7be0d8fc5");
        reportDuplicateSignParam.setCodeCurStakingLocked(new BigDecimal("50000000"));
        reportDuplicateSignParam.setCodeNodeoptDesc("AMOUNT");
        reportDuplicateSignParam.setCodeStatus(2);
        reportDuplicateSignParam.setCodeRewardValue(new BigDecimal("1000000000"));
        return  reportDuplicateSignParam;
    }
}
