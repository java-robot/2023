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
import org.nju.demo.entity.AEmployee;
import org.nju.demo.entity.AEmployeeExample;

public interface AEmployeeMapper {
    @SelectProvider(type=AEmployeeSqlProvider.class, method="countByExample")
    long countByExample(AEmployeeExample example);

    @DeleteProvider(type=AEmployeeSqlProvider.class, method="deleteByExample")
    int deleteByExample(AEmployeeExample example);

    @Delete({
        "delete from a_employee",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into a_employee (employee_name)",
        "values (#{employeeName,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(AEmployee record);

    @InsertProvider(type=AEmployeeSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insertSelective(AEmployee record);

    @SelectProvider(type=AEmployeeSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="employee_name", property="employeeName", jdbcType=JdbcType.VARCHAR)
    })
    List<AEmployee> selectByExample(AEmployeeExample example);

    @Select({
        "select",
        "id, employee_name",
        "from a_employee",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="employee_name", property="employeeName", jdbcType=JdbcType.VARCHAR)
    })
    AEmployee selectByPrimaryKey(Integer id);

    @UpdateProvider(type=AEmployeeSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") AEmployee record, @Param("example") AEmployeeExample example);

    @UpdateProvider(type=AEmployeeSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") AEmployee record, @Param("example") AEmployeeExample example);

    @UpdateProvider(type=AEmployeeSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(AEmployee record);

    @Update({
        "update a_employee",
        "set employee_name = #{employeeName,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(AEmployee record);
}