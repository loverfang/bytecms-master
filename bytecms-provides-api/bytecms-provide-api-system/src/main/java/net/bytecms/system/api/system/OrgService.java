package net.bytecms.system.api.system;

import net.bytecms.core.api.BaseService;
import net.bytecms.system.dto.system.OrgDto;
import net.bytecms.core.utils.Tree;

import java.util.List;

public interface OrgService extends BaseService<OrgDto> {

	Tree<OrgDto> selectTreeList();

	void saveOrg(OrgDto orgDTO);

	boolean deleteOrg(String id);

    OrgDto info(String id);

	void deleteByOrgCode(String code);
	
	List<OrgDto> listByParentId(String parentId);

	/*
	 * @Author LG
	 * @Description 根据当前登陆用户查询当前用户所在公司的指定部门
	 * @Date 15:03 2019/7/13
	 * @Param [userId]
	 * @return java.util.List<com.intdna.qao.service.dto.system.OrgDto>
	 **/
	List<OrgDto> getOrgsByUserId(String userId, String orgCode);




	//根据组织id和角色标识获取用户（获取指定部门下的某个角色的所有用户id）
	//List<String> listUserIdsByOrgAndRole(List<String> orgIds, RoleEnum roleEnum);
}
