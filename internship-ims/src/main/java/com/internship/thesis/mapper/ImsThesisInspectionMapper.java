package com.internship.thesis.mapper;

import java.util.List;
import com.internship.thesis.domain.ImsThesisInspection;

/**
 * 论文中期检查信息Mapper接口
 * 
 * @author internship
 * @date 2020-12-18
 */
public interface ImsThesisInspectionMapper 
{
    /**
     * 查询论文中期检查信息
     * 
     * @param inspectId 论文中期检查信息ID
     * @return 论文中期检查信息
     */
    public ImsThesisInspection selectImsThesisInspectionById(Long inspectId);

    /**
     * 查询论文中期检查信息列表
     * 
     * @param imsThesisInspection 论文中期检查信息
     * @return 论文中期检查信息集合
     */
    public List<ImsThesisInspection> selectImsThesisInspectionList(ImsThesisInspection imsThesisInspection);

    /**
     * 新增论文中期检查信息
     * 
     * @param imsThesisInspection 论文中期检查信息
     * @return 结果
     */
    public int insertImsThesisInspection(ImsThesisInspection imsThesisInspection);

    /**
     * 修改论文中期检查信息
     * 
     * @param imsThesisInspection 论文中期检查信息
     * @return 结果
     */
    public int updateImsThesisInspection(ImsThesisInspection imsThesisInspection);

    /**
     * 删除论文中期检查信息
     * 
     * @param inspectId 论文中期检查信息ID
     * @return 结果
     */
    public int deleteImsThesisInspectionById(Long inspectId);

    /**
     * 批量删除论文中期检查信息
     * 
     * @param inspectIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteImsThesisInspectionByIds(Long[] inspectIds);
}
