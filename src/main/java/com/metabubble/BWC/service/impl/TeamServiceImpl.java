package com.metabubble.BWC.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metabubble.BWC.entity.Admin;
import com.metabubble.BWC.entity.Orders;
import com.metabubble.BWC.entity.Team;
import com.metabubble.BWC.mapper.AdminMapper;
import com.metabubble.BWC.mapper.TeamMapper;
import com.metabubble.BWC.service.AdminService;
import com.metabubble.BWC.service.TeamService;
import com.metabubble.BWC.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {

    private static final BigDecimal bigDecimalForFirstWithVip =new BigDecimal("0.5");
    private static final BigDecimal bigDecimalForFirstWithNtoVip =new BigDecimal("0.5");
    private static final BigDecimal bigDecimalForSecondWithVip =new BigDecimal("0.2");
    private static final BigDecimal bigDecimalForSecondWithNtoVip =new BigDecimal(0);

    @Autowired
    private UserService userService;

    /**
     * 团队向上一级返现
     * @param id 返现对象id
     * @author leitianyu999
     */
    @Override
    @Transactional
    public void cashbackForUserFromFirst(Long id) {
        Team team = this.getById(id);
        //查询是否为会员
        if (userService.checkGrade(id)) {
            //会员返现
            team.setTotalWithdrawnAmount(team.getTotalWithdrawnAmount().add(bigDecimalForFirstWithVip));
        }else {
            //非会员返现
            team.setTotalWithdrawnAmount(team.getTotalWithdrawnAmount().add(bigDecimalForFirstWithNtoVip));
        }
        this.updateById(team);
    }

    /**
     * 团队向上一级返现
     * @param id 返现对象id
     * @author leitianyu999
     */
    @Override
    @Transactional
    public void cashbackForUserFromSecond(Long id) {
        Team team = this.getById(id);
        //查询是否为会员
        if (userService.checkGrade(id)) {
            //会员返现
            team.setTotalWithdrawnAmount(team.getTotalWithdrawnAmount().add(bigDecimalForSecondWithVip));
        }else {
            //非会员返现
            team.setTotalWithdrawnAmount(team.getTotalWithdrawnAmount().add(bigDecimalForSecondWithNtoVip));
        }
        this.updateById(team);
    }
}
