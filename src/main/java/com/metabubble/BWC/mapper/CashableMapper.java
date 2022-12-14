package com.metabubble.BWC.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.metabubble.BWC.dto.CashableDto;
import com.metabubble.BWC.entity.Cashable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface CashableMapper extends BaseMapper<Cashable> {

    @Select("SELECT cashable.id,cashable.user_id,cashable.trade_no,cashable.cashable_amount,cashable.pay_type," +
            "cashable.status,cashable.withdraw_reason,cashable.create_time,cashable.update_time,cashable.admin_id," +
            "user.ali_pay_id,user.ali_pay_name,user.wx_id,user.status AS userStatus,user.withdrawn_amount,user.tel" +
            " FROM cashable" +
            " JOIN user" +
            " on cashable.user_id=user.id" +
            " ${ew.customSqlSegment}" +
            " ORDER BY cashable.create_time DESC")
    IPage<CashableDto> dto(Page<CashableDto> page, @Param(Constants.WRAPPER) QueryWrapper<Object> wrapper);

}
