package com.internship.web.controller.ims.file;

import com.internship.common.annotation.Log;
import com.internship.common.core.controller.BaseController;
import com.internship.common.core.domain.AjaxResult;
import com.internship.common.core.domain.entity.SysUser;
import com.internship.common.core.page.TableDataInfo;
import com.internship.common.enums.BusinessType;
import com.internship.common.utils.SecurityUtils;
import com.internship.common.utils.StringUtils;
import com.internship.common.utils.file.FileUploadUtils;
import com.internship.file.domain.File;
import com.internship.file.domain.ImsFileInfo;
import com.internship.file.service.IImsFileInfoService;
import com.internship.framework.aspectj.LogAspect;
import com.internship.system.service.ISysUserService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件信息Controller
 * 
 * @author internship
 * @date 2020-12-08
 */
@RestController
@RequestMapping("/ims/file")
public class ImsFileInfoController extends BaseController
{
    private Logger log = LoggerFactory.getLogger(LogAspect.class);
    @Resource
    private IImsFileInfoService imsFileInfoService;
    @Resource
    private ISysUserService userService;

    /**
     * 查询文件信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ImsFileInfo imsFileInfo)
    {
        startPage();
        List<ImsFileInfo> list = imsFileInfoService.selectImsFileInfoList(imsFileInfo);
        return getDataTable(list);
    }

    /**
     * 获取文件信息详细信息
     */
    @RequestMapping(value = "/download/{fileId}",method={RequestMethod.GET,RequestMethod.POST})
    public void download(HttpServletResponse response, @PathVariable("fileId") String fileId) throws IOException {
        log.info("download:{}",fileId);
        File file = imsFileInfoService.getFile(fileId);
        log.info("文件为："+imsFileInfoService.getFile(fileId));
        InputStream inputStream = file.getFileContent().getInputStream();
        int length = inputStream.available();
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\""+ URLEncoder.encode(file.getFileName())+"\"");
        response.addHeader("Content-Length", "" + length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.copy(inputStream, response.getOutputStream());
    }

    /**
     * 新增文件信息
     */
    @Log(title = "文件信息", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    public AjaxResult upload(
            @RequestParam(value = "fileName",required = false) String fileName
            , @RequestParam("file") MultipartFile file)
    {
        try {
            if(StringUtils.isBlank(fileName)){
                fileName = file.getOriginalFilename();
            }
            String filePath = FileUploadUtils.upload(file);
            SysUser user = userService.selectUserByUserName(SecurityUtils.getUsername());
            String deptName = user.getDept().getDeptName();
            ImsFileInfo fileInfo = imsFileInfoService.insertImsFileInfo(fileName, filePath, file,deptName);
            log.info("文件信息为："+filePath);
            return AjaxResult.success(fileInfo);
        }catch (Exception e){
            log.error("文件上传失败,{}",fileName,e);
            return AjaxResult.error("文件上传失败");
        }
    }

    /**
     * 删除文件信息
     */
    @Log(title = "文件信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{fileIds}")
    public AjaxResult remove(@PathVariable String[] fileIds)
    {
        return toAjax(imsFileInfoService.deleteImsFileInfoByIds(fileIds));
    }
}
