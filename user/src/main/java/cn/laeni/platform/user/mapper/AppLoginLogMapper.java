package cn.laeni.platform.user.mapper;

import cn.laeni.platform.user.entity.AppLoginLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;

public interface AppLoginLogMapper {
    @Insert({
        "insert into app_login_log (app_id, user_id, ",
        "time)",
        "values (#{appId,jdbcType=CHAR}, #{userId,jdbcType=CHAR}, ",
        "#{time})"
    })
    int insert(AppLoginLog record);

    @InsertProvider(type=AppLoginLogSqlProvider.class, method="insertSelective")
    int insertSelective(AppLoginLog record);
}