package com.dbzhw.dao;

import com.dbzhw.entity.UserEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MemberDao {

    @Select("select id,username,password,phone,email,created,updated,openid from mb_user where id =#{userId}")
    UserEntity findByID(@Param("userId") Long userId);

    @Insert("INSERT INTO `mb_user` (username,password,phone,email,created,updated) VALUES (#{username}, #{password},#{phone},#{email},#{created},#{updated});")
    Integer insertUser(UserEntity userEntity);

    @Select("select id,username,password,phone,email,created,updated,openid from mb_user where username =#{username} and password =#{password}")
    UserEntity login(@Param("username") String username, @Param("password") String password);

    @Select("select id,username,password,phone,email,created,updated,openid from mb_user where openid =#{openid}")
    UserEntity findOpenidUser(@Param("openid") String openid);

    @Update("update mb_user set openid =#{openid} where id =#{userId}")
    Integer updateUserOpenid(@Param("userId") Integer userId, @Param("openid") String openid);
}
