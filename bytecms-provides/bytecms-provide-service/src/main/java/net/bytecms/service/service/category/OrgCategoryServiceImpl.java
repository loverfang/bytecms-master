package net.bytecms.service.service.category;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import net.bytecms.service.api.category.CmsCategoryService;
import net.bytecms.service.api.category.OrgCategoryService;
import net.bytecms.service.dto.category.CmsCategoryDto;
import net.bytecms.service.dto.category.OrgCategoryDto;
import net.bytecms.service.entity.category.OrgCategory;
import net.bytecms.service.mapper.category.OrgCategoryMapper;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.core.utils.BuildTree;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门分类 服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-11-13
 */
@Transactional
@Service
public class OrgCategoryServiceImpl extends BaseServiceImpl<OrgCategoryDto, OrgCategory, OrgCategoryMapper> implements OrgCategoryService {


    @Autowired
    CmsCategoryService cmsCategoryService;

    @Override
    public List<OrgCategoryDto> listByOrgId(String orgId) {
        QueryWrapper<OrgCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("org_id", orgId).eq("half_checked", 1);
        List<OrgCategory> categoryList = this.baseMapper.selectList(wrapper);
        return Checker.BeNotEmpty(categoryList) ? T2DList(categoryList) : Lists.newArrayList();
    }

    @Override
    public void deleteByOrgId(String orgId) {
        QueryWrapper<OrgCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("org_id", orgId);
        this.baseMapper.delete(wrapper);
    }


    @Transactional
    @Override
    public boolean saveOrgCategory(OrgCategoryDto v) {
        List<String> halfCheckedKeys = v.getHalfCheckedKeys();//半选中
        List<String> onCheckKeys = v.getOnCheckKeys();//全选中
        String orgId = v.getOrgId();
        deleteByOrgId(orgId);
        if (onCheckKeys.size() == 1 && onCheckKeys.get(0).equals("-1")) {//全部删除
            return true;
        } else {
            List<OrgCategoryDto> halfList = filterList(halfCheckedKeys, orgId, 0);
            List<OrgCategoryDto> checkList = filterList(onCheckKeys, orgId, 1);
            List<OrgCategoryDto> allList = new ArrayList<>();
            allList.addAll(halfList);
            allList.addAll(checkList);
            return insertBatch(allList);
        }
    }

    private List<OrgCategoryDto> filterList(List<String> keys, String orgId, Integer halfChecked) {
        List<OrgCategoryDto> orgCategoryDtos = new ArrayList<>();
        for (String categoryId : keys) {
            if (categoryId.equals("-1")) continue;
            OrgCategoryDto orgCategory = new OrgCategoryDto();
            orgCategory.setOrgId(orgId).setId(generateId()).setCategoryId(categoryId).setHalfChecked(halfChecked);
            orgCategoryDtos.add(orgCategory);
        }
        return orgCategoryDtos;
    }


    @Override
    public Tree<CmsCategoryDto> selectTreeCategoryByOrg(String orgId) {
        List<OrgCategoryDto> orgCategoryList = listByOrgId(orgId);//获取已经分配的栏目
        List<Tree<CmsCategoryDto>> trees = new ArrayList<Tree<CmsCategoryDto>>();
        List<String> categoryIdList = new ArrayList<>();
        List<CmsCategoryDto> cmsCategoryList = cmsCategoryService.listDtoByMap(new HashMap<>());//获取全部栏目
        if (Checker.BeNotNull(orgCategoryList)) {  //已经分配的栏目id
            for (OrgCategoryDto category : orgCategoryList) {
                categoryIdList.add(category.getCategoryId());
            }
        }
        for (CmsCategoryDto cmsCategory : cmsCategoryList) { //全部的栏目放入树上
            Tree<CmsCategoryDto> tree = new Tree<CmsCategoryDto>();
            tree.setKey(cmsCategory.getId());
            tree.setId(cmsCategory.getId());
            tree.setParentId(cmsCategory.getParentId());
            tree.setTitle(cmsCategory.getName());
            trees.add(tree);
        }
        List<Tree<CmsCategoryDto>> topNodes = BuildTree.buildList(trees, "0"); //处理室树状结构
        Tree<CmsCategoryDto> root = new Tree<CmsCategoryDto>();
        if (topNodes.size() == 1) {
            root = topNodes.get(0);
        } else {
            root.setKey("-1");
            root.setId("-1");
            root.setParentId("0");
            root.setHasParent(false);
            root.setHasChildrenNode(true);
            root.setChildren(topNodes);
            root.setTitle("顶级节点");
        }
        if (Checker.BeNotEmpty(categoryIdList)) {
            Map<String, Object> attr = new HashMap<>(16);
            attr.put("checkerKeys", categoryIdList);
            root.setAttributes(attr);
        }
        return root;
    }

    @Override
    public void updateCategoryToHalfChecked(String categoryId) {
    	List<String> categoryIds =new ArrayList<>(16);
		recursionCategory(categoryId,categoryIds);
		List<OrgCategoryDto> orgCategoryDtos= listByField("category_id",categoryIds);
        if(Checker.BeNotEmpty(orgCategoryDtos)){
			orgCategoryDtos.forEach(orgCategoryDto->{
				orgCategoryDto.setHalfChecked(0);
			});
			updateByPks(orgCategoryDtos);
		}

    }

	private void recursionCategory(String categoryId,List<String> categoryIds){
    	if(Checker.BeNotBlank(categoryId)){
			CmsCategoryDto cmsCategoryDto = cmsCategoryService.getByPk(categoryId);
			if (Checker.BeNotNull(cmsCategoryDto) && !"0".equals(cmsCategoryDto.getParentId()) && categoryIds.size()<5) { //防止意外死循环
				categoryIds.add(cmsCategoryDto.getParentId());
				recursionCategory(cmsCategoryDto.getParentId(),categoryIds);
			}
		}
	}
}
