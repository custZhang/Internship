package com.internship.task.service.impl;

import com.internship.common.exception.CustomException;
import com.internship.common.utils.DateUtils;
import com.internship.common.utils.StringUtils;
import com.internship.commons.ImsConstants;
import com.internship.task.domain.ImsApprovalDetails;
import com.internship.task.domain.ImsTaskChild;
import com.internship.task.domain.ImsTaskChildTopic;
import com.internship.task.domain.ImsTaskConfig;
import com.internship.task.mapper.ImsApprovalDetailsMapper;
import com.internship.task.mapper.ImsTaskConfigMapper;
import com.internship.task.service.IImsApprovalDetailsService;
import com.internship.task.service.IImsTaskChildService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 审批明细信息Service业务层处理
 * 
 * @author internship
 * @date 2021-01-11
 */
@Service
public class ImsApprovalDetailsServiceImpl implements IImsApprovalDetailsService 
{
    @Resource
    private ImsApprovalDetailsMapper imsApprovalDetailsMapper;

    @Resource
    private IImsTaskChildService taskChildService;

    @Resource
    private ImsTaskConfigMapper taskConfigMapper;

    @Resource
    private ImsTaskChildTopicServiceImpl topicService;

    /**
     * 查询审批明细信息
     * 
     * @param approvalId 审批明细信息ID
     * @return 审批明细信息
     */
    @Override
    public ImsApprovalDetails selectImsApprovalDetailsById(Long approvalId)
    {
        return imsApprovalDetailsMapper.selectImsApprovalDetailsById(approvalId);
    }

    /**
     * 查询审批明细信息列表
     * 
     * @param imsApprovalDetails 审批明细信息
     * @return 审批明细信息
     */
    @Override
    public List<ImsApprovalDetails> selectImsApprovalDetailsList(ImsApprovalDetails imsApprovalDetails)
    {
        return imsApprovalDetailsMapper.selectImsApprovalDetailsList(imsApprovalDetails);
    }

    /**
     * 新增审批明细信息
     * 
     * @param imsApprovalDetails 审批明细信息
     * @return 结果
     */
    @Override
    public int insertImsApprovalDetails(ImsApprovalDetails imsApprovalDetails)
    {
        imsApprovalDetails.setCreateTime(DateUtils.getNowDate());
        imsApprovalDetails.setUpdateTime(DateUtils.getNowDate());
        String approvalConclusion = imsApprovalDetails.getApprovalConclusion().equals(ImsConstants.STATUS_YES) ? "T" : "F";
        String approvalStatus = approvalConclusion.equals(ImsConstants.TRUE) ? "E" : "S";
        imsApprovalDetails.setApprovalStatus(approvalStatus);
        imsApprovalDetails.setApprovalConclusion(approvalConclusion);
        //论文任务
        if (ImsConstants.TASK_PROCESS_TYPE_2.equals(imsApprovalDetails.getTaskProcessType())){
            if(StringUtils.isNotNull(imsApprovalDetails.getApprovalConclusion()) && StringUtils.isNotNull(imsApprovalDetails.getChildId())){
                ImsTaskChildTopic taskChild = topicService.selectImsTaskChildTopicById(imsApprovalDetails.getChildId());
                //流程配置表
                ImsTaskConfig config = new ImsTaskConfig();
                config.setProcessType(taskChild.getProcessType());
                config.setStage(taskChild.getChildTaskStatus());
                config.setProcessChildType(taskChild.getChildProcessType());
                config.setIsCheckNode(ImsConstants.IS_CHECK_NODE_YES);
                config.setCheckConclusion(imsApprovalDetails.getApprovalConclusion());
                ImsTaskConfig taskConfig = taskConfigMapper.selectImsTaskConfig(config);
                //流程结束
                if (null != taskConfig && ImsConstants.PROCESS_END_Y.equals(taskConfig.getProcessEnd())){
                    taskChild.setChildTaskProcess(ImsConstants.TASK_PROCESS_02);
                    taskChild.setChildTaskStatus(ImsConstants.TASK_STAGE_Y04);
                    topicService.updateImsTaskChildTopic(taskChild);
                }else {
                    if (null == taskConfig.getNextProcessId()){
                        throw new CustomException("未找到下一流程信息");
                    }
                    ImsTaskConfig imsTaskConfig = taskConfigMapper.selectImsTaskConfigById(taskConfig.getNextProcessId());
                    ImsTaskChildTopic child = topicService.selectImsTaskChildTopicById(imsApprovalDetails.getChildId());
                    child.setChildTaskStatus(imsTaskConfig.getStage());
                    topicService.updateImsTaskChildTopic(child);
                }
            }
        }else {
            //审核通过的
            if(StringUtils.isNotNull(imsApprovalDetails.getApprovalConclusion()) && StringUtils.isNotNull(imsApprovalDetails.getChildId())){
                ImsTaskChild taskChild = taskChildService.selectImsTaskChildById(imsApprovalDetails.getChildId());
                //流程配置表
                ImsTaskConfig config = new ImsTaskConfig();
                config.setProcessType(taskChild.getProcessType());
                config.setStage(taskChild.getChildTaskStatus());
                config.setProcessChildType(taskChild.getChildProcessType());
                config.setIsCheckNode(ImsConstants.IS_CHECK_NODE_YES);
                config.setCheckConclusion(imsApprovalDetails.getApprovalConclusion());
                ImsTaskConfig taskConfig = taskConfigMapper.selectImsTaskConfig(config);
                //流程结束
                if (null != taskConfig && ImsConstants.PROCESS_END_Y.equals(taskConfig.getProcessEnd())){
                    taskChild.setChildTaskProcess(ImsConstants.TASK_PROCESS_02);
                    taskChild.setChildTaskStatus(ImsConstants.TASK_STAGE_Y04);
                    taskChildService.updateImsTaskChild(taskChild);
                }else {
                    if (null == taskConfig.getNextProcessId()){
                        throw new CustomException("未找到下一流程信息");
                    }
                    ImsTaskConfig imsTaskConfig = taskConfigMapper.selectImsTaskConfigById(taskConfig.getNextProcessId());
                    ImsTaskChild child = taskChildService.selectImsTaskChildById(imsApprovalDetails.getChildId());
                    child.setChildTaskStatus(imsTaskConfig.getStage());
                    child.setNextRoleUser(imsTaskConfig.getRole());
                    taskChildService.updateImsTaskChild(child);
                }
            }
        }
        return imsApprovalDetailsMapper.insertImsApprovalDetails(imsApprovalDetails);
    }



    /**
     * 修改审批明细信息
     * 
     * @param imsApprovalDetails 审批明细信息
     * @return 结果
     */
    @Override
    public int updateImsApprovalDetails(ImsApprovalDetails imsApprovalDetails)
    {
        imsApprovalDetails.setUpdateTime(DateUtils.getNowDate());
        return imsApprovalDetailsMapper.updateImsApprovalDetails(imsApprovalDetails);
    }

    /**
     * 批量删除审批明细信息
     * 
     * @param approvalIds 需要删除的审批明细信息ID
     * @return 结果
     */
    @Override
    public int deleteImsApprovalDetailsByIds(Long[] approvalIds)
    {
        return imsApprovalDetailsMapper.deleteImsApprovalDetailsByIds(approvalIds);
    }

    /**
     * 删除审批明细信息信息
     * 
     * @param approvalId 审批明细信息ID
     * @return 结果
     */
    @Override
    public int deleteImsApprovalDetailsById(Long approvalId)
    {
        return imsApprovalDetailsMapper.deleteImsApprovalDetailsById(approvalId);
    }

}
