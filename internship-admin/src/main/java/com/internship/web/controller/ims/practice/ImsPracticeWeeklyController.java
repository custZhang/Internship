package com.internship.web.controller.ims.practice;

import com.internship.common.annotation.Log;
import com.internship.common.core.controller.BaseController;
import com.internship.common.core.domain.AjaxResult;
import com.internship.common.core.domain.entity.SysUser;
import com.internship.common.core.page.TableDataInfo;
import com.internship.common.enums.BusinessType;
import com.internship.common.utils.SecurityUtils;
import com.internship.common.utils.StringUtils;
import com.internship.common.utils.poi.ExcelUtil;
import com.internship.practice.domain.ImsPracticeWeekly;
import com.internship.practice.service.IImsPracticeWeeklyService;
import com.internship.system.service.ISysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 实习考核信息Controller
 * 
 * @author internship
 * @date 2020-11-26
 */
@RestController
@RequestMapping("/practice/weekly")
public class ImsPracticeWeeklyController extends BaseController
{
    @Resource
    private IImsPracticeWeeklyService imsPracticeWeeklyService;

    @Resource
    private ISysUserService sysUserService;

    /**
     * 查询实习考核信息列表
     */
    @GetMapping("/list")
    public TableDataInfo getWeekList(ImsPracticeWeekly imsPracticeWeekly)
    {
        startPage();
        SysUser user = SecurityUtils.getLoginUser().getUser();
        Long userId = user.getUserId();
        Long roleId = user.getRoleId();
        if (StringUtils.isNotNull(roleId) && 100 == roleId ){
            imsPracticeWeekly.setUserId(userId);
        }
        List<ImsPracticeWeekly> list = imsPracticeWeeklyService.selectImsPracticeWeeklyList(imsPracticeWeekly);
        return getDataTable(list);
    }
    /**
     * 导出实习考核信息列表
     */
    @Log(title = "实习考核信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ImsPracticeWeekly imsPracticeWeekly)
    {
        List<ImsPracticeWeekly> list = imsPracticeWeeklyService.selectImsPracticeWeeklyList(imsPracticeWeekly);
        ExcelUtil<ImsPracticeWeekly> util = new ExcelUtil<ImsPracticeWeekly>(ImsPracticeWeekly.class);
        return util.exportExcel(list, "weekly");
    }

    /**
     * 获取实习考核信息详细信息
     */
    @GetMapping(value = "/{weeklyId}")
    public AjaxResult getInfo(@PathVariable("weeklyId") Long weeklyId)
    {
        return AjaxResult.success(imsPracticeWeeklyService.selectImsPracticeWeeklyById(weeklyId));
    }

    /**
     * 新增实习考核信息
     */
    @Log(title = "实习考核信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ImsPracticeWeekly imsPracticeWeekly)
    {
        SysUser user = sysUserService.selectUserByUserName(SecurityUtils.getUsername());
        String deptName = user.getDept().getDeptName();
        Long userId = user.getUserId();
        String userName = user.getUserName();
        imsPracticeWeekly.setCreateDept(deptName);     //登录人所属院系/班级
        imsPracticeWeekly.setCreateBy(userName);       //创建者
        imsPracticeWeekly.setUserId(userId);           //登录用户ID
        return toAjax(imsPracticeWeeklyService.insertImsPracticeWeekly(imsPracticeWeekly));
    }

    /**
     * 修改实习考核信息
     */
    @Log(title = "实习考核信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ImsPracticeWeekly imsPracticeWeekly)
    {
        SysUser user = sysUserService.selectUserByUserName(SecurityUtils.getUsername());
        String userName = user.getUserName();
        imsPracticeWeekly.setUpdateBy(userName);     //更新者
        return toAjax(imsPracticeWeeklyService.updateImsPracticeWeekly(imsPracticeWeekly));
    }

    /**
     * 删除实习考核信息
     */
    @Log(title = "实习考核信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{weeklyIds}")
    public AjaxResult remove(@PathVariable Long[] weeklyIds)
    {
        return toAjax(imsPracticeWeeklyService.deleteImsPracticeWeeklyByIds(weeklyIds));
    }
}
