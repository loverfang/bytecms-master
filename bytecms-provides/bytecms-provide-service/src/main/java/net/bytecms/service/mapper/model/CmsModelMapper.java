package net.bytecms.service.mapper.model;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bytecms.service.dto.model.CmsModelDto;
import net.bytecms.service.entity.model.CmsModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LG
 * @since 2019-10-23
 */
@Mapper
public interface CmsModelMapper extends BaseMapper<CmsModel> {

    List<CmsModelDto> listModelByCategory(@Param("dto") CmsModelDto v);
}
