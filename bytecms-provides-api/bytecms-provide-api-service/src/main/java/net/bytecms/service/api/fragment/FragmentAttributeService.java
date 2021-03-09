package net.bytecms.service.api.fragment;
import net.bytecms.service.dto.fragment.FragmentAttributeDto;
import net.bytecms.core.api.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LG
 * @since 2019-11-07
 */
public interface FragmentAttributeService extends BaseService<FragmentAttributeDto> {


    boolean deleteByFragmentId(String pk);
}