package com.internship.thesis.mapper;

import com.internship.thesis.domain.ImsThesisTopic;

import java.util.List;

/**
 * 课题信息Mapper接口
 * 
 * @author internship
 * @date 2020-12-18
 */
public interface ImsThesisTopicMapper 
{
    /**
     * 查询课题信息
     * 
     * @param topicId 课题信息ID
     * @return 课题信息
     */
    public ImsThesisTopic selectImsThesisTopicById(String topicId);

    /**
     * 根据用户查询课题信息
     *
     * @param userId 用户ID
     * @return 课题信息
     */
    public ImsThesisTopic selectImsThesisTopicByUserId(Long userId);

    /**
     * 查询课题信息列表
     * 
     * @param imsThesisTopic 课题信息
     * @return 课题信息集合
     */
    public List<ImsThesisTopic> selectImsThesisTopicList(ImsThesisTopic imsThesisTopic);

    /**
     * 新增课题信息
     * 
     * @param imsThesisTopic 课题信息
     * @return 结果
     */
    public int insertImsThesisTopic(ImsThesisTopic imsThesisTopic);

    /**
     * 修改课题信息
     * 
     * @param imsThesisTopic 课题信息
     * @return 结果
     */
    public int updateImsThesisTopic(ImsThesisTopic imsThesisTopic);

    /**
     * 删除课题信息
     * 
     * @param topicId 课题信息ID
     * @return 结果
     */
    public int deleteImsThesisTopicById(String topicId);

    /**
     * 批量删除课题信息
     * 
     * @param topicIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteImsThesisTopicByIds(String[] topicIds);
}
