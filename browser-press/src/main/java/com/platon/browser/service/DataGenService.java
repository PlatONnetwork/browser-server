package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.*;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.utils.EpochUtil;
import com.platon.browser.utils.HexTool;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Data
@Service
public class DataGenService {

    private static final List<String> PROPOSAL_HASH = new ArrayList<>();
    private static final List<String> NODE_ID = new ArrayList<>();
    private static final List<String> ADDRESS = new ArrayList<>();

    private static final String [] NODE_IDS={
            "0x00193cef7c6ca8e461a1a37602be6717a8c98d8b7db7dd931c90dca7d36168660781e0d606df7f3b3aca70e2e9d0eee193f83d8fd4039fc7953f636ee5462610",
            "0x00d6f98040f3f6419f9bd609a5e5c26ad8eef044e1f958fdddf7489442244e65ca38b255c8fc303990acaa5ce04836a00869a81829bc7324b4a0e8376092d4a4",
            "0x069e9945f78abd1ea64129cd762f714fb5aec8a8af1bbc0d2efab6c5960d72e2e0e331e4110d54ec9ec8836bdf5b77ce817f8902c13d3aa48071575e4d4ec79a",
            "0x0732c6f705cca61fe7074d0daa4a5d585a46d4d4b935c990e309a292d61271b63d1336eff9629ef312dae85ae4cc50c68cc8660ff12aa2034084604572570aa9",
            "0x07a7d28cfe5e19ac5a34711be9c255075b92ca6251fe1393a638c2f9573f2f0e429eac69be26aabfdde78a3552703c2ef9f046d84b03a90eff981f26ecde3828",
            "0x0878a61b503dd5a9fe9ea3545d6d3bd41c3b50a47f3594cb8bbab3e47558d68fc8fcc409cd0831e91afc4e609ef9da84e0696c50354ad86b25f2609efef6a834",
            "0x08856a9022cc1f4b7c90b2d059e64acb6f6c5ac11da907d86db6a3072e9d821c59603c1ea94a2e537bea0a38320d678c482a66eaaf1a79c4d3432ea41e51b721",
            "0x08b4746e5b8da217b3b3e37d4bab9139ea7694ed75737c4987c78571141f15cbebb515a5fa4dd5fefe0afaba98f2c8785459a62bcc278b2fb463840b88c9f223",
            "0x08c2c07edcd7c5f0feb33b7e5fb05b0f8fe2336d8b41edbc23d97cc600e638dda9b7a22d99a0331d6e5c7584ff350dd219a54b3910a6b113f87724a6844cd605",
            "0x0ae95cabf29c78f97c074af98165719c8c07f0bc4295335010dc5ce87e2aa824e5f96534795712b873d4a914c25602bb0b172ffb75f95fd9a6b3cbea396d140a",
            "0x0c98924aee1ae786b0ab5d2fb0260937769c38bdd989fa03f1aee709b218ab1c98bd1d1b26c4f80aeb9c07e8b322655d39cc607b6a98d50ed2ae73cfa3847d41",
            "0x0d15d65c1a988dfc8cc58f515a9bb56cbaf1ff5cb0a5554700bc9af20a26c0470a83c8eb46e16175154a6bcaad7e280bbfd837a768f9f094da770b7bd3849f88",
            "0x0d3ee8c2f659c24ae2948f139295d6a4c4c48134c722dc020c6c9e72e88b395eae0dd19f27642608ae0f981351bc0f6b2b3e9813c70bc7e4f2e8251a6ec1b4af",
            "0x0d9644d4343a61d259e61f380888142ebaecac451f732cbe67bf73a62489c8fa0c4ddc97a64860d66298a926ada5b4c9b4ed7f40e4cb16edbe25f957480fb866",
            "0x0dcff5a44cd72c19f94f7b72a5a7766ba5674afb9c13a9085a0ae03848d6a09c2bc0a0ca9660c0aa124b179ec6e84fb9af1121e7f0441705e052d6a6b2f87a7e",
            "0x0fc21309daa09e42aa7b2b6565596920df5e35af009d86d76102205fadddeb5b171e0fa72ebbfb85152a90085074686434f0ace5b4d1dcb9c40f05670b5cfdd6",
            "0x10328270c5a4c574ac641a07d23ae70d7c7649320489bd50a4f6195d1120cd4a4255f7d512555864ce57ac7c9de8fe9f18df7cf028b16613f66199872e58f581",
            "0x1139d1a1fa6b7cf169e0c75542666130eec3b30c7a13bbce2dbd832bcc61ae05668d4453cd2240ac31fe71b7577fc34eb31d78b2c53eeefd22751e94842cd2f7",
            "0x121aa909d479ec333b64e0831b98aa067eb9daff0c14f507a3f58af699f29d3df4d791f7264762f9aafcad8a3b898c2d836290097ccff8ad8dc6d614d0412133",
            "0x14c34e7007372d6c6e05daec4706ed8d4df3ffe0dec73410bb9232d77777890b800bcd826317e98a52c17c7e5dfcb4450caaac4d0a7bebc582eb3366381e391b",
            "0x14f70566435cea4309176ad6a8aebb69ac8f99e9e211df66227522b5bb37c7a52e1f4de42543e4bb5346dbce23a636c7237a42e67ff4888befcc2167f7c2b451",
            "0x190430d9de4a918dd3315e07f7ff8260408b313c4cc5425ec0f9c1fc305ebfcc6b199d10fc3bb1f79a2558ba686bf582a3ebbfe30cfae268063911be0fa3677c",
            "0x1b62cdf14e227b37eb72e0ae3207bd1159b1a972c5cd1d636d2a74728c8cb0b949336b3c9967be7ca22d55f319a0a820776128dba21482c27c66c91fa2a94f02",
            "0x1bed31834bf52664834cef4fea87716aca1003fef92be0710c5783718a9ffb6ec276ec66a74bd186f04d20b56ba9bd4ad8ee1b2cb369022ea22d2e192536dc15",
            "0x1cda9a6906ca0a0ea743d638f2ac144c32169b12ee245492c7bc26bc58980dc340014214b21365e59f25709341bd56348565ba08adaa3a52fc8be5d59f84cf25",
            "0x1d7d426480534455c5601d317a2a95fbb67f178650ab8a7f7d2285884ca133c0de87d07e9d979596cb242f2f9a1e32689748eef2493a0b974aff523205ab7050",
            "0x218a37ec708da09b620d50d80339257bd3d937560b4d898595bf887486cc3ce558a393c4051058001589bb20d39b3ba9f03ea96b20580a86fb97a797bf1a07ab",
            "0x2208bacc005d7aab19bbe03d995f5ac696b8136c5469f662a7fe383458ecf17406fa0bce8064aa7d4df8d75b84010888d94aa07686ffac15750ad4e770d787f0",
            "0x2285f9b039a58936434ea68d3cd49a6150b0b0bd31c85da87b7e8547c139c1258998f622f91ff4ef6f348a7225251cb67eb81fb151ae2906a55c659b45a1ead9",
            "0x236de81fa49a808e7f82b540b4297b7b0bac73e12baac4f4a9c2a5b03849accb8ddad2ca4ad5f332eed24c6cdf767bbe2a7b84d8ca1fda816eaff9791713967f",
            "0x23bd25be756164b4aef946f166e2aa434a7ce176aa9355783f33b7295bfba629faa484c443eabcc902503a2b6accce20ac8eb4391906da07c65740a8ba6526e9",
            "0x252bde772aaf6cac0fc36fbf06f51451cbfc488ab9eed946a7a063014754df365d7284c7d360ffcfd8d0655ccb97c9f8a407610f0c7e285ecd52f51b1767fea0",
            "0x26344e270809236abfb6f5ecff13d93d7ecb570a4d2003515abb3aed1c5af45f0fb064b17551d6570a60c14eeac482e54998565def6eaafc61399ba08398c929",
            "0x27599e3b992bde212acd6d70513f49c7fbfe3a2222a90dbf6ce868673b8a6b33b8f42087a8c2078708aff13edfafaccebb616d8426a4bb9f105058f060d30727",
            "0x27e9f42776aedeaa6682290be089643310f33e6a635b9b9617c09fdc513b1f0f21c868f1ed7269e452b242879ade0c43145d8aaf1c3f7e21c5b1e2fecadcd15a",
            "0x289c89c1dee59a6fba00572c0f12912258513102334f87926069a0dd90622b556c31d2f349ac7cc0f4f06859f64ef5051ffdc7de602ca3d46ddc3c805ee9e4d6",
            "0x28aed5f1c460fd78f5277aba7cb38ef1b8a1073711530a054928962f9e1808a3b5e3b90f350e44e97fa854e1ba213728cde0a439691aad64a82254e17837fd89",
            "0x29c668dd940b6f08594373125067f434f7285c6f8bfabc230872c610f836e6e87a2085c87abf1b8670a29dcc4ab4b854e6b856f5af8acc580e20a5503c90720d",
            "0x29e8529df01d63d69bfb72b172915c6a1a67f7250760332394c3521f69032a1ecc4faa0442e4b5598fd3c70cef524c9be9033d19aa0f45e8d6254d789ad8524c",
            "0x2a0b27f5d6446e99fbe864b50f97a6fb6cfbe435b19bc645bc262deca6cf42b635ac0e24d9a91451f2ed03412b6ade3eca514fb2ee542c7e21de48ad6baf8f5f",
            "0x2c9b2d5724d07fa85cc048bd7558e077467d4619b3c319882ad3053e9d4687bb06e0e3336987d81c8b90c4c22be92ce33f2b46815cd64dec5d71c41afd71f3be",
            "0x2ca24e7c519581c39ed23db916c510190ee83e3e19981da3a5578554b14b3d97ddef8ccc98c1dfcbbb78185c52c5c2d2a3c84ef34064c2f7b54bc858263f508a",
            "0x2ceff513af088c690770a7f21e6a28a6f86e1536db7bbc67719731b5e0be6ba5c00d6c670cf56ec822ac506c43049b0c86dffa0ff80b347f81656d1af32cc0b4",
            "0x2ee9c2ba241438aab171eb43b1b8cf3404a3e28bfc03bfc35d35ef97ed7a370aca66a6368ac227facd90f3b31c8bb632c5bce2682f27fdd86888828e2d3d38b7",
            "0x3163a8d6a4540ecf1794ece0245f291154d30e1080359d2e994ef79c1a469aa0cd808769d9c7ee30ca342c6803d2ebcec3eb71a928d6db187dfb1fc2cf640395",
            "0x31bca02094eb78126a517b206a88c73cfa9ec6f704c7030d18212cace820f025f00bf0ea68dbf3f3a5436ca63b53bf7bf80ad8d5de7d8359d0b7fed9dbc3ab99",
            "0x34dd71c5ad93e0ca75a02dad8320830462b466d1bd03a614213b438dac7e7eb77765183bde83bdb77adbcdd735c79e3cc6424ee2d6d59a81f0cad28653cd7b58",
            "0x35701082911673403b80fd0d8ab5a7d666384e781d842efe3c2355bd7f551a0a649529badccd4c592d248d439e81f7f816153e7e3cfd356752dc5c45db301cc0",
            "0x364ab93bac5c26bcc08811259b1b2832e0e0d38a0ebd46bd034d896ed26e1626ee2d5d434acf25f4e2553fa1e04c2fdf8f0b488930cfbed4d46eb6d273fc44ec",
            "0x375874cf7de911dc08664f77615c8e1765c720b490ee58fbe04e8afba82e2ea8b4965e8ea6a3c8db657c5d2aac2ca9cf991c4722ab78421170fcf0bb51aaf84b",
            "0x3c11e4f316c956a27655902dc1a19b925b8887d59eff791eea63edc8a05454ec594d5eb0f40ae151df87acd6e101761ecc5bb0d3b829bf3a85f5432493b22f37",
            "0x3c31b9cf56c4c47f7c95c6ca9d1669c2cbb127220dcd804c42fcf68714a8c38806267e72542d47532598e52090219677cf38bb5d906da1e7a2a2b351b871a82f",
            "0x3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2",
            "0x3c9ad55147a7144f6067327c3b82ea70e7c5426add9ceea4d07dc2902239bf9e049b88625eb65d014a7718f79354608cab0921782c643f0208983fffa3582e40",
            "0x3dfe103eb6b0d68bc69969f9968ad2323c45dcbb8b132d9f481e90575c4a51ca94dd3ff425bd56cb176fc92932dd06fffd7c98515bdfee2c07706cdd2d15d731",
            "0x3e2d9dae2cbca5078feb27be19d5d78eb49363424356d572c3e097758af38536102d36b08c2b8bcbcfabb9340c14d6d607ac78b31f8c7f72894d061fbc062105",
            "0x3f021d45ac74ef504d609aba7c66cf77435fa502f6baef0d9c63bb97e204cd2a589bf0b82c28939d6b465c0d3fe021917646bbeacb42b8a392cbdb22b0bfc1ac",
            "0x40a5b90ccb302b50ff2610f3231fabf263e0ea3a23372035cf856ea4b27951da3e1dbf05f0856c0ffa01bc57f256a418fe213d99df55b90e3ecc3da6042dc032",
            "0x43b90c4a10829b651a281bf92db8830e19b78f26c52a73be497cd617fdaae000bcc52118ad85a8ce72852d3301cf9f5aa4f6bc82e63743a9912da5916ac2de79",
            "0x46e59410cf5010798015775e98b4aff99a6d410d2322a6498fcf428d119ac463ccb70af91fe7404a6ab0fce33d2bc3a570a19c8dc06f75cf61d8ca10ff8b1275",
            "0x4753a59325e15c0b830acb5dc0652c0402f4a76c6de65e366fedaface48d14ddb364172c70d3c45c1a9ceca4a2f735bac28a0e1fb4b5cb595371a3c475deb60d",
            "0x4774b6224b8e98b96b658092bee32c88c41b1a8c80dcfd7e1fdffc7be59c5f72eae3aecac37b0c7398154489066b0b022240a68daf4432849fabe75768faaf5e",
            "0x4983410f7172a8c6384b3f36a80263288941c7b206e3005fedf349587ac89cfd7b56d73bf5765a5965e08047f9129ba5d897324e311d7ff24b8b4d9ed347a4bf",
            "0x4ab93edabdd30c8209b13cefa76b66d3e9ed0f12d7aee88c289098ff08c2919f355d4e3a154f4dff5343a30b01a97ba5fa2d5f2d083c3e5878d301037aa2c6fc",
            "0x4b21e17f2926a92986ecece848f3eee43f23baca5017ef508ad4fd85735d066dfd498d97ee3fd5ff8f92d892bdb11c3b1611303582833433361acbf51b569698",

    };

    static class Status{
        public Status(int status, int isConsensus, int isSettle, int isInit) {
            this.status = status;
            this.isConsensus = isConsensus;
            this.isSettle = isSettle;
            this.isInit = isInit;
        }
        private int status;
        private int isConsensus;
        private int isSettle;
        private int isInit;
    }

    private static final Status[] STATUSES_ARR = {
            new Status(1,1,1,2),
            new Status(1,1,1,2),
            new Status(2,1,1,2),
            new Status(2,2,2,2),
            new Status(3,2,2,2),
            new Status(3,1,2,2),
    };

    private static final Random random = new Random();
    public static String randomNodeId(){
        return NODE_IDS[random.nextInt(NODE_IDS.length)];
    }

    private String blockListStr;
    private String transactionListStr;
    private String nodeOptListStr;
    private String nodeStr;
    private String stakingStr;
    private String delegationStr;
    private String proposalStr;
    private String voteStr;
    private String rpPlanStr;
    private String slashStr;

    private NetworkStat networkStat;
    private List<Config> configList;

    @PostConstruct
    private void init() {
        URL dirUrl = DataGenService.class.getClassLoader().getResource("data");
        String dirPath = dirUrl.getPath();
        File dir = new File(dirPath);
        Arrays.asList(dir.listFiles()).forEach(file -> {
            try {
                String content = FileUtils.readFileToString(file,"UTF-8");
                switch (file.getName()){
                    case "block.json":
                        blockListStr = content;
                        break;
                    case "transaction.json":
                        transactionListStr = content;
                        break;
                    case "nodeopt.json":
                        nodeOptListStr = content;
                        break;
                    case "node.json":
                        nodeStr=content;
                        break;
                    case "staking.json":
                        stakingStr=content;
                        break;
                    case "networkstat.json":
                        networkStat=JSON.parseObject(content, NetworkStat.class);
                        break;
                    case "delegation.json":
                        delegationStr=content;
                        break;
                    case "config.json":
                        configList=JSON.parseArray(content,Config.class);
                        break;
                    case "proposal.json":
                        proposalStr=content;
                        break;
                    case "vote.json":
                        voteStr=content;
                        break;
                    case "rpplan.json":
                        rpPlanStr=content;
                        break;
                    case "slash.json":
                        slashStr=content;
                        break;
                }
            } catch (IOException e) {
                log.error("读取文件内容出错",e);
            }
        });
    }

    private long nodeOptMaxId = 0L;

    @Value("${platon.addressReusedTimes}")
    private int addressReusedTimes;

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public BlockResult getBlockResult(BigInteger blockNumber){
        BlockResult br = new BlockResult();

        Block block = JSON.parseObject(blockListStr,Block.class);
        br.setBlock(block);

        List<Transaction> transactionList = JSON.parseArray(transactionListStr,Transaction.class);
        br.setTransactionList(transactionList);

        List<NodeOpt> nodeOptList = JSON.parseArray(nodeOptListStr,NodeOpt.class);
        br.setNodeOptList(nodeOptList);

        String nodeId = randomNodeId();
        br.buildAssociation(blockNumber,nodeId,++nodeOptMaxId,addressReusedTimes);

        return br;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public StakeResult getStakeResult(Transaction tx,Long nodeSum,Long stakeSum) throws BlockNumberException {
        Node node = JSON.parseObject(nodeStr,Node.class);
        node.setStakingBlockNum(tx.getNum());
        node.setNodeId(HexTool.prefix(DigestUtils.sha512Hex(stakeSum.toString())));
        node.setNodeIcon(node.getNodeId().substring(0,6));
        node.setNodeName(node.getNodeId().substring(7,10));

        Status status = STATUSES_ARR[random.nextInt(STATUSES_ARR.length)];
        node.setStatus(status.status);
        node.setIsConsensus(status.isConsensus);
        node.setIsSettle(status.isSettle);
        node.setIsInit(status.isInit);
        node.setStakingTxIndex(tx.getIndex());
        node.setStakingAddr(HexTool.prefix(DigestUtils.sha1Hex(stakeSum.toString())));
        node.setStatVerifierTime(EpochUtil.getEpoch(BigInteger.valueOf(tx.getNum()),BigInteger.TEN).intValue());

        if(ADDRESS.size()<5000&&!ADDRESS.contains(tx.getFrom())){
            ADDRESS.add(tx.getFrom());
        }

        Staking staking = JSON.parseObject(stakingStr,Staking.class);
        BeanUtils.copyProperties(node,staking);

        StakeResult stakeResult = new StakeResult();
        stakeResult.setNode(node);
        stakeResult.setStaking(staking);
        return stakeResult;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Delegation getDelegation(Transaction tx,Long totalCount){
        Delegation copy = JSON.parseObject(delegationStr, Delegation.class);
        copy.setStakingBlockNum(tx.getNum());
        copy.setNodeId(HexTool.prefix(DigestUtils.sha512Hex(totalCount.toString())));
        copy.setDelegateAddr(ADDRESS.get(random.nextInt(ADDRESS.size())));
        return copy;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Proposal getProposal(Transaction tx,Long totalCount){
        Proposal copy = JSON.parseObject(proposalStr, Proposal.class);
        copy.setHash(tx.getHash());
        copy.setNodeId(HexTool.prefix(DigestUtils.sha512Hex(totalCount.toString())));
        copy.setNodeName(copy.getNodeId().substring(7,10));
        copy.setName(copy.getNodeId().substring(0,6));
        // 1文本提案,2升级提案,3参数提案,4取消提案
        switch (tx.getTypeEnum()){
            case PROPOSAL_TEXT:
                copy.setType(1);
                break;
            case PROPOSAL_CANCEL:
                copy.setType(4);
                break;
            case PROPOSAL_PARAMETER:
                copy.setType(3);
                break;
            case PROPOSAL_UPGRADE:
                copy.setType(2);
                break;
        }
        copy.setBlockNumber(tx.getNum());
        PROPOSAL_HASH.add(copy.getHash());
        NODE_ID.add(copy.getNodeId());
        return copy;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Vote getVote(Transaction tx,Long totalCount){
        Vote copy = JSON.parseObject(voteStr, Vote.class);
        copy.setNodeId(HexTool.prefix(DigestUtils.sha512Hex(totalCount.toString())));
        copy.setNodeName(copy.getNodeId().substring(7,10));
        copy.setHash(tx.getHash());
        copy.setProposalHash(PROPOSAL_HASH.get(random.nextInt(PROPOSAL_HASH.size())));
        return copy;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public RpPlan getRpPlan(Transaction tx,Long totalCount) throws BlockNumberException {
        RpPlan copy = JSON.parseObject(rpPlanStr, RpPlan.class);
        copy.setId(null);
        copy.setEpoch(EpochUtil.getEpoch(BigInteger.valueOf(tx.getNum()),BigInteger.TEN).longValue());
        copy.setAddress(ADDRESS.get(random.nextInt(ADDRESS.size())));
        return copy;
    }

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public Slash getSlash(Transaction tx,Long totalCount){
        Slash copy = JSON.parseObject(slashStr, Slash.class);
        copy.setHash(tx.getHash());
        copy.setBenefitAddr(ADDRESS.get(random.nextInt(ADDRESS.size())));
        copy.setNodeId(NODE_ID.get(random.nextInt(NODE_ID.size())));
        return copy;
    }
}
