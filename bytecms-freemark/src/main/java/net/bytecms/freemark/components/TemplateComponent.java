package net.bytecms.freemark.components;

import net.bytecms.core.constants.Constants;
import net.bytecms.core.handler.CustomException;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.FileUtil;
import net.bytecms.core.utils.FreeMarkerUtils;
import freemarker.template.Configuration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 模板指令组件
 */
@Slf4j
@Data
@Component
public class TemplateComponent {

    private Configuration configuration;

    /**
     * 创建内容静态化页面
     *
     * @return
     */
    public ApiResult createContentStaticFile(String tempPath,String staticFilePath, Map<String, Object> param) {
        ApiResult apiResult =ApiResult.result();
        try {
            FreeMarkerUtils.generateFileByFile(tempPath,staticFilePath,this.configuration,param);
            apiResult.put("url",param.get("url"));
        } catch (Exception e) {
            FileUtil.deleteFile(staticFilePath);
            log.error(e.getMessage());
            log.error("创建内容静态化页面生成失败!");
            throw new CustomException(ApiResult.result(20006));
        }
        apiResult.put(Constants.staticFilePath,"内容静态化文件:"+staticFilePath);
        return apiResult;
    }


    /**
     * 创建内容列表静态化页面
     *
     * @return
     */
    public ApiResult createCategoryStaticFile(String tempPath,String staticFilePath, Map<String, Object> param) {
        ApiResult apiResult =ApiResult.result();
        try {
            FreeMarkerUtils.generateFileByFile(tempPath,staticFilePath,this.configuration,param);
        } catch (Exception e) {
            FileUtil.deleteFile(staticFilePath);
            log.error(e.getMessage());
            log.error("创建内容列表静态化页面失败!");
            apiResult=ApiResult.result(20006);
            throw new CustomException(apiResult);
        }
        apiResult.put(Constants.staticFilePath,"栏目分类静态化文件:"+staticFilePath);
        return apiResult;
    }

    /**
     * 创建首页静态化页面
     *
     * @return
     */
    public ApiResult createIndexStaticFile(String tempPath,String staticFilePath, Map<String, Object> param) {
        ApiResult apiResult =ApiResult.result();
        try {
            FreeMarkerUtils.generateFileByFile(tempPath,staticFilePath,this.configuration,param);
        } catch (Exception e) {
            FileUtil.deleteFile(staticFilePath);
            log.error(e.getMessage());
            log.error("创建首页静态化页面生成失败!");
            apiResult=ApiResult.result(20006);
            throw new CustomException(apiResult);
        }
        apiResult.put(Constants.staticFilePath,"首页静态化文件:"+staticFilePath);
        return apiResult;
    }

}
