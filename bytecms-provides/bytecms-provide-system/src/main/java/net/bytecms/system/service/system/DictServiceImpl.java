package net.bytecms.system.service.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.bytecms.system.api.system.DictService;
import net.bytecms.system.dto.system.DictDto;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.system.entity.system.Dict;
import net.bytecms.system.mapper.system.DictMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-08-29
 */
@Service
public class DictServiceImpl extends BaseServiceImpl<DictDto, Dict, DictMapper> implements DictService {


    @Override
    public List<DictDto> listType(DictDto v) {
        QueryWrapper<Dict> queryWrapper=new QueryWrapper<>();
        queryWrapper.groupBy("type").select("type");
        return ResultT2D(list(queryWrapper));
    }

	@Override
	public List<DictDto> listByType(String type) {
		  QueryWrapper<Dict> queryWrapper=new QueryWrapper<>();
	        queryWrapper.eq("type", type).notIn("parent_id", 0);
		return ResultT2D(this.baseMapper.selectList(queryWrapper));
	}
}
