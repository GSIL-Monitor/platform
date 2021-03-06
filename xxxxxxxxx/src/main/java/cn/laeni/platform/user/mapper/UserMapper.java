package cn.laeni.platform.user.mapper;

import cn.laeni.platform.user.entity.User;
import cn.laeni.platform.user.mapper.dynaSqlProvider.UserDynamic;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    /**
     * 根据用户id查询用户基本数据
     *
     * @param userId
     * @return
     */
    @Select("SELECT * FROM `user` WHERE `user_id`=#{userId}")
    User findUserByUserId(String userId);

    /**
     * 插入一个用户数据
     *
     * @param user
     * @return
     */
    @Insert("INSERT INTO `user` (`user_id`, `nickname`, `avatar`, `grade`) VALUES (#{userId}, #{nickname}, #{avatar}, #{grade})")
    int saveUser(User user);

    /**
     * 动态更新user数据
     *
     * @param user
     * @return
     */
    @InsertProvider(type = UserDynamic.class, method = "updataUser")
    int upUser(User user);
}
