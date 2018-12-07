package cn.laeni.ossfile.mapper;

import cn.laeni.ossfile.entity.UserSpace;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserSpaceMapper {
    @Delete({
        "delete from user_space",
        "where user_id = #{userId,jdbcType=CHAR}"
    })
    int deleteByPrimaryKey(String userId);

    @Insert({
        "insert into user_space (user_id, sum_space, ",
        "used_space)",
        "values (#{userId,jdbcType=CHAR}, #{sumSpace,jdbcType=BIGINT}, ",
        "#{usedSpace,jdbcType=BIGINT})"
    })
    int insert(UserSpace record);

    int insertSelective(UserSpace record);

    @Select({
        "select",
        "user_id, sum_space, used_space",
        "from user_space",
        "where user_id = #{userId,jdbcType=CHAR}"
    })
    @ResultMap("BaseResultMap")
    UserSpace selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(UserSpace record);

    @Update({
        "update user_space",
        "set sum_space = #{sumSpace,jdbcType=BIGINT},",
          "used_space = #{usedSpace,jdbcType=BIGINT}",
        "where user_id = #{userId,jdbcType=CHAR}"
    })
    int updateByPrimaryKey(UserSpace record);
}