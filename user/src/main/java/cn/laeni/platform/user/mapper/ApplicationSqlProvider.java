package cn.laeni.platform.user.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;

import cn.laeni.platform.user.entity.Application;

public class ApplicationSqlProvider {

    public String insertSelective(Application record) {
        BEGIN();
        INSERT_INTO("application");
        
        if (record.getAppId() != null) {
            VALUES("app_id", "#{appId,jdbcType=CHAR}");
        }
        
        if (record.getAppName() != null) {
            VALUES("app_name", "#{appName,jdbcType=CHAR}");
        }
        
        if (record.getUserId() != null) {
            VALUES("user_id", "#{userId,jdbcType=CHAR}");
        }
        
        if (record.getRedirectUri() != null) {
            VALUES("redirect_uri", "#{redirectUri,jdbcType=CHAR}");
        }
        
        if (record.getAddTime() != null) {
            VALUES("add_time", "#{addTime}");
        }
        
        if (record.getExpiredTime() != null) {
            VALUES("expired_time", "#{expiredTime}");
        }
        
        return SQL();
    }
}