package net.bytecms.service.mapper.tags;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bytecms.service.dto.tags.CmsTagsDto;
import net.bytecms.service.entity.tags.CmsTags;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 标签 Mapper 接口
 * </p>
 *
 * @author LG
 * @since 2019-12-09
 */
@Mapper
public interface CmsTagsMapper extends BaseMapper<CmsTags> {

    String getTagsByContentId(@Param("contentId") String contentId);

    List<CmsTagsDto> listTags(@Param("maxRowNum") Integer maxRowNum);

    List<CmsTagsDto> getTagsByContent(@Param("contentId") String contentId);

    IPage<CmsTagsDto> listPage(IPage<CmsTagsDto> pages, @Param("dto") CmsTagsDto dto);
}
