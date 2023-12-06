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
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.nju.demo.entity.AVersion;
import org.nju.demo.entity.AVersionExample;

public interface AVersionMapper {
    @SelectProvider(type=AVersionSqlProvider.class, method="countByExample")
    long countByExample(AVersionExample example);

    @DeleteProvider(type=AVersionSqlProvider.class, method="deleteByExample")
    int deleteByExample(AVersionExample example);

    @Delete({
            "delete from a_version",
            "where version_id = #{versionId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String versionId);

    @Insert({
            "insert into a_version (version_id, version_name, ",
            "file_path, create_time, ",
            "project_id, last_id, ",
            "code_id, folder_name, ",
            "code_state)",
            "values (#{versionId,jdbcType=VARCHAR}, #{versionName,jdbcType=VARCHAR}, ",
            "#{filePath,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
            "#{projectId,jdbcType=VARCHAR}, #{lastId,jdbcType=VARCHAR}, ",
            "#{codeId,jdbcType=INTEGER}, #{folderName,jdbcType=VARCHAR}, ",
            "#{codeState,jdbcType=VARCHAR})"
    })
    int insert(AVersion record);

    @InsertProvider(type=AVersionSqlProvider.class, method="insertSelective")
    int insertSelective(AVersion record);

    @SelectProvider(type=AVersionSqlProvider.class, method="selectByExample")
    @Results({
            @Result(column="version_id", property="versionId", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="version_name", property="versionName", jdbcType=JdbcType.VARCHAR),
            @Result(column="file_path", property="filePath", jdbcType=JdbcType.VARCHAR),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="project_id", property="projectId", jdbcType=JdbcType.VARCHAR),
            @Result(column="last_id", property="lastId", jdbcType=JdbcType.VARCHAR),
            @Result(column="code_id", property="codeId", jdbcType=JdbcType.INTEGER),
            @Result(column="folder_name", property="folderName", jdbcType=JdbcType.VARCHAR),
            @Result(column="code_state", property="codeState", jdbcType=JdbcType.VARCHAR)
    })
    List<AVersion> selectByExample(AVersionExample example);

    @Select({
            "select",
            "version_id, version_name, file_path, create_time, project_id, last_id, code_id, ",
            "folder_name, code_state",
            "from a_version",
            "where version_id = #{versionId,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="version_id", property="versionId", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="version_name", property="versionName", jdbcType=JdbcType.VARCHAR),
            @Result(column="file_path", property="filePath", jdbcType=JdbcType.VARCHAR),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="project_id", property="projectId", jdbcType=JdbcType.VARCHAR),
            @Result(column="last_id", property="lastId", jdbcType=JdbcType.VARCHAR),
            @Result(column="code_id", property="codeId", jdbcType=JdbcType.INTEGER),
            @Result(column="folder_name", property="folderName", jdbcType=JdbcType.VARCHAR),
            @Result(column="code_state", property="codeState", jdbcType=JdbcType.VARCHAR)
    })
    AVersion selectByPrimaryKey(String versionId);

    @UpdateProvider(type=AVersionSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") AVersion record, @Param("example") AVersionExample example);

    @UpdateProvider(type=AVersionSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") AVersion record, @Param("example") AVersionExample example);

    @UpdateProvider(type=AVersionSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(AVersion record);

    @Update({
            "update a_version",
            "set version_name = #{versionName,jdbcType=VARCHAR},",
            "file_path = #{filePath,jdbcType=VARCHAR},",
            "create_time = #{createTime,jdbcType=TIMESTAMP},",
            "project_id = #{projectId,jdbcType=VARCHAR},",
            "last_id = #{lastId,jdbcType=VARCHAR},",
            "code_id = #{codeId,jdbcType=INTEGER},",
            "folder_name = #{folderName,jdbcType=VARCHAR},",
            "code_state = #{codeState,jdbcType=VARCHAR}",
            "where version_id = #{versionId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(AVersion record);
}