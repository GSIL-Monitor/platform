package cn.laeni.platform.ossfile.mapper.dyna;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SQL;

import java.awt.*;
import java.util.Map;

/**
 * PathStructureMapper对应的动态SQL类
 *
 * @author laeni.cn
 */
public class PathStructureDynamic {

    /**
     * 查询该用户指定路径下的所有文件夹(根路径的父路径ID为null)
     * @param map Mapper对应的参数值
     * @return
     */
    public String selectByUserId(Map map) {
        StringBuilder sql = new StringBuilder("SELECT path_id, user_id, path_name, parent_path_id, time, directory ")
                .append("FROM path_structure where user_id = #{userId,jdbcType=CHAR} ")
                .append("AND directory = 1 AND parent_path_id ");
        if (map.get("parentPathId") == null) {
            sql.append("IS NULL ");
        } else {
            sql.append("= #{parentPathId,jdbcType=INTEGER}");
        }

        return sql.toString();
    }
}
