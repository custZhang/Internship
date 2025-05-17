package com.internship.file.service.impl;

import com.internship.common.config.InternshipConfig;
import com.internship.common.exception.BaseException;
import com.internship.common.utils.StringUtils;
import com.internship.file.domain.ImsFileInfo;
import com.internship.file.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @javax.annotation.Resource
    private InternshipConfig internshipConfig;

    /**
     * 根据文件信息获取文件资源
     * @param imsFileInfo
     * @return
     */
    @Override
    public Resource loadAsResource(ImsFileInfo imsFileInfo) {
        try {
            Path file = load(imsFileInfo);
            Resource resource = new UrlResource(file.toUri());
            log.info("resource："+resource);
            log.info("文件uri为："+file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new BaseException(
                        "读取不到文件: " + imsFileInfo);
            }
        } catch (MalformedURLException e) {
            throw new BaseException("读取不到文件: " + imsFileInfo);
        }
    }

    /**
     * 获取文件路径
     * @param imsFileInfo 文件信息
     * @return
     */
    @Override
    public Path load(ImsFileInfo imsFileInfo) {
        return Paths.get(replaceFilePath(imsFileInfo.getFilePath()));
    }

    /**
     * 下载时：文件路径替换
     * @param filePath
     * @return 路径
     */
    public String replaceFilePath(String filePath){
        String path = "";
        if (StringUtils.isNotEmpty(filePath)){
            log.info("默认上传路径为："+internshipConfig.getProfile());
            path = filePath.replace("/profile",internshipConfig.getProfile());
            log.info("文件真实路径为："+path);
        }
        return path;
    }
}
