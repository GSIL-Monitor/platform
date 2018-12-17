package cn.laeni.platform.goods.mapper;

import cn.laeni.platform.goods.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional//使用事物管理进行回滚
@Rollback//该测试方法在执行结束后，自动回滚，必须配合@Transactional注解一起使用。
public class UserMapperTest {

	// Mapper
	@Autowired
	UserMapper userMapper;
	@Autowired
	UserIdWarehouseMapper userIdWarehouseMapper;
	@Autowired
	PasswordMapper passwordMapper;
	@Autowired
	LoginNameMapper loginNameMapper;
	@Autowired
	LoginPhoneMapper loginPhoneMapper;
	@Autowired
	LoginEmailMapper loginEmailMapper;

	// 根据用户id查询用户基本数据
	@Test
	public void test() {
		User user = init();
		System.out.println("初始化后的user对象:"+user);

		System.out.println("\n通过userId查询的User对象");
		System.out.println(userMapper.findUserByUserId(user.getUserId()));

		// 登录密码
		System.out.println("\n通过userId查询的Password对象");
		System.out.println(passwordMapper.findPasswordByUserId(user.getUserId()));

		// 登录名Mapper测试
		LoginName loginName = loginNameMapper.findLoginNameByUserId(user.getUserId());
		System.out.println("\n打印通过userId查询的LoginName对象:");
		System.out.println(loginName);
		if(loginName!=null){
			System.out.println("打印通过loginName查询的LoginName对象:");
			System.out.println(loginNameMapper.findLoginNameByLoginName(loginName.getLoginName()));
		}

		// 登录手机号Mapper测试
		LoginPhone loginPhone = loginPhoneMapper.findLoginPhoneByUserId(user.getUserId());
		System.out.println("\n打印通过userId查询的LoginPhone对象:");
		System.out.println(loginPhone);
		if(loginPhone!=null){
			System.out.println("打印通过loginPhone查询的LoginPhone对象:");
			System.out.println(loginPhoneMapper.findLoginPhoneByLoginPhone(loginPhone.getLoginPhone()));
		}

		// 登录邮箱Mapper测试
		LoginEmail loginEmail = loginEmailMapper.findLoginEmailByUserId(user.getUserId());
		System.out.println("\n打印通过userId查询的LoginEmail对象:");
		System.out.println(loginEmail);
		if(loginEmail!=null){
			System.out.println("打印通过loginEmail查询的LoginEmail对象:");
			System.out.println(loginEmailMapper.findLoginEmailByLoginEmail(loginEmail.getLoginEmail()));
		}

	}

	public User init() {
		// 插入usrt
		User user = new User();

		HashMap<String, String> userId = new HashMap<>();
		// 通过存储过程获取一个Id并将其在原表中删除
		userIdWarehouseMapper.getUserIdAndDel(userId);
		user.setUserId(userId.get("userId"));
		user.setNickname("TestNickname");
		userMapper.saveUser(user);

		// 插入登录名
		LoginName loginName = new LoginName();
		loginName.setUserId(user.getUserId());
		loginName.setLoginName("laeni-----------");

		// 插入登录手机号
		LoginPhone loginPhone = new LoginPhone();
		loginPhone.setUserId(user.getUserId());
		loginPhone.setLoginPhone("1008611");

		// 插入登录邮箱
		LoginEmail loginEmail = new LoginEmail();
		loginEmail.setUserId(user.getUserId());
		loginEmail.setLoginEmail("m@laeni.cn");

		// 插入登录密码
		Password password = new Password();
		password.setUserId(user.getUserId());
		password.setPassword("*********");

		return user;
	}

}
