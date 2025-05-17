package com.internship.web.controller.ims.task;

import com.internship.common.annotation.Log;
import com.internship.common.core.controller.BaseController;
import com.internship.common.core.domain.AjaxResult;
import com.internship.common.core.domain.entity.SysUser;
import com.internship.common.core.page.TableDataInfo;
import com.internship.common.enums.BusinessType;
import com.internship.common.utils.SecurityUtils;
import com.internship.common.utils.poi.ExcelUtil;
import com.internship.system.service.ISysUserService;
import com.internship.task.domain.ImsTaskInfo;
import com.internship.task.service.IImsTaskInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 任务信息Controller
 * 
 * @author internship
 * @date 2020-12-25
 */
@RestController
@RequestMapping("/ims/taskInfo")
public class ImsTaskInfoController extends BaseController
{
    @Resource
    private IImsTaskInfoService imsTaskInfoService;
    @Resource
    private ISysUserService sysUserService;

    /**
     * 查询任务信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ImsTaskInfo imsTaskInfo)
    {
        startPage();
        List<ImsTaskInfo> list = imsTaskInfoService.selectImsTaskInfoList(imsTaskInfo);
        return getDataTable(list);
    }

    /**
     * 导出任务信息列表
     */
    @Log(title = "任务信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ImsTaskInfo imsTaskInfo)
    {
        List<ImsTaskInfo> list = imsTaskInfoService.selectImsTaskInfoList(imsTaskInfo);
        ExcelUtil<ImsTaskInfo> util = new ExcelUtil<ImsTaskInfo>(ImsTaskInfo.class);
        return util.exportExcel(list, "taskInfo");
    }

    /**
     * 获取任务信息详细信息
     */
    @GetMapping(value = "/{taskId}")
    public AjaxResult getInfo(@PathVariable("taskId") Long taskId)
    {
        return AjaxResult.success(imsTaskInfoService.selectImsTaskInfoById(taskId));
    }

    /**
     * 新增任务信息
     */
    @Log(title = "任务信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ImsTaskInfo imsTaskInfo)
    {
        SysUser user = sysUserService.selectUserByUserName(SecurityUtils.getUsername());
        String deptName = user.getDept().getDeptName();
        String userName = user.getUserName();
        imsTaskInfo.setCreateDept(deptName);
        imsTaskInfo.setCreateBy(userName);
        imsTaskInfoService.insertImsTaskInfo(imsTaskInfo);
        return AjaxResult.success();
    }

    /**
     * 修改任务信息
     */
    @Log(title = "任务信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ImsTaskInfo imsTaskInfo)
    {
        SysUser user = sysUserService.selectUserByUserName(SecurityUtils.getUsername());
        String userName = user.getUserName();
        imsTaskInfo.setUpdateBy(userName);
        return toAjax(imsTaskInfoService.updateImsTaskInfo(imsTaskInfo));
    }

    /**
     * 删除任务信息
     */
    @Log(title = "任务信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{taskIds}")
    public AjaxResult remove(@PathVariable Long[] taskIds)
    {
        return toAjax(imsTaskInfoService.deleteImsTaskInfoByIds(taskIds));
    }
}
