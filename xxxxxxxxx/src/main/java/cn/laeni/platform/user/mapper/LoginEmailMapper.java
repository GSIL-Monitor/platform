package cn.laeni.platform.user.mapper;

import cn.laeni.platform.user.entity.LoginEmail;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface LoginEmailMapper {

	/**
	 * 通过邮箱地址查询记录
	 *
	 * @param loginEmail
	 * @return
	 */
	@Results({
			// 结果映射(持久化类命名和数据库一致的可省略,数据库命令经过驼峰转换后和持久化类一致的也不需要声明,但前提是要开启MyBatis的自动驼峰命名转换)
			@Result(property = "user", column = "user_id",
					one = @One(select = "UserMapper.findUserByUserId")),
			@Result(property = "userId", column = "user_id"),
			@Result(property = "loginEmail", column = "login_email"),
	})
	@Select("SELECT * FROM `login_email` WHERE `login_email`=#{loginEmail}")
    LoginEmail findLoginEmailByLoginEmail(String loginEmail);

	/**
	 * 通过用户ID查询用户记录
	 *
	 * @param userId
	 * @return
	 */
	@Results({
			@Result(property = "userId", column = "user_id"),
			@Result(property = "loginEmail", column = "login_email"),
			@Result(property = "user", column = "user_id",
					one = @One(select = "UserMapper.findUserByUserId")),
	})
	@Select("SELECT * FROM `login_email` WHERE `user_id`=#{userId}")
	LoginEmail findLoginEmailByUserId(String userId);

	/**
	 * 使用存储过程插入或更新一条数据\n
	 * 如果loginName存在,则不进行任何操作\n
	 * 如果loginName不存在,但userId存在,则进行更新操作\n
	 * 如果loginName和都不存在,则进行插入操作
	 *
	 * @param loginEmail 对象Bean
	 * @return
	 */
	@Update("CALL `in_or_up_login_email`(#{loginEmail},#{userId})")
	int inOrUpLoginEmail(LoginEmail loginEmail);
}
