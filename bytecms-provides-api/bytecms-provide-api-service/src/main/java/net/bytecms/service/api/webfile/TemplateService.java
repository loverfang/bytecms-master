package net.bytecms.service.api.webfile;

import net.bytecms.service.dto.webfile.TemplateDto;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.TreeFileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface TemplateService  {

    /**
     * 获取网站模板文件树
     * @return
     */
    TreeFileInfo treeTempFile();

    /**
     * 获取文件内容
     * @param path
     * @return
     */
    ApiResult getFileContent(String path);

    /**
     * 保存文件内容至文件
     * @param path
     * @param content
     * @return
     */
    ApiResult setFileContent(String path, String content);

    /**
     * 删除文件或者目录
     * @param filePath
     * @return
     */
    ApiResult deleteFile(String filePath);

    /**
     * 创建模板
     * @param templateDto
     */
    void saveTemp(TemplateDto templateDto);


    /**
     * 批量打包下载
     * @param path
     * @param response
     */
    void downZip(String path, HttpServletResponse response);


    /**
     * 文件导入，导入
     * @param multipartFile
     * @param type
     */
    void importFile(MultipartFile multipartFile, Integer type);

    /**
     * 获取指令
     * @return
     */
    ApiResult getDirects();
}
