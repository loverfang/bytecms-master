package net.bytecms.service.api.category;
import net.bytecms.service.dto.category.CmsCategoryDto;
import net.bytecms.core.api.BaseService;
import net.bytecms.core.utils.Tree;
import net.bytecms.service.dto.navigation.Navigation;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分类 服务类
 * </p>
 *
 * @author LG
 * @since 2019-11-04
 */
public interface CmsCategoryService extends BaseService<CmsCategoryDto> {


    /**
     * 查询分类树
     * @return
     */
    Tree<CmsCategoryDto> selectTreeCategory(boolean needAuth);

    /**
     * 保存 分类
     * @param v
     */
    void save(CmsCategoryDto v);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CmsCategoryDto getInfoById(String id);

    /**
     * 编辑
     * @param v
     */
    void update(CmsCategoryDto v);



    /**
     * 重新静态化分类列表页
     * @param v
     */
    void reStaticFileByCid(String id,List<String> status);

    /**
     * 查询首页发布分类用于前台展示
     * categoryId: 当前分类 id
     * @return
     */
    Tree<CmsCategoryDto> selectHomePageCategory(String categoryId);

    /**
     * 根据父分类id查询子分类
     * @param categoryId
     * @return
     */
    Map<String,Object> selectChildCategorys(String categoryId);


    /**
     * 删除分类
     * @param pk
     * @return
     */
    boolean deleteByCategoryId(String pk);

    /**
     * 根据分类code 获取分类详情
     * @param codes
     * @return
     */
    List<CmsCategoryDto> selectCategoryByCodes(String[] codes);


    /**
     * 查询所有需要生成的分类
     * @return
     */
    List<CmsCategoryDto> selectCategoryForWholeSiteGen();


    /**
     * 更新当前栏目下的所有内容为当前所属内容的模型
     * @param id
     */
    void updateContentModel(String id);

    /**
     * 查询org
     * @param pid
     * @param orgId
     * @return
     */
    List<CmsCategoryDto> listCategoryByPidAndOrgId(String pid, String orgId);

    /**
     * 获取栏目的详细信息
     * @param categoryId
     * @return
     */
    CmsCategoryDto getCategoryInfoByPk(String categoryId,String categoryCode);

    /**
     * 面包屑导航
     * @param contentId
     * @return
     */
    List<Navigation> getNavigation(String contentId);

    /**
     * 查询父分类以及当前分类的面包屑导航
     * @param id
     * @return
     */
    List<CmsCategoryDto> selectParentCategorys(String id);
}