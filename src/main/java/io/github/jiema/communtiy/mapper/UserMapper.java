package io.github.jiema.communtiy.mapper;

import io.github.jiema.communtiy.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user(name,account_id,token,gmt_create,gmt_modified) VALUES (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);
}
