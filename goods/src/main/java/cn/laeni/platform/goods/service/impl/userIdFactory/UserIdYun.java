package cn.laeni.platform.goods.service.impl.userIdFactory;

import cn.laeni.platform.goods.entity.UserIdWarehouse;
import cn.laeni.platform.goods.mapper.UserIdWarehouseMapper;
import cn.laeni.utils.string.CharacterUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laeni.cn
 */
public class UserIdYun implements Runnable {
	/** 是否在生产ID */
	Boolean is = false;

	public UserIdWarehouseMapper idWarehouseMapper;
	/**
	 * 生产随机字符串的工具
	 */
	public CharacterUtils characterUtils;
	/**
	 * 库中id的最小数量
	 */
	public int min;
	/**
	 * 每次生成id的数量
	 */
	public int single;

	@Override
	public void run() {
		/*
		这里可以不用加锁,即使同时启动两个线程也会太不影响性能
		 */
		if (!is) {
			// 将对象的状态设置为正在生产ID
			is = true;

			if(idWarehouseMapper.countUserIdWarehouse().getCount() < this.min) {
				List<UserIdWarehouse> list = new ArrayList<UserIdWarehouse>();

				for(int i=0;i<10;i++) {
					UserIdWarehouse idWarehouse = new UserIdWarehouse();
					// 生成一个10位随机字符串
					idWarehouse.setUserId("1"+characterUtils.getRandomString(8,"012356789"));
					// 设置当前时间戳
					idWarehouse.setAddTime(System.currentTimeMillis() / 1000);
					list.add(idWarehouse);
				}
				
				idWarehouseMapper.insertUserIdWarehouse(list);
				// 判断用户表和仓库表中是否有值,如果没有则尝试存入
			}

			is = false;
		}

	}

}
