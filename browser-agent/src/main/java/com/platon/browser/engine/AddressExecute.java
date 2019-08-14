package com.platon.browser.engine;

import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dto.TransactionBean;
import com.platon.browser.enums.AddressEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: dongqile
 * Date: 2019/8/14
 * Time: 17:14
 */
@Component
public class AddressExecute {
    private static Logger logger = LoggerFactory.getLogger(AddressExecute.class);

    @Autowired
    private AddressMapper addressMapper;

    private Map <String, Address> addresss = new HashMap <>();

    private AddressExecuteResult addressExecuteResult = new AddressExecuteResult();

    @PostConstruct
    private void init () {
        // 初始化全量数据
        List <Address> addresseList = addressMapper.selectByExample(null);
        addresseList.forEach(address -> addresss.put(address.getAddress(), address));
    }

    public void execute ( TransactionBean tx, BlockChain bc ) {
        //入库前对address进行数据分析统计
        //数据来源是具体分析后的最后结果

        if (addresss.get(tx.getFrom()).equals(null)) {
            Address address = new Address();
            address.setAddress(tx.getFrom());
            //todo：主动发起交易的都认为是账户地址因为当前川陀版本无wasm
            address.setType(AddressEnum.ACCOUNT.getCode());
            address.setTxQty(1);
            switch (tx.getTypeEnum()){
                case TRANSFER:
                    address.setTransferQty(1);
                    break;
                case CREATEPROPOSALPARAMETER:// 创建参数提案
                case CREATEPROPOSALTEXT:// 创建文本提案
                case CREATEPROPOSALUPGRADE:// 创建升级提案
                case DECLAREVERSION:// 版本声明
                case VOTINGPROPOSAL:// 提案投票
                    address.setProposalQty(1);
                    break;
                case DELEGATE:// 发起委托
                    address.setCandidateCount(1);
                    address.setDelegateQty(1);
                    break;
                case UNDELEGATE:// 撤销委托
                    address.setDelegateQty(1);
                    break;
                case INCREASESTAKING:// 增加自有质押
                case CREATEVALIDATOR:// 创建验证人
                case EXITVALIDATOR:// 退出验证人
                case REPORTVALIDATOR:// 举报验证人
                case EDITVALIDATOR:// 编辑验证人
                    address.setStakingQty(1);
                    break;
            }

        }
        if (addresss.get(tx.getTo()).equals(null)) {

        }

    }

    public AddressExecuteResult exportResult () {
        return addressExecuteResult;
    }

    public void commitResult () {
        addressExecuteResult.getAddAddress().clear();
        addressExecuteResult.getUpdateAddress().clear();
    }

}