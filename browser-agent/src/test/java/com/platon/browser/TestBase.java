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
        CreateStakingParam createStakingParam = CreateStakingParam.builder()
                .nodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18")
                .stakingHes(new BigDecimal("5000"))
                .nodeName("testNode01")
                .externalId("externalId01")
                .benefitAddr("0xff48d9712d8a55bf603dab28f4645b6985696a61")
                .programVersion("1794")
                .bigVersion("1700")
                .webSite("web_site01")
                .details("details01")
                .isInit(1)
                .stakingBlockNum(new BigInteger("200"))
                .stakingTxIndex(0)
                .stakingAddr("0xb58c7fd25437e2fcf038b948963ffb80276bd44d")
                .joinTime(new Date(System.currentTimeMillis()))
                .txHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7")
                .build();
        return createStakingParam;
    }

    public ModifyStakingParam modifyStakingParam(){
        ModifyStakingParam modifyStakingParam = ModifyStakingParam.builder()
                .nodeName("testNode02")
                .externalId("externalId02")
                .benefitAddr("0xff48d9712d8a55bf603dab28f4645b6985696a61")
                .webSite("web_site01")
                .details("details01")
                .isInit(2)
                .txHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7")
                .stakingBlockNum(new BigInteger("200"))
                .bNum(new BigInteger("300"))
                .time(new java.sql.Date(System.currentTimeMillis()))
                .nodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18")
                .build();
        return modifyStakingParam;
    }

    public AddStakingParam addStakingParam(){
        AddStakingParam addStakingParam = AddStakingParam.builder()
                .amount(new BigDecimal("500000000000000000000000000"))
                .nodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18")
                .txHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7")
                .bNum(new BigInteger("300"))
                .time(new Date(System.currentTimeMillis()))
                .stakingBlockNum(new BigInteger("200"))
                .build();
        return addStakingParam;
    }

    public WithdrewStakingParam withdrewStakingParam(){
        WithdrewStakingParam withdrewStakingParam = WithdrewStakingParam.builder()
                .nodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18")
                .txHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7")
                .bNum(new BigInteger("300"))
                .stakingBlockNum(new BigInteger("200"))
                .time(new Date(System.currentTimeMillis()))
                .stakingReductionEpoch(3)
                .build();
        return withdrewStakingParam;
    }

    public ReportDuplicateSignParam reportDuplicateSignParam(){
        ReportDuplicateSignParam reportDuplicateSignParam = ReportDuplicateSignParam.builder()
                .time(new Date(System.currentTimeMillis()))
                .settingEpoch(3)
                .nodeId("0x20a090d94bc5015c9339a46e9ca5d80057a5ef25cc14e71cef67b502ec32949253f046821e80dfb6ff666ef0e0badf58fdb719368c38393f7c40ebcf18d8ed18")
                .stakingBlockNum(new BigInteger("200"))
                .bNum(new BigInteger("200"))
                .txHash("0xaa85c7e85542ac8e8d2428c618130d02723138437d105d06d405f9e735469be7")
                .slashRate("0.5")
                .slashData("json")
                .benefitAddr("0x02fba14f5e72092c8fca6ced087cd4e7be0d8fc5")
                .codeCurStakingLocked(new BigDecimal("50000000"))
                .codeNodeoptDesc("AMOUNT")
                .codeStatus(2)
                .codeRewardValue(new BigDecimal("1000000000"))
                .build();
        return  reportDuplicateSignParam;
    }
}
