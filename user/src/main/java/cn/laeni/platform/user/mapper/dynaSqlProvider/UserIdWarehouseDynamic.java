package cn.laeni.platform.user.mapper.dynaSqlProvider;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import cn.laeni.platform.user.entity.UserIdWarehouse;

public class UserIdWarehouseDynamic {

	/**
	 * 批量插入 UserIdWarehouse
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String insertUserIdWarehouses(Map<String, Object> map) {
		List<UserIdWarehouse> list = (List<UserIdWarehouse>) map.get("list");

		if (list.size() <= 0) {
			return null;
		}

		// 这里不能使用SQL对象,而是需要自定义实现方法
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO `user_id_warehouse` ");
		sb.append("(`user_id`,`add_time`) ");
		sb.append("VALUES ");

		// 构造SQL模板
		MessageFormat mf = new MessageFormat("(#'{'list[{0}].userId,jdbcType=VARCHAR}, #'{'list[{0}].addTime,jdbcType=BIGINT})");

		for (int i = 0; i < list.size(); i++) {
			sb.append(
					mf.format(new Object[] { i })	// 表示将一个数组替换为模板中的占位符,第一个替换{0},第二个替换{1},以此类推
					);
			if (i < list.size() - 1) {
				sb.append(",");
			}
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

}
