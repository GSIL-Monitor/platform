package cn.laeni.platform.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.laeni.platform.user.mapper.UserIdWarehouseMapper;
import cn.laeni.platform.user.service.UserIdFactory;
import cn.laeni.platform.user.service.impl.userIdFactory.UserIdYun;
import cn.laeni.utils.string.CharacterUtils;

import java.util.HashMap;

/**
 * @author laeni.cn
 */
@Service
public class UserIdFactoryImpl implements UserIdFactory {

	/**
	 * 生产随机字符串的工具
	 */
	@Autowired
	CharacterUtils characterUtils;
	@Autowired
	UserIdWarehouseMapper idWarehouseMapper;

	/** 设置库中id的最小数量(如果小于该数量就生产ID) */
	@Value("${useridfactory.min-idle:10000}")
	private int min;
	/** 每次生成多少id */
	@Value("${useridfactory.single:10}")
	private int single;


	/** 获取一个id */
	@Override
	public String getUserIdAndDel() {
		// 启动一个线程生产ID
		UserIdYun userIdYun = new UserIdYun();
		// 设置库中id的最小数量
		userIdYun.min = this.min;
		// 设置随机字符串生成工具
		userIdYun.characterUtils = this.characterUtils;
		userIdYun.single = this.single;
		userIdYun.idWarehouseMapper = this.idWarehouseMapper;

		new Thread(userIdYun).run();

		// 通过存储过程获取一个Id并将其在原表中删除
		HashMap<String, String> userId = new HashMap<>(1);
		idWarehouseMapper.getUserIdAndDel(userId);
		//获取一个id
		return userId.get("userId");
	}



}
