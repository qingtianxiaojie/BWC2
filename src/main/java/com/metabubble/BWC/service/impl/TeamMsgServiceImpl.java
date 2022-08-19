package com.metabubble.BWC.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metabubble.BWC.entity.TeamMsg;
import com.metabubble.BWC.mapper.TeamMsgMapper;
import com.metabubble.BWC.service.MerchantTypeService;
import com.metabubble.BWC.service.TeamMsgService;
import com.metabubble.BWC.service.TeamService;
import com.metabubble.BWC.utils.MobileUtils;
import org.springframework.stereotype.Service;

@Service
public class TeamMsgServiceImpl extends ServiceImpl<TeamMsgMapper, TeamMsg>
        implements TeamMsgService {


    @Override
    public void add(Long id,String tel , String msg) {
        String phone = MobileUtils.blurPhone(tel);
        TeamMsg teamMsg = new TeamMsg();
        teamMsg.setType(0);
        teamMsg.setUserId(id);
        teamMsg.setDownPhone(phone);
        teamMsg.setMsg(msg);
        this.save(teamMsg);
    }

    @Override
    public void addWithdrawals(Long id, String amount) {
        TeamMsg teamMsg = new TeamMsg();
        teamMsg.setType(2);
        teamMsg.setUserId(id);
        teamMsg.setMsg(amount);
        this.save(teamMsg);
    }

    @Override
    public void addRecharge(Long id, String amount) {
        TeamMsg teamMsg = new TeamMsg();
        teamMsg.setType(3);
        teamMsg.setUserId(id);
        teamMsg.setMsg(amount);
        this.save(teamMsg);
    }

    @Override
    public void addCashback(Long id, String tel, String amount) {
        String phone = MobileUtils.blurPhone(tel);
        TeamMsg teamMsg = new TeamMsg();
        teamMsg.setType(0);
        teamMsg.setUserId(id);
        teamMsg.setDownPhone(phone);
        teamMsg.setMsg(amount);
        this.save(teamMsg);
    }
}