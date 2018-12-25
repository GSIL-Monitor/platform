package cn.laeni.platform.user.service.impl;

import cn.laeni.platform.user.entity.Application;
import cn.laeni.platform.user.mapper.ApplicationMapper;
import cn.laeni.platform.user.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;

    /**
     * 根据Application的主键查询一条记录
     * @return
     */
    @Override
    public Application getApplicationByAppId(String appId) {
        return applicationMapper.selectByPrimaryKey(appId);
    }
}
