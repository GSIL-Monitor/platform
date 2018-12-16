package cn.laeni.platform.user.mapper;

import cn.laeni.platform.user.entity.UserIdWarehouse;
import cn.laeni.platform.user.mapper.dynaSqlProvider.UserIdWarehouseDynamic;
import cn.laeni.platform.user.other.entity.Count;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * UserIdWarehouse表的各种原子操作
 * @author laeni.cn
 *
 */
public interface UserIdWarehouseMapper {

	/**
	 * 使用动态SQL语句批量插入
	 * @return
	 */
	@InsertProvider(type= UserIdWarehouseDynamic.class, method = "insertUserIdWarehouses")
	int insertUserIdWarehouse(@Param("list") List<UserIdWarehouse> list);

	/**
	 * 统计总数
	 * @return
	 */
	@Select("SELECT COUNT(`user_id`) AS 'count' FROM `user_id_warehouse`")
    Count countUserIdWarehouse();
	
	/**
	 * 根据主键id查询一条记录
	 * @param userId
	 * @return
	 */
	@Select("SELECT * FROM `user_id_warehouse` WHERE `user_id`=#{userId}")
	UserIdWarehouse findById(@Param("userId") String userId);
	
	/**
	 * 随机获取一条并将其删除(使用存储函数)
	 * @return
	 */
	@Select("CALL get_userid_and_del_this(#{userId,mode=OUT,jdbcType=VARCHAR})")
	@Options(statementType= StatementType.CALLABLE)
	void getUserIdAndDel(Map<String, String> userId);
}
