package net.bytecms.service.service.category;
import net.bytecms.service.api.category.CmsCategoryModelService;
import net.bytecms.service.dto.category.CmsCategoryModelDto;
import net.bytecms.service.entity.category.CmsCategoryModel;
import net.bytecms.service.mapper.category.CmsCategoryModelMapper;
import net.bytecms.core.service.BaseServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分类-模型 关系表 一个分类可以针对不同的模型投稿（本系统暂时只是一对一后期可扩展） 服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-11-06
 */
@Transactional
@Service
public class CmsCategoryModelServiceImpl extends BaseServiceImpl<CmsCategoryModelDto, CmsCategoryModel, CmsCategoryModelMapper> implements CmsCategoryModelService {



}
