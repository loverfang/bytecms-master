package net.bytecms.service.service.content;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.bytecms.service.api.content.ContentFileService;
import net.bytecms.service.api.resource.SysResourceService;
import net.bytecms.service.dto.content.ContentFileDto;
import net.bytecms.service.dto.resource.SysResourceDto;
import net.bytecms.service.entity.content.ContentFile;
import net.bytecms.service.mapper.content.ContentFileMapper;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.core.utils.Checker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 内容附件 服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-12-07
 */
@Service
public class ContentFileServiceImpl extends BaseServiceImpl<ContentFileDto, ContentFile, ContentFileMapper> implements ContentFileService {

    @Autowired
    SysResourceService resourceService;

    @Override
    @Transactional
    public void saveContentFiles(List<ContentFileDto> attachFiles,String contentId) {
        deleteByFiled("content_id",contentId);
        if(Checker.BeNotEmpty(attachFiles)){
            attachFiles.forEach(attachFile->{
                attachFile.setUserId(getUserId()).setContentId(contentId).setId(generateId());
            });
            insertBatch(attachFiles);
        }
    }

    @Override
    public List<SysResourceDto> getAttachByContnet(String content_id) {
        List<SysResourceDto> sysResourceDtos=new ArrayList<>(16);
        QueryWrapper<ContentFile> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("sys_resource_id").eq("content_id",content_id);
        List<ContentFile> contentFiles=baseMapper.selectList(queryWrapper);
        if(Checker.BeNotEmpty(contentFiles)){
            Set<String> resourceIds=new HashSet<>();
            contentFiles.forEach(contentFile->{
                if(Checker.BeNotBlank(contentFile.getSysResourceId())){
                    resourceIds.add(contentFile.getSysResourceId());
                }
            });
            if(Checker.BeNotEmpty(resourceIds)){
                sysResourceDtos=resourceService.getByPks(resourceIds);
            }
        }
        return sysResourceDtos;
    }
}
