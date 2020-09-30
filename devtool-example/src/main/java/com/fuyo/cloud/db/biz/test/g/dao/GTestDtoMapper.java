package com.fuyo.cloud.db.biz.test.g.dao;

import com.fuyo.cloud.db.biz.test.g.domain.GTestDto;
import com.fuyo.cloud.db.biz.test.g.domain.GTestDtoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GTestDtoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_test1
     *
     * @mbg.generated
     */
    long countByExample(GTestDtoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_test1
     *
     * @mbg.generated
     */
    int deleteByExample(GTestDtoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_test1
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_test1
     *
     * @mbg.generated
     */
    int insert(GTestDto record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_test1
     *
     * @mbg.generated
     */
    int insertSelective(GTestDto record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_test1
     *
     * @mbg.generated
     */
    GTestDto selectOneByExample(GTestDtoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_test1
     *
     * @mbg.generated
     */
    GTestDto selectOneByExampleSelective(@Param("example") GTestDtoExample example, @Param("selective") GTestDto.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_test1
     *
     * @mbg.generated
     */
    List<GTestDto> selectByExampleSelective(@Param("example") GTestDtoExample example, @Param("selective") GTestDto.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_test1
     *
     * @mbg.generated
     */
    List<GTestDto> selectByExample(GTestDtoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_test1
     *
     * @mbg.generated
     */
    GTestDto selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") GTestDto.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_test1
     *
     * @mbg.generated
     */
    GTestDto selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_test1
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") GTestDto record, @Param("example") GTestDtoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_test1
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") GTestDto record, @Param("example") GTestDtoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_test1
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(GTestDto record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_test1
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(GTestDto record);
}