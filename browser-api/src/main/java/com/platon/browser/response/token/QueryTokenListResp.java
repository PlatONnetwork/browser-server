//package com.platon.browser.response.token;
//
//import com.platon.browser.utils.ConvertUtil;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.experimental.Accessors;
//
//import java.math.BigDecimal;
//import java.util.Date;
//
///**
// * 合约列表数据响应报文
// *
// * @author AgentRJ
// * @create 2020-09-23 14:39
// */
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Accessors(chain = true)
//public class QueryTokenListResp {
//
//    @ApiModelProperty(value = "合约地址")
//    private String address;
//    @ApiModelProperty(value = "合约名称")
//    private String name;
//    @ApiModelProperty(value = "合约符号")
//    private String symbol;
//    @ApiModelProperty(value = "合约精度")
//    private Integer decimal;
//    @ApiModelProperty(value = "合约发行总量")
//    private BigDecimal totalSupply;
//    @ApiModelProperty(value = "合约图标（base64编码）")
//    private String icon;
//    @ApiModelProperty(value = "合约创建者")
//    private String creator;
//    @ApiModelProperty(value = "合约部署所在哈希")
//    private String txHash;
//    @ApiModelProperty(value = "合约对应官方网站")
//    private String webSite;
//    @ApiModelProperty(value = "持有人数")
//    private Integer holder;
//    @ApiModelProperty(value = "合约创建时间")
//    private Date blockTimestamp;
//    @ApiModelProperty(value = "交易记录时间")
//    private Date createTime;
//
//    public static QueryTokenListResp fromErc20Token(Erc20Token token) {
//        return QueryTokenListResp.builder()
//                .address(token.getAddress()).name(token.getName())
//                .symbol(token.getSymbol()).decimal(token.getDecimal())
//                .totalSupply(ConvertUtil.convertByFactor(token.getTotalSupply(), token.getDecimal()))
//                .creator(token.getCreator()).txHash(token.getTxHash())
//                .blockTimestamp(token.getBlockTimestamp())
//                .createTime(token.getCreateTime())
//                .holder(token.getHolder())
//                .build();
//    }
//}
