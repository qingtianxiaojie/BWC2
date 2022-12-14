package com.metabubble.BWC.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDateTime;

/**
 * 商家任务
 */
@Data
public class TaskDto {

    //任务id
    private Long id;

    //商家id
    private Long merchantId;

    //商家名字
    private String merchantName;

    //商家图片
    private String pic;

    //商家电话
    private String tel;

    //任务名称
    private String name;

    //任务类型：0为早餐(默认)，1为午餐，2为下午茶，3为宵夜
    private Integer type;

    //平台类型：0为美团(默认)，1为饿了么
    private Integer platform;

    //任务是否需要评价：0为需要(默认)，1为不需要
    private Integer comment;

    //任务数量
    private Integer amount;

    //已完成任务数量
    private Integer completed;

    //任务剩余量
    private Integer taskLeft;

    //任务时间间隔
    private Integer timeInterval;

    //非会员最低消费
    private BigDecimal minConsumptionA;

    //非会员满减额
    private BigDecimal rebateA;

    //会员最低消费
    private BigDecimal minConsumptionB;

    //会员满减额
    private BigDecimal rebateB;

    //任务要求
    private String requirement;

    //任务备注
    private String remark;

    //是否启用，0为禁用(默认)，1为启用
    private Integer status;

    //任务开始时间
    private LocalDateTime startTime;

    //任务结束时间
    private LocalDateTime endTime;

    //营业开始时间
    private Time businessStartTime;

    //营业结束时间
    private Time businessEndTime;

    //任务创建时间
    private LocalDateTime createTime;
}