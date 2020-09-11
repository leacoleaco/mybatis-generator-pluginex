package com.fuyo.cloud.db.biz.test.g.dao;

import com.fuyo.cloud.db.biz.test.g.domain.GTestPartDto;
import com.fuyo.cloud.db.biz.test.g.domain.GTestPartDtoExample;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface GTestPartDtoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    long countByExample(GTestPartDtoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    int deleteByExample(GTestPartDtoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    int insert(GTestPartDto record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    int insertSelective(GTestPartDto record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    GTestPartDto selectOneByExample(GTestPartDtoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    GTestPartDto selectOneByExampleSelective(@Param("example") GTestPartDtoExample example, @Param("selective") GTestPartDto.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    Map selectOneToMap(@Param("example") GTestPartDtoExample example, @Param("expressions") String ... expressions);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    List<GTestPartDto> selectByExampleSelective(@Param("example") GTestPartDtoExample example, @Param("selective") GTestPartDto.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    List<Map> selectToMap(@Param("example") GTestPartDtoExample example, @Param("expressions") String ... expressions);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    List<GTestPartDto> selectByExample(GTestPartDtoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    GTestPartDto selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") GTestPartDto.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    GTestPartDto selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") GTestPartDto record, @Param("example") GTestPartDtoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") GTestPartDto record, @Param("example") GTestPartDtoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(GTestPartDto record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table test_part
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(GTestPartDto record);
}