package cn.laeni.platform.user.mapper.dynaSqlProvider;

import org.apache.ibatis.jdbc.SQL;

import cn.laeni.platform.user.entity.User;

public class UserDynamic {
	public String updataUser(User user) {
		
		// UPDATE `user` SET `nickname`=#{nickname}, `avatar`=#{avatar}, `grade`=#{grade} WHERE `user_id`=#{userId}
		
		 String sql = new SQL() {
			{
				UPDATE("`user`");
				if(user.getGrade() != null) {
					SET("`grade`=#{grade}");
				}
				if(user.getNickname() != null) {
					SET("`nickname`=#{nickname}");
				}
				if(user.getAvatar() != null) {
					SET("`avatar`=#{avatar}");
				}
				WHERE("`user_id`=#{userId}");
			}
		}.toString();
		return sql;
	}
}
