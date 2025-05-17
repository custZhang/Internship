package com.internship.task.mapper;

import com.internship.task.domain.ImsTaskConfig;

import java.util.List;

/**
 * 流程配置信息Mapper接口
 * 
 * @author internship
 * @date 2020-12-25
 */
public interface ImsTaskConfigMapper 
{
    /**
     * 查询流程配置信息
     * 
     * @param processId 流程配置信息ID
     * @return 流程配置信息
     */
    public ImsTaskConfig selectImsTaskConfigById(String processId);

    /**
     * 查询流程配置信息列表
     * 
     * @param imsTaskConfig 流程配置信息
     * @return 流程配置信息集合
     */
    public List<ImsTaskConfig> selectImsTaskConfigList(ImsTaskConfig imsTaskConfig);

    /**
     * 查询流程配置信息
     *
     * @param imsTaskConfig 流程配置信息
     * @return 流程配置信息集合
     */
    public ImsTaskConfig selectImsTaskConfig(ImsTaskConfig imsTaskConfig);

    /**
     * 新增流程配置信息
     * 
     * @param imsTaskConfig 流程配置信息
     * @return 结果
     */
    public int insertImsTaskConfig(ImsTaskConfig imsTaskConfig);

    /**
     * 修改流程配置信息
     * 
     * @param imsTaskConfig 流程配置信息
     * @return 结果
     */
    public int updateImsTaskConfig(ImsTaskConfig imsTaskConfig);

    /**
     * 删除流程配置信息
     * 
     * @param processId 流程配置信息ID
     * @return 结果
     */
    public int deleteImsTaskConfigById(String processId);

    /**
     * 批量删除流程配置信息
     * 
     * @param processIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteImsTaskConfigByIds(String[] processIds);
}
