package cn.laeni.platform.user.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;

import cn.laeni.platform.user.entity.Address;

public class AddressSqlProvider {

    public String insertSelective(Address record) {
        BEGIN();
        INSERT_INTO("address");
        
        if (record.getAddressId() != null) {
            VALUES("address_id", "#{addressId,jdbcType=CHAR}");
        }
        
        if (record.getUserId() != null) {
            VALUES("user_id", "#{userId,jdbcType=CHAR}");
        }
        
        if (record.getUserName() != null) {
            VALUES("user_name", "#{userName,jdbcType=CHAR}");
        }
        
        if (record.getTelPhone() != null) {
            VALUES("tel_phone", "#{telPhone,jdbcType=CHAR}");
        }
        
        if (record.getLocation() != null) {
            VALUES("location", "#{location,jdbcType=CHAR}");
        }
        
        if (record.getPostcode() != null) {
            VALUES("postcode", "#{postcode,jdbcType=CHAR}");
        }
        
        return SQL();
    }
}