package org.nju.demo.dao;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.nju.demo.entity.ACode;
import org.nju.demo.entity.ACodeExample;

public interface ACodeMapper {
    @SelectProvider(type=ACodeSqlProvider.class, method="countByExample")
    long countByExample(ACodeExample example);

    @DeleteProvider(type=ACodeSqlProvider.class, method="deleteByExample")
    int deleteByExample(ACodeExample example);

    @Delete({
            "delete from a_code",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into a_code (code_name, description)",
            "values (#{codeName,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(ACode record);

    @InsertProvider(type=ACodeSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insertSelective(ACode record);

    @SelectProvider(type=ACodeSqlProvider.class, method="selectByExample")
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="code_name", property="codeName", jdbcType=JdbcType.VARCHAR),
            @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR)
    })
    List<ACode> selectByExample(ACodeExample example);

    @Select({
            "select",
            "id, code_name, description",
            "from a_code",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="code_name", property="codeName", jdbcType=JdbcType.VARCHAR),
            @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR)
    })
    ACode selectByPrimaryKey(Integer id);

    @UpdateProvider(type=ACodeSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") ACode record, @Param("example") ACodeExample example);

    @UpdateProvider(type=ACodeSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") ACode record, @Param("example") ACodeExample example);

    @UpdateProvider(type=ACodeSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ACode record);

    @Update({
            "update a_code",
            "set code_name = #{codeName,jdbcType=VARCHAR},",
            "description = #{description,jdbcType=VARCHAR}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(ACode record);
}