package net.bytecms.service.api.fragment;
import net.bytecms.core.api.BaseService;
import net.bytecms.service.dto.fragment.FragmentDto;
import net.bytecms.core.utils.ApiResult;

import java.util.List;

/**
 * <p>
 * 页面片段数据表 服务类
 * </p>
 *
 * @author LG
 * @since 2019-11-07
 */
public interface FragmentService extends BaseService<FragmentDto> {


    /**
     * 保存页面片段
     * @param v
     */
    void saveFragment(FragmentDto v);


    /**
     * 删除页面片段文件和数据
     * @param pk
     * @return
     */
    boolean deleteFragmentByPk(String pk);

    /**
     * 根据 code 获取数据
     * @param code
     */
    List<FragmentDto> getFragmentDataByCode(String code);


    /**
     * 编辑页面片段数据，用于还原
     * @param id
     * @return
     */
    ApiResult getInfoByPk(String id);

    /**
     * 更新片段数据
     * @param v
     */
    void updateFragmentByPk(FragmentDto v);
}