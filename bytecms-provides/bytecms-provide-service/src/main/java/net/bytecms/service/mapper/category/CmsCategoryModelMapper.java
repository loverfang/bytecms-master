package net.bytecms.service.mapper.category;

import net.bytecms.service.entity.category.CmsCategoryModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
/**
 * <p>
 * 分类-模型 关系表 一个分类可以针对不同的模型投稿（本系统暂时只是一对一后期可扩展） Mapper 接口
 * </p>
 *
 * @author LG
 * @since 2019-11-06
 */
@Mapper
public interface CmsCategoryModelMapper extends BaseMapper<CmsCategoryModel> {

}
