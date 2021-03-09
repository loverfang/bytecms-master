package net.bytecms.service.api.model;

import net.bytecms.service.dto.model.CmsDefaultModelFieldDto;
import net.bytecms.service.dto.model.CmsModelDto;
import net.bytecms.core.api.BaseService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LG
 * @since 2019-10-23
 */
public interface CmsModelService extends BaseService<CmsModelDto> {

  /**
   * 获取系统默认字段
   * @return
   */
  List<CmsDefaultModelFieldDto> listDefaultField();


    List<CmsModelDto> listModelByCategory(CmsModelDto v);
}
