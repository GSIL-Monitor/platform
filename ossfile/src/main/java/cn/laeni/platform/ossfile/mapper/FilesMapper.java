package cn.laeni.platform.ossfile.mapper;

import cn.laeni.platform.ossfile.entity.Files;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface FilesMapper {
    @Delete({
        "delete from files",
        "where file_key = #{fileKey,jdbcType=CHAR}"
    })
    int deleteByPrimaryKey(String fileKey);

    @Insert({
        "insert into files (file_key, user_id, ",
        "file_size, md5, file_type, ",
        "time)",
        "values (#{fileKey,jdbcType=CHAR}, #{userId,jdbcType=CHAR}, ",
        "#{fileSize,jdbcType=BIGINT}, #{md5,jdbcType=CHAR}, #{fileType,jdbcType=CHAR}, ",
        "#{time,jdbcType=INTEGER})"
    })
    int insert(Files record);

    int insertSelective(Files record);

    /**
     * 动态更新记录
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Files record);

    @Update({
        "update files",
        "set user_id = #{userId,jdbcType=CHAR},",
          "file_size = #{fileSize,jdbcType=BIGINT},",
          "md5 = #{md5,jdbcType=CHAR},",
          "file_type = #{fileType,jdbcType=CHAR},",
          "time = #{time,jdbcType=INTEGER}",
        "where file_key = #{fileKey,jdbcType=CHAR}"
    })
    int updateByPrimaryKey(Files record);

    @Select({
            "select",
            "file_key, user_id, file_size, md5, file_type, time",
            "from files",
            "where file_key = #{fileKey,jdbcType=CHAR}"
    })
    @ResultMap("BaseResultMap")
    Files selectByPrimaryKey(String fileKey);

}