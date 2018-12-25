package cn.laeni.platform.user.mapper;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.laeni.platform.user.entity.LoginName;

public interface LoginNameMapper {

	/**
	 * 根据登录名查询记录
	 *
	 * @param loginName
	 * @return
	 */
	@Results({
			// 结果映射(持久化类命名和数据库一致的可省略,数据库命令经过驼峰转换后和持久化类一致的也不需要声明,但前提是要开启MyBatis的自动驼峰命名转换)
			@Result(property = "user", column = "user_id", one = @One(select = "cn.laeni.platform.user.mapper.UserMapper.selectByPrimaryKey")),
			@Result(property = "userId", column = "user_id"),
			@Result(property = "loginName", column = "login_name"),
	})
	@Select("SELECT login_name,user_id FROM `login_name` WHERE `login_name`=#{loginName}")
	LoginName findLoginNameByLoginName(String loginName);

	/**
	 * 根据用户Id查询记录
	 *
	 * @param userId
	 * @return
	 */
	@Results({
			// 结果映射(持久化类命名和数据库一致的可省略,数据库命令经过驼峰转换后和持久化类一致的也不需要声明,但前提是要开启MyBatis的自动驼峰命名转换)
			@Result(column = "user_id", property = "user", one = @One(select = "cn.laeni.platform.user.mapper.UserMapper.selectByPrimaryKey")),
			@Result(property = "userId", column = "user_id"),
	})
	@Select("SELECT * FROM `login_name` WHERE `user_id`=#{userId}")
	LoginName findLoginNameByUserId(String userId);

	/**
	 * 将登录名对应的用户ID更改为当前传入的用户id <br/>
	 * 一般用于登录帐号已经存在但依然要使用该帐号注册时使用<br/>
	 * 一旦更改成功,该帐号将不能更改再登录原来的帐号
	 *
	 * @param loginName 登录名
	 * @return 数据库影响条数
	 */
	@Update("UPDATE `login_name` SET `user_id`=#{userId} WHERE `login_name`=#{loginName}")
	int updateLoginNameInUserId(LoginName loginName);

	/**
	 * 使用存储过程插入或更新一条数据\n
	 * 如果loginName存在,则不进行任何操作\n
	 * 如果loginName不存在,但userId存在,则进行更新操作\n
	 * 如果loginName和都不存在,则进行插入操作
	 *
	 * @param loginName
	 * @return
	 */
	@Update("CALL `in_or_up_login_name`(#{loginName},#{userId})")
	int inOrUpLoginName(LoginName loginName);
}
