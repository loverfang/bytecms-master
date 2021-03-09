package net.bytecms.service.service.content;
import net.bytecms.service.entity.content.ContentAttribute;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.service.api.content.ContentAttributeService;
import net.bytecms.service.dto.content.ContentAttributeDto;
import net.bytecms.service.mapper.content.ContentAttributeMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 内容扩展 服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-10-30
 */
@Transactional
@Service
public class ContentAttributeServiceImpl extends BaseServiceImpl<ContentAttributeDto, ContentAttribute, ContentAttributeMapper> implements ContentAttributeService {



}
