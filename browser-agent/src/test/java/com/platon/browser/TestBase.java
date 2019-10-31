package com.platon.browser;//package com.platon.browser;


/**
 * @Auther: Chendongming
 * @Date: 2019/9/7 16:35
 * @Description:
 */
public class TestBase {

    static {
        System.setProperty("START_APP","false");
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

}
