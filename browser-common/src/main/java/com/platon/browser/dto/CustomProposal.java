package com.platon.browser.dto;

import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.Vote;
import com.platon.browser.param.CreateProposalParameterParam;
import com.platon.browser.param.CreateProposalTextParam;
import com.platon.browser.param.CreateProposalUpgradeParam;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/16 15:34
 * @Description:
 */
@Data
public class CustomProposal extends Proposal {

    //赞成票
    private List <CustomVote> yesList = new ArrayList <>();
    //否决票
    private List <CustomVote> noList = new ArrayList <>();
    //弃权票
    private List <CustomVote> abstentionList = new ArrayList <>();


    public void bulidStructure ( Proposal proposal ) {
        BeanUtils.copyProperties(proposal, this);
    }

    public void updateWithProposalText ( CustomTransaction tx, CreateProposalTextParam param ) {
        this.setHash(tx.getHash());
        this.setYeas(0L);
        this.setNays(0L);
        this.setAbstentions(0L);
        this.setEndVotingBlock(param.getEndVotingBlock().toString());
        this.setCreateTime(new Date());
        this.setUpdateTime(new Date());
        this.setType(String.valueOf(TypeEnum.TEXT.code));
        this.setUrl("");
        this.setNewVersion("");
        this.setParamName("");
        this.setCurrentValue("");
        this.setActiveBlock("");
        this.setTimestamp(tx.getTimestamp());
        this.setAccuVerifiers(0L);
        this.setNewValue("");
        this.setStatus(StatusEnum.VOTEING.code);
        this.setVerifier(param.getVerifier());
    }


    public void updateWithProposalUpgrage ( CustomTransaction tx, CreateProposalUpgradeParam param ) {
        this.setHash(tx.getHash());
        this.setYeas(0L);
        this.setNays(0L);
        this.setAbstentions(0L);
        this.setEndVotingBlock(param.getEndVotingBlock().toString());
        this.setCreateTime(new Date());
        this.setUpdateTime(new Date());
        this.setType(String.valueOf(TypeEnum.UPGRADE.code));
        this.setUrl(param.getUrl());
        this.setNewVersion(String.valueOf(param.getNewVersion()));
        this.setParamName("");
        this.setCurrentValue("");
        this.setActiveBlock(String.valueOf(param.getActiveBlock()));
        this.setTimestamp(tx.getTimestamp());
        this.setAccuVerifiers(0L);
        this.setNewVersion("");
        this.setStatus(StatusEnum.VOTEING.code);
        this.setVerifier(param.getVerifier());
    }

    public void updateWithProposalParam ( CustomTransaction tx, CreateProposalParameterParam param ) {
        this.setHash(tx.getHash());
        this.setYeas(0L);
        this.setNays(0L);
        this.setAbstentions(0L);
        this.setEndVotingBlock(param.getEndVotingBlock().toString());
        this.setCreateTime(new Date());
        this.setUpdateTime(new Date());
        this.setType(String.valueOf(TypeEnum.UPGRADE.code));
        this.setUrl(param.getUrl());
        this.setNewVersion(String.valueOf(param.getNewVersion()));
        this.setParamName(param.getParamName());
        this.setCurrentValue(param.getCurrentValue());
        this.setActiveBlock("");
        this.setTimestamp(tx.getTimestamp());
        this.setAccuVerifiers(0L);
        this.setNewValue(param.getNewVersion());
        this.setStatus(StatusEnum.VOTEING.code);
        this.setVerifier(param.getVerifier());
    }

    public enum OptionEnum {
        SUPPORT(1, "支持"),
        OPPOSITION(2, "反对"),
        ABSTENTION(3, "弃权");
        public int code;
        public String desc;

        OptionEnum ( int code, String desc ) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode () {
            return code;
        }

        public String getDesc () {
            return desc;
        }

        private static Map <Integer, OptionEnum> ENUMS = new HashMap <>();

        static {
            Arrays.asList(OptionEnum.values()).forEach(en -> ENUMS.put(en.code, en));
        }

        public static OptionEnum getEnum ( Integer code ) {
            return ENUMS.get(code);
        }

        public static boolean contains ( int code ) {
            return ENUMS.containsKey(code);
        }

        public static boolean contains ( OptionEnum en ) {
            return ENUMS.containsValue(en);
        }
    }

    public enum TypeEnum {
        TEXT(1, "文本提案"),
        UPGRADE(2, "升级提案"),
        PARAMETER(3, "参数提案");
        public int code;
        public String desc;

        TypeEnum ( int code, String desc ) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode () {
            return code;
        }

        public String getDesc () {
            return desc;
        }

        private static Map <Integer, TypeEnum> ENUMS = new HashMap <>();

        static {
            Arrays.asList(TypeEnum.values()).forEach(en -> ENUMS.put(en.code, en));
        }

        public static TypeEnum getEnum ( Integer code ) {
            return ENUMS.get(code);
        }

        public static boolean contains ( int code ) {
            return ENUMS.containsKey(code);
        }

        public static boolean contains ( TypeEnum en ) {
            return ENUMS.containsValue(en);
        }
    }

    public enum StatusEnum {
        VOTEING(1, "投票中"),
        PASS(2, "通过"),
        FAIL(3, "失败"),
        PREUPGRADE(4, "预升级"),
        FINISH(5, "升级完成");
        public int code;
        public String desc;

        StatusEnum ( int code, String desc ) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode () {
            return code;
        }

        public String getDesc () {
            return desc;
        }

        private static Map <Integer, StatusEnum> ENUMS = new HashMap <>();

        static {
            Arrays.asList(StatusEnum.values()).forEach(en -> ENUMS.put(en.code, en));
        }

        public static StatusEnum getEnum ( Integer code ) {
            return ENUMS.get(code);
        }

        public static boolean contains ( int code ) {
            return ENUMS.containsKey(code);
        }

        public static boolean contains ( TypeEnum en ) {
            return ENUMS.containsValue(en);
        }
    }
}
