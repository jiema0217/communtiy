package io.github.jiema.communtiy.mapper;

import io.github.jiema.communtiy.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user(name,account_id,token,gmt_create,gmt_modified,avatar_url)" +
            " VALUES (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("SELECT * FROM  user WHERE token=#{token}")
    User findByToken(@Param("token") String token);

    @Select("SELECT * FROM  user WHERE account_id=#{accountId}")
    User findById(@Param("accountId") Integer accountId);
}
