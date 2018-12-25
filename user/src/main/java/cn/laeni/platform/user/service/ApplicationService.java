package cn.laeni.platform.user.service;

import cn.laeni.platform.user.entity.Application;

/**
 * ApplicationService相关Service层
 * @author Laeni
 */
public interface ApplicationService {

    /**
     * 根据Application的主键查询一条记录
     * @return
     */
    Application getApplicationByAppId(String appId);
}
