package cn.laeni.platform.user.mapper;

import cn.laeni.platform.user.entity.Password;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface PasswordMapper {
	/**
	 * 增加一条记录
	 * 
	 * @param Password
	 * @return
	 */
	@Insert("INSERT INTO `password` (`user_id`,`password`,`history_password`,`last_revise_time`) VALUES (#{userId},#{password},#{historyPassword},#{lastReviseTime});")
	int savePassword(Password Password);

	/**
	 * 根据id查询一条记录
	 * 
	 * @param userId
	 * @return
	 */
	@Select("SELECT * FROM `password` WHERE `user_id`=#{userId}")
	Password findPasswordByUserId(String userId);

	// 更新一条记录
	@Update("REPLACE INTO `password` (`user_id`, `password`, `history_password`, `last_revise_time`) VALUES (#{userId},#{password},#{historyPassword}, #{lastReviseTime});")
	int updatePassword(Password password);
}
