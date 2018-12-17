package cn.laeni.platform.user.mapper.dynaSqlProvider;

import org.apache.ibatis.jdbc.SQL;

import cn.laeni.platform.user.entity.ConnectQQ;

public class ConnectQQDynamic {
	public static void main(String[] args) {
		System.out.println(
				new SQL(){
					{
						UPDATE("表名");
						SET("a=1");
						SET("as=2", "ds=3");
					}
				}.toString()
		);
	}
	/**
	 * 动态更新数据
	 * 防止未提交更新的数据全部被清空
	 * @param connectQQ
	 * @return
	 */
	public String updataConnectQQ(ConnectQQ connectQQ) {
		String sql = new SQL() {
			{
				UPDATE("`connect_qq`");
				if(connectQQ.getAccessToken() != null)
				SET("`accessToken` = #{accessToken}");
				if(connectQQ.getRefreshToken() != null)
				SET("`refreshToken` = #{refreshToken}");
				if(connectQQ.getExpires() != null)
				SET("`expires` = #{expires}");
				
				WHERE("`openid` = #{openid} OR `userId` = #{userId}");
			}
		}.toString();
		System.out.println(sql);
		return sql;
	}
}


interface a{
	static String a="55";
}