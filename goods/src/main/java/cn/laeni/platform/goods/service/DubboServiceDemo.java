package cn.laeni.platform.goods.service;

import cn.laeni.platform.goods.mapper.UserMapper;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Dubbo测试Service
 * @author Laeni
 */
@Service
public class DubboServiceDemo implements DubboServiceInterface {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String getUser() {
        return userMapper.findUserByUserId("107262398").toString();
    }
}
