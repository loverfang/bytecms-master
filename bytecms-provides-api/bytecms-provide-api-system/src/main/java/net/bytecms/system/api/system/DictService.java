package net.bytecms.system.api.system;
import net.bytecms.core.api.BaseService;
import net.bytecms.system.dto.system.DictDto;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author LG
 * @since 2019-08-29
 */
public interface DictService extends BaseService<DictDto> {

    /**
     * 分组查询类型
     * @param v
     * @return
     */

    List<DictDto> listType(DictDto v);

    /**
     * 根据类型获取数据
     * @param v
     * @return
     */
	List<DictDto> listByType(String type);
}
