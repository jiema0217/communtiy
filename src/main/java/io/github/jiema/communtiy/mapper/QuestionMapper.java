package io.github.jiema.communtiy.mapper;

import io.github.jiema.communtiy.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("INSERT INTO question (title,description,gmt_create,gmt_modified,creator,tag) " +
            "values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("SELECT * FROM question LIMIT #{offset}, #{size}")
    List<Question> list(@Param("offset") Integer offset, @Param("size") Integer size);

    @Select("SELECT count(1) FROM question")
    Integer count();

    @Select("SELECT * FROM question WHERE creator=#{accountId} LIMIT #{offset}, #{size}")
    List<Question> listByUserId(@Param("accountId") Integer accountId, @Param("offset") Integer offset, @Param("size") Integer size);

    @Select("SELECT count(1) FROM question WHERE creator=#{accountId}")
    Integer countByUserId(@Param("accountId") Integer accountId);
}
