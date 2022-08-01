package com.metabubble.BWC.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.metabubble.BWC.entity.Orders;
import com.metabubble.BWC.entity.Team;
import com.metabubble.BWC.entity.User;

public interface TeamService extends IService<Team> {

    //向上一级返现
    public void cashbackForUserFromFirst(Long id);
    //向上二级返现
    public void cashbackForUserFromSecond(Long id);
    //创建团队表
    public void save(Long id);
    //团队添加上一级和上二级
    public void addTeamTop(User user,User topUser);
}
