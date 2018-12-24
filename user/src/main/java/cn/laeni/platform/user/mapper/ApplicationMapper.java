package cn.laeni.platform.user.mapper;

import cn.laeni.platform.user.entity.Application;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;

public interface ApplicationMapper {
    @Insert({
        "insert into application (app_id, app_name, ",
        "user_id, redirect_uri, add_time, ",
        "expired_time)",
        "values (#{appId,jdbcType=CHAR}, #{appName,jdbcType=CHAR}, ",
        "#{userId,jdbcType=CHAR}, #{redirectUri,jdbcType=CHAR}, #{addTime}, ",
        "#{expiredTime})"
    })
    int insert(Application record);

    @InsertProvider(type=ApplicationSqlProvider.class, method="insertSelective")
    int insertSelective(Application record);
}