package com.example.mavendemo.dao;

import com.example.mavendemo.dataobject.PromoDO;

public interface PromoDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Thu Feb 14 15:53:14 GMT+08:00 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Thu Feb 14 15:53:14 GMT+08:00 2019
     */
    int insert(PromoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Thu Feb 14 15:53:14 GMT+08:00 2019
     */
    int insertSelective(PromoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Thu Feb 14 15:53:14 GMT+08:00 2019
     */
    PromoDO selectByPrimaryKey(Integer id);
    PromoDO selectByItemId(Integer itemId);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Thu Feb 14 15:53:14 GMT+08:00 2019
     */
    int updateByPrimaryKeySelective(PromoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo
     *
     * @mbg.generated Thu Feb 14 15:53:14 GMT+08:00 2019
     */
    int updateByPrimaryKey(PromoDO record);
}