package net.bytecms.service.api.resource;

import net.bytecms.core.api.BaseService;
import net.bytecms.service.dto.resource.SysResourceDto;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LG
 * @since 2019-11-11
 */
public interface SysResourceService extends BaseService<SysResourceDto> {


	String getfilePathById(String id);

    void deleteByFilePath(String filePath);
}