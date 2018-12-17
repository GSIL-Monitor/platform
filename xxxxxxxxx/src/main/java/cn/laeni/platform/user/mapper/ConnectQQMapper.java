package cn.laeni.platform.goods.mapper;

import cn.laeni.platform.goods.entity.ConnectQQ;
import cn.laeni.platform.goods.mapper.dynaSqlProvider.ConnectQQDynamic;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

public interface ConnectQQMapper {
	
	/**
	 * 查询一条记录
	 * @param id 用户id或者openid
	 * @return
	 */
	@Select("SELECT * FROM `connect_qq` WHERE `openid` = #{id} OR `user_id` = #{id}")
    ConnectQQ findConnectQQByOpenIdOrUserId(String id);
	
	/**
	 * 插入记录
	 * @param connectQQ
	 * @return
	 */
	@Insert("INSERT INTO `connect_qq`(`openid`, `user_id`, `access_token`, `refresh_token`, `expires`)"
			+ "VALUES (#{openid}, #{userId}, #{accessToken}, #{refreshToken}, #{expires})")
	int saveConnectQQ(ConnectQQ connectQQ);
	
	// 修改记录
	@UpdateProvider(type= ConnectQQDynamic.class,method="updataConnectQQ")
	int updateConnectQQ(ConnectQQ connectQQ);
	
	/**
	 * 删除记录
	 * @param id id 用户id或者openid
	 * @return
	 */
	@Delete("DELETE FROM `connect_qq` WHERE `openid` = #{id} OR `user_id` = #{id}")
	int seleteConnectQQ(String id);
}
