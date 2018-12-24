package cn.laeni.platform.user.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;

import cn.laeni.platform.user.entity.AppLoginLog;

public class AppLoginLogSqlProvider {

    public String insertSelective(AppLoginLog record) {
        BEGIN();
        INSERT_INTO("app_login_log");
        
        if (record.getAppId() != null) {
            VALUES("app_id", "#{appId,jdbcType=CHAR}");
        }
        
        if (record.getUserId() != null) {
            VALUES("user_id", "#{userId,jdbcType=CHAR}");
        }
        
        if (record.getTime() != null) {
            VALUES("time", "#{time}");
        }
        
        return SQL();
    }
}