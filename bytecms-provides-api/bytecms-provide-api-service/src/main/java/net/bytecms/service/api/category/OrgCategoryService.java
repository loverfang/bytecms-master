package net.bytecms.service.api.category;
import java.util.List;

import net.bytecms.service.dto.category.CmsCategoryDto;
import net.bytecms.service.dto.category.OrgCategoryDto;
import net.bytecms.core.api.BaseService;
import net.bytecms.core.utils.Tree;

/**
 * <p>
 * 部门分类 服务类
 * </p>
 *
 * @author LG
 * @since 2019-11-13
 */
public interface OrgCategoryService extends BaseService<OrgCategoryDto> {

	/**
	 * 根据组织id获取本门分类信息
	 * @Description:   
	 * @Author:         zzx
	 * @CreateDate:     2019年11月13日
	 * @UpdateUser:     zzx
	 * @UpdateDate:     2019年11月13日
	 * @UpdateRemark:   修改内容
	 * @Version:        1.0
	 */
	List<OrgCategoryDto> listByOrgId(String orgId);

	/**
	 * 根据组织id删除本门分类信息
	 * @Description:   
	 * @Author:         zzx
	 * @CreateDate:     2019年11月13日
	 * @UpdateUser:     zzx
	 * @UpdateDate:     2019年11月13日
	 * @UpdateRemark:   修改内容
	 * @Version:        1.0
	 */
	void deleteByOrgId(String orgId);


	/**
	 * 组织分配分类
	 * @param v
	 */
	boolean saveOrgCategory(OrgCategoryDto v);


	/**
	 * 根据组织id获取对应的分类
	 * @param orgId
	 */
	Tree<CmsCategoryDto> selectTreeCategoryByOrg(String orgId);


	/**
	 * 更新至半选状态
	 * @param categoryId
	 */
	void updateCategoryToHalfChecked(String categoryId);

}