package com.metabubble.BWC.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class CashableDto {
    //支付宝ID
    private String aliPayId;

    //支付宝名称
    private String aliPayName;
    //序列化
    private static final long serialVersionUID = 1L;

    //提现编号
    private Long id;

    //转账订单号
    private Long tradeNo;

    //提现金额
    private BigDecimal cashableAmount;

    //提现类型 1,支付宝(默认) 2，微信
    private Integer payType;

    //提现状态 1，待转账(默认) 2，已转账 3,已取消
    private Integer status;

    //退款原因
    private String withdrawReason;

    //申请时间
    private LocalDateTime createTime;

    //转账、退款时间
    private LocalDateTime updateTime;

}