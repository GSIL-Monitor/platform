package cn.laeni.ossfile.mapper;

import cn.laeni.ossfile.entity.FileLink;
import cn.laeni.ossfile.entity.FileLinkKey;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface FileLinkMapper {
    @Delete({
        "delete from file_link",
        "where path_id = #{pathId,jdbcType=INTEGER}",
          "and file_key = #{fileKey,jdbcType=CHAR}"
    })
    int deleteByPrimaryKey(FileLinkKey key);

    @Insert({
        "insert into file_link (path_id, file_key, ",
        "used)",
        "values (#{pathId,jdbcType=INTEGER}, #{fileKey,jdbcType=CHAR}, ",
        "#{used,jdbcType=VARCHAR})"
    })
    int insert(FileLink record);

    int insertSelective(FileLink record);

    @Select({
        "select",
        "path_id, file_key, used",
        "from file_link",
        "where path_id = #{pathId,jdbcType=INTEGER}",
          "and file_key = #{fileKey,jdbcType=CHAR}"
    })
    @ResultMap("BaseResultMap")
    FileLink selectByPrimaryKey(FileLinkKey key);

    int updateByPrimaryKeySelective(FileLink record);

    @Update({
        "update file_link",
        "set used = #{used,jdbcType=VARCHAR}",
        "where path_id = #{pathId,jdbcType=INTEGER}",
          "and file_key = #{fileKey,jdbcType=CHAR}"
    })
    int updateByPrimaryKey(FileLink record);
}