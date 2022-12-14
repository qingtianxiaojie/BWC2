package com.metabubble.BWC.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.metabubble.BWC.common.R;
import com.metabubble.BWC.entity.Config;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface ConfigService extends IService<Config> {

    /**
     * 一个根据id获取内容的静态类，不设置接口，可调用
     * @param id
     * @return
     */
    public R<String> getContentById(Long id);


    /**
     * 根据id直接获取内容
     * @param id
     * @return
     */
    public String getOnlyContentById(Long id);

    /**
     * 一个根据id获取详细内容的静态类，不设置接口，可调用
     * @param id
     * 晴天小杰
     * @return
     */
    public R<List> getContentsById(Long id);


}
