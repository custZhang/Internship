package com.internship.web.controller.system;

import com.internship.common.annotation.Log;
import com.internship.common.core.controller.BaseController;
import com.internship.common.core.domain.AjaxResult;
import com.internship.common.core.domain.entity.SysUser;
import com.internship.common.core.page.TableDataInfo;
import com.internship.common.enums.BusinessType;
import com.internship.common.utils.SecurityUtils;
import com.internship.common.utils.StringUtils;
import com.internship.system.domain.SysNotice;
import com.internship.system.service.ISysNoticeService;
import com.internship.system.service.ISysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告 信息操作处理
 * 
 * @author internship
 */
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController
{
    @Resource
    private ISysNoticeService noticeService;
    @Resource
    private ISysUserService userService;

    /**
     * 获取通知公告列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice)
    {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 获取用户与通知公告列表
     */
    @GetMapping("/isReadList")
    public TableDataInfo isReadList(SysNotice notice)
    {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeIsReadList(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId)
    {
        return AjaxResult.success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysNotice notice)
    {
        SysUser user = userService.selectUserByUserName(SecurityUtils.getUsername());
        String userName = user.getUserName();
        if (StringUtils.isNull(notice.getDeptId())){
            Long deptId = user.getDeptId();
            notice.setDeptId(deptId);
        }
        notice.setCreateBy(userName);
        noticeService.insertNotice(notice);
        return AjaxResult.success();
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysNotice notice)
    {
        notice.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds)
    {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
