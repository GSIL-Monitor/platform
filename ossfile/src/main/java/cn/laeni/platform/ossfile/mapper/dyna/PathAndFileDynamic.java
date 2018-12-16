package cn.laeni.platform.ossfile.mapper.dyna;

import cn.laeni.platform.ossfile.entity.Files;

import java.util.Map;

/**
 * 动态SQL语句拼接
 *
 * @author laeni.cn
 */
public class PathAndFileDynamic {
    public String selectByFilesSelective(Files files) {
        StringBuilder sql = new StringBuilder("SELECT ")
                .append("ps.path_id, ps.path_name, ps.parent_path_id, ps.time, ps.directory, fl.used, f.file_key, f.file_size, f.file_type ")
                .append("FROM path_structure AS ps INNER JOIN file_link AS fl INNER JOIN files AS f ")
                .append("ON ps.path_id = fl.path_id AND fl.`file_key` = f.`file_key` ");

        if (files.getFileKey() != null) {
            sql = sql.append("AND f.file_key = #{fileKey,jdbcType=CHAR} ");
        }
        if (files.getUserId() != null) {
            sql = sql.append("AND f.user_id = #{userId,jdbcType=CHAR} ");
        }
        if (files.getFileSize() != null) {
            sql = sql.append("AND f.file_size = #{fileSize,jdbcType=BIGINT} ");
        }
        if (files.getMd5() != null) {
            sql = sql.append("AND f.md5 = #{md5,jdbcType=CHAR} ");
        }
        if (files.getFileType() != null) {
            sql = sql.append("AND f.file_type = #{fileType,jdbcType=CHAR} ");
        }

        // 如果有多条,则只显示其中一条
        sql = sql.append("LIMIT 1");

        return sql.toString();
    }

    /**
     * 防止根路径差查不到文件
     * @param map
     * @return
     */
    public String selectByUserIdAndPathId(Map<String, String> map) {
        /*

    @Select({
            "SELECT ps.path_id, ps.path_name, ps.parent_path_id, ps.time,",
            "ps.directory, fl.used, f.file_key, f.file_size, f.file_type",
            "FROM path_structure AS ps INNER JOIN file_link AS fl INNER JOIN files AS f",
            "ON ps.path_id = fl.path_id AND fl.`file_key` = f.`file_key`",
            "AND ps.user_id = #{userId,jdbcType=CHAR}",

            "AND ps.`parent_path_id` = #{parentPathId,jdbcType=INTEGER}"
    })
         */
        StringBuilder sql = new StringBuilder("SELECT ps.path_id, ps.path_name, ps.parent_path_id, ps.time, ")
                .append("ps.directory, fl.used, f.file_key, f.file_size, f.file_type ")
                .append("FROM path_structure AS ps INNER JOIN file_link AS fl INNER JOIN files AS f ")
                .append("ON ps.path_id = fl.path_id AND fl.`file_key` = f.`file_key` ")
                .append("AND ps.user_id = #{userId,jdbcType=CHAR} ")

                .append("AND ps.parent_path_id ");
        if (map.get("parentPathId") == null) {
            sql.append("IS NULL ");
        } else {
            sql.append("= #{parentPathId,jdbcType=INTEGER}");
        }

        return sql.toString();
    }
}
