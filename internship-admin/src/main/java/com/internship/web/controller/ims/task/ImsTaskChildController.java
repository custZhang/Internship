package com.internship.web.controller.ims.task;

import com.internship.common.annotation.Log;
import com.internship.common.core.controller.BaseController;
import com.internship.common.core.domain.AjaxResult;
import com.internship.common.core.page.TableDataInfo;
import com.internship.common.enums.BusinessType;
import com.internship.common.utils.SecurityUtils;
import com.internship.common.utils.poi.ExcelUtil;
import com.internship.commons.ImsConstants;
import com.internship.task.domain.ImsTaskChild;
import com.internship.task.domain.vo.practiceVo.ImsChildPracticeVo;
import com.internship.task.domain.vo.practiceVo.ImsPracticeApplicationVo;
import com.internship.task.service.IImsTaskChildService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 子任务信息Controller
 * 
 * @author internship
 * @date 2020-12-25
 */
@RestController
@RequestMapping("/ims/child")
public class ImsTaskChildController extends BaseController
{
    @Resource
    private IImsTaskChildService imsTaskChildService;

    /**
     * 查询子任务信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ImsTaskChild imsTaskChild)
    {
        startPage();
        List<ImsTaskChild> list = imsTaskChildService.selectImsTaskChildList(imsTaskChild);
        return getDataTable(list);
    }

    /**
     * 查询实习周记、总结子任务信息列表
     */
    @GetMapping("/practice/list")
    public TableDataInfo childPracticeList(ImsChildPracticeVo imsChildPracticeVo)
    {
        startPage();
        imsChildPracticeVo.setNextUser(SecurityUtils.getLoginUser().getUser().getUserId());
        List<ImsChildPracticeVo> list = imsTaskChildService.selectChildPracticeList(imsChildPracticeVo);
        return getDataTable(list);
    }

    /**
     * 查询实习申请子任务信息列表
     */
    @GetMapping("/application/list")
    public TableDataInfo practiceApplicationList(ImsPracticeApplicationVo vo)
    {
        startPage();
        vo.setChildProcessType(ImsConstants.CHILD_PROCESS_TYPE_I00);
        vo.setNextUser(SecurityUtils.getLoginUser().getUser().getUserId());
        List<ImsPracticeApplicationVo> list = imsTaskChildService.selectPracticeApplicationList(vo);
        return getDataTable(list);
    }


    /**
     * 导出子任务信息列表
     */
    @Log(title = "子任务信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ImsTaskChild imsTaskChild)
    {
        List<ImsTaskChild> list = imsTaskChildService.selectImsTaskChildList(imsTaskChild);
        ExcelUtil<ImsTaskChild> util = new ExcelUtil<ImsTaskChild>(ImsTaskChild.class);
        return util.exportExcel(list, "child");
    }

    /**
     * 获取子任务信息详细信息
     */
    @GetMapping(value = "/{childId}")
    public AjaxResult getInfo(@PathVariable("childId") Long childId)
    {
        return AjaxResult.success(imsTaskChildService.selectImsTaskChildById(childId));
    }

    /**
     * 新增子任务信息
     */
    @Log(title = "子任务信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ImsTaskChild imsTaskChild)
    {
        imsTaskChildService.insertImsTaskChild(imsTaskChild);
        return AjaxResult.success();
    }

    /**
     * 修改子任务信息
     */
    @Log(title = "子任务信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ImsTaskChild imsTaskChild)
    {
        return toAjax(imsTaskChildService.updateImsTaskChild(imsTaskChild));
    }

    /**
     * 删除子任务信息
     */
    @Log(title = "子任务信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{childIds}")
    public AjaxResult remove(@PathVariable Long[] childIds)
    {
        return toAjax(imsTaskChildService.deleteImsTaskChildByIds(childIds));
    }
}
