package com.internship.web.controller.ims.thesis;

import com.internship.common.annotation.Log;
import com.internship.common.core.controller.BaseController;
import com.internship.common.core.domain.AjaxResult;
import com.internship.common.core.domain.entity.SysUser;
import com.internship.common.core.page.TableDataInfo;
import com.internship.common.enums.BusinessType;
import com.internship.common.utils.SecurityUtils;
import com.internship.common.utils.poi.ExcelUtil;
import com.internship.system.service.ISysUserService;
import com.internship.thesis.domain.ImsThesisTopic;
import com.internship.thesis.service.IImsThesisTopicService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 课题信息Controller
 * 
 * @author internship
 * @date 2020-12-18
 */
@RestController
@RequestMapping("/thesis/topic")
public class ImsThesisTopicController extends BaseController
{
    @Resource
    private IImsThesisTopicService imsThesisTopicService;
    @Resource
    private ISysUserService sysUserService;

    /**
     * 查询课题信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ImsThesisTopic imsThesisTopic)
    {
        startPage();
        List<ImsThesisTopic> list = imsThesisTopicService.selectImsThesisTopicList(imsThesisTopic);
        return getDataTable(list);
    }

    /**
     * 导出课题信息列表
     */
    @Log(title = "课题信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ImsThesisTopic imsThesisTopic)
    {
        List<ImsThesisTopic> list = imsThesisTopicService.selectImsThesisTopicList(imsThesisTopic);
        ExcelUtil<ImsThesisTopic> util = new ExcelUtil<ImsThesisTopic>(ImsThesisTopic.class);
        return util.exportExcel(list, "topic");
    }

    /**
     * 获取课题信息详细信息
     */
    @GetMapping(value = "/{topicId}")
    public AjaxResult getInfo(@PathVariable("topicId") String topicId)
    {
        return AjaxResult.success(imsThesisTopicService.selectImsThesisTopicById(topicId));
    }

    /**
     * 获取课题信息详细信息
     */
    @GetMapping(value = "/userId")
    public AjaxResult getTopicInfoByUserId(@RequestParam("userId") Long userId)
    {
        return AjaxResult.success(imsThesisTopicService.selectImsThesisTopicByUserId(userId));
    }

    /**
     * 新增课题信息
     */
    @Log(title = "课题信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ImsThesisTopic imsThesisTopic)
    {
        SysUser user = sysUserService.selectUserByUserName(SecurityUtils.getUsername());
        imsThesisTopic.setCreateDept(user.getDept().getDeptName());
        imsThesisTopic.setCreateBy(user.getUserName());
        return toAjax(imsThesisTopicService.insertImsThesisTopic(imsThesisTopic));
    }

    /**
     * 修改课题信息
     */
    @Log(title = "课题信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ImsThesisTopic imsThesisTopic)
    {
        SysUser user = sysUserService.selectUserByUserName(SecurityUtils.getUsername());
        imsThesisTopic.setUpdateBy(user.getUserName());
        return toAjax(imsThesisTopicService.updateImsThesisTopic(imsThesisTopic));
    }

    /**
     * 删除课题信息
     */
    @Log(title = "课题信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{topicIds}")
    public AjaxResult remove(@PathVariable String[] topicIds)
    {
        return toAjax(imsThesisTopicService.deleteImsThesisTopicByIds(topicIds));
    }
}
