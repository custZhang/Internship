package com.internship.web.controller.ims.thesis;

import com.internship.common.annotation.Log;
import com.internship.common.core.controller.BaseController;
import com.internship.common.core.domain.AjaxResult;
import com.internship.common.core.page.TableDataInfo;
import com.internship.common.enums.BusinessType;
import com.internship.common.utils.poi.ExcelUtil;
import com.internship.thesis.domain.ImsThesisInspection;
import com.internship.thesis.service.IImsThesisInspectionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 论文中期检查信息Controller
 * 
 * @author internship
 * @date 2020-12-18
 */
@RestController
@RequestMapping("/thesis/inspection")
public class ImsThesisInspectionController extends BaseController
{
    @Resource
    private IImsThesisInspectionService imsThesisInspectionService;

    /**
     * 查询论文中期检查信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ImsThesisInspection imsThesisInspection)
    {
        startPage();
        List<ImsThesisInspection> list = imsThesisInspectionService.selectImsThesisInspectionList(imsThesisInspection);
        return getDataTable(list);
    }

    /**
     * 导出论文中期检查信息列表
     */
    @Log(title = "论文中期检查信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ImsThesisInspection imsThesisInspection)
    {
        List<ImsThesisInspection> list = imsThesisInspectionService.selectImsThesisInspectionList(imsThesisInspection);
        ExcelUtil<ImsThesisInspection> util = new ExcelUtil<ImsThesisInspection>(ImsThesisInspection.class);
        return util.exportExcel(list, "inspection");
    }

    /**
     * 获取论文中期检查信息详细信息
     */
    @GetMapping(value = "/{inspectId}")
    public AjaxResult getInfo(@PathVariable("inspectId") Long inspectId)
    {
        return AjaxResult.success(imsThesisInspectionService.selectImsThesisInspectionById(inspectId));
    }

    /**
     * 新增论文中期检查信息
     */
    @Log(title = "论文中期检查信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ImsThesisInspection imsThesisInspection)
    {
        return toAjax(imsThesisInspectionService.insertImsThesisInspection(imsThesisInspection));
    }

    /**
     * 修改论文中期检查信息
     */
    @Log(title = "论文中期检查信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ImsThesisInspection imsThesisInspection)
    {
        return toAjax(imsThesisInspectionService.updateImsThesisInspection(imsThesisInspection));
    }

    /**
     * 删除论文中期检查信息
     */
    @Log(title = "论文中期检查信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{inspectIds}")
    public AjaxResult remove(@PathVariable Long[] inspectIds)
    {
        return toAjax(imsThesisInspectionService.deleteImsThesisInspectionByIds(inspectIds));
    }
}
