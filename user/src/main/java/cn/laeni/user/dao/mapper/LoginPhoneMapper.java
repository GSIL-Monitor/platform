package cn.laeni.user.dao.mapper;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;

import cn.laeni.user.dao.entity.LoginPhone;

public interface LoginPhoneMapper {

	/**
	 * 根据手机号码查询记录
	 * @param loginPhone
	 * @return
	 */
	@Results({
		// 结果映射(持久化类命名和数据库一致的可省略,数据库命令经过驼峰转换后和持久化类一致的也不需要声明,但前提是要开启MyBatis的自动驼峰命名转换)
		@Result(column="user_id",		// 这里的"user_id"表示LoginPhone对象的"Password"属性对应"_login_name"表的字段"user_id",即为"_login_name"表的外键参照列
		property="user",				// 表示查询的结果对应LoginPhone对象的那个属性
		one=@One(	// 一对一关系
				select="cn.laeni.user.dao.mapper.UserMapper.findUserByUserId")
		),
		@Result(property="userId",column="user_id"),
	})
	@Select("SELECT * FROM `login_phone` WHERE `login_phone`=#{loginPhone}")
	LoginPhone findLoginPhoneByLoginPhone(String loginPhone);
	
	/**
	 * 根据用户id查询记录
	 * @param userId
	 * @return
	 */
	@Results({
			// 结果映射(持久化类命名和数据库一致的可省略,数据库命令经过驼峰转换后和持久化类一致的也不需要声明,但前提是要开启MyBatis的自动驼峰命名转换)
			@Result(column="user_id",		// 这里的"user_id"表示LoginPhone对象的"Password"属性对应"_login_name"表的字段"user_id",即为"_login_name"表的外键参照列
					property="user",				// 表示查询的结果对应LoginPhone对象的那个属性
					one=@One(	// 一对一关系
							select="cn.laeni.user.dao.mapper.UserMapper.findUserByUserId")
			),
			@Result(property="userId",column="user_id"),
	})
	@Select("SELECT * FROM `login_phone` WHERE `user_id`=#{userId}")
	LoginPhone findLoginPhoneByUserId(String userId);

	/**
	 * 使用存储过程插入或更新一条数据\n
	 * 如果loginName存在,则不进行任何操作\n
	 * 如果loginName不存在,但userId存在,则进行更新操作\n
	 * 如果loginName和都不存在,则进行插入操作
	 * @param loginPhone 登录手机号
	 * @return 数据库影响条数
	 */
	@Update("CALL `in_or_up_login_phone`(#{loginPhone},#{userId})")
	int inOrUpLoginPhone(LoginPhone loginPhone);
}
