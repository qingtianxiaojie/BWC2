package com.metabubble.BWC.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metabubble.BWC.entity.Orders;
import com.metabubble.BWC.entity.UserMsg;
import com.metabubble.BWC.mapper.UserMsgMapper;
import com.metabubble.BWC.service.UserMsgService;
import com.metabubble.BWC.utils.MobileUtils;
import org.springframework.stereotype.Service;

@Service
public class UserMsgServiceImpl extends ServiceImpl<UserMsgMapper, UserMsg>
        implements UserMsgService {
    @Override
    public void addUserCashback(Orders orders) {
        UserMsg userMsg = new UserMsg();
        userMsg.setUserId(orders.getUserId());
        String cashbackMsg = orders.getMerchantName()+"返现"+orders.getRebate();
        userMsg.setType(2);
        userMsg.setMsg(cashbackMsg);
        this.save(userMsg);
    }

    @Override
    public void addWithdrawals(Long id, String amount) {
        UserMsg userMsg = new UserMsg();
        userMsg.setUserId(id);
        userMsg.setType(0);
        userMsg.setMsg(amount);
        this.save(userMsg);
    }


    @Override
    public void addRecharge(Long id, String amount) {
        UserMsg userMsg = new UserMsg();
        userMsg.setUserId(id);
        userMsg.setType(1);
        userMsg.setMsg(amount);
        this.save(userMsg);
    }


    @Override
    public void overruleUserCashback(Orders orders) {
        UserMsg userMsg = new UserMsg();
        userMsg.setUserId(orders.getUserId());
        String cashbackMsg = "商家因"+orders.getReason()+"驳回了任务"+orders.getTaskName()+"的返现金额"+orders.getRebate();
        userMsg.setType(2);
        userMsg.setMsg(cashbackMsg);
        this.save(userMsg);
    }

    @Override
    public void overruleCashback(Long id, String tel, String amount) {
        UserMsg userMsg = new UserMsg();
        userMsg.setUserId(id);
        userMsg.setType(2);
        String s = MobileUtils.blurPhone(tel);
        String cashbackMsg = "用户"+s+"返现金额"+amount+"被驳回";
        userMsg.setMsg(cashbackMsg);
        this.save(userMsg);
    }
}
