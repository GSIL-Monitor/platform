package cn.laeni.platform.user.mapper;

import cn.laeni.platform.user.entity.Address;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;

public interface AddressMapper {
    @Insert({
        "insert into address (address_id, user_id, ",
        "user_name, tel_phone, location, ",
        "postcode)",
        "values (#{addressId,jdbcType=CHAR}, #{userId,jdbcType=CHAR}, ",
        "#{userName,jdbcType=CHAR}, #{telPhone,jdbcType=CHAR}, #{location,jdbcType=CHAR}, ",
        "#{postcode,jdbcType=CHAR})"
    })
    int insert(Address record);

    @InsertProvider(type=AddressSqlProvider.class, method="insertSelective")
    int insertSelective(Address record);
}