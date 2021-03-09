package net.bytecms.service.api.fragment;
import net.bytecms.service.dto.fragment.FragmentDirectoryDto;
import net.bytecms.service.dto.fragment.FragmentFileModelDto;
import net.bytecms.service.dto.model.CmsDefaultModelFieldDto;
import net.bytecms.core.api.BaseService;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.TreeFileInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 页面片段文件模型 服务类
 * </p>
 *
 * @author LG
 * @since 2019-11-07
 */
public interface FragmentFileModelService extends BaseService<FragmentFileModelDto> {


    /**
     * 查询页面片段文件
     * @return
     */
    TreeFileInfo treeFragmentFiles();

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
     * 创建页面片段以及保存数据库
     * @param v
     */
    void saveFragmentFile(FragmentFileModelDto v) ;

    /**
     * 创建目录
     * @param v
     */
    void saveFragmentDirectory(FragmentDirectoryDto v) ;

    FragmentFileModelDto getInfoByPk(String id);

    /**
     * 查询系统默认字段
     * @return
     */
    List<CmsDefaultModelFieldDto> listDefaultField();

    /**
     * 更新页面片段 设计
     * @param v
     */
    void updateFragmentDesignFile(FragmentFileModelDto v);

    /**
     * 获取页面片段数据（编辑时查看详情）
     * @param id
     * @return
     */
    FragmentFileModelDto getFragmentDesignByPk(String id);

    /**
     * 根据code 获取页面片段文件位置
     * @param code
     * @return
     */
    String getFragmentFilePathByCode(String code);


    /**
     * 打包zip 下载
     * @param path
     */
    void downZip(String path, HttpServletResponse response);
}