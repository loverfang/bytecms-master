package net.bytecms.service.api.content;
import net.bytecms.service.dto.content.ContentFileDto;
import net.bytecms.service.dto.resource.SysResourceDto;
import net.bytecms.core.api.BaseService;

import java.util.List;

/**
 * <p>
 * 内容附件 服务类
 * </p>
 *
 * @author LG
 * @since 2019-12-07
 */
public interface ContentFileService extends BaseService<ContentFileDto> {


    /**
     * 保存内容附件
     * @param attachFiles
     */
    void saveContentFiles(List<ContentFileDto> attachFiles,String contentId);

    /**
     * 获取内容附件
     * @param id
     * @return
     */
    List<SysResourceDto> getAttachByContnet(String content_id);
}