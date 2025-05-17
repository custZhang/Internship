package com.internship.entitlement.service;


import com.internship.entitlement.domain.ImsBenefits;

import java.util.List;

/**
 * 工资待遇信息Service接口
 * 
 * @author internship
 * @date 2020-12-05
 */
public interface IImsBenefitsService 
{
    /**
     * 查询工资待遇信息
     * 
     * @param benefitsId 工资待遇信息ID
     * @return 工资待遇信息
     */
    public ImsBenefits selectImsBenefitsById(Long benefitsId);

    /**
     * 查询工资待遇信息列表
     * 
     * @param imsBenefits 工资待遇信息
     * @return 工资待遇信息集合
     */
    public List<ImsBenefits> selectImsBenefitsList(ImsBenefits imsBenefits);

    /**
     * 新增工资待遇信息
     * 
     * @param imsBenefits 工资待遇信息
     * @return 结果
     */
    public int insertImsBenefits(ImsBenefits imsBenefits);

    /**
     * 修改工资待遇信息
     * 
     * @param imsBenefits 工资待遇信息
     * @return 结果
     */
    public int updateImsBenefits(ImsBenefits imsBenefits);

    /**
     * 批量删除工资待遇信息
     * 
     * @param benefitsIds 需要删除的工资待遇信息ID
     * @return 结果
     */
    public int deleteImsBenefitsByIds(Long[] benefitsIds);

    /**
     * 删除工资待遇信息信息
     * 
     * @param benefitsId 工资待遇信息ID
     * @return 结果
     */
    public int deleteImsBenefitsById(Long benefitsId);
}
