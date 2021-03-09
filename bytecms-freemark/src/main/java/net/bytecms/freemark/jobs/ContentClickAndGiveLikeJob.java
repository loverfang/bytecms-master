package net.bytecms.freemark.jobs;

import net.bytecms.core.api.BaseRedisService;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;
import net.bytecms.service.api.content.ContentService;
import net.bytecms.service.dto.content.ContentDto;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 定时更新文章点击次数和点赞次数
 */
@Component
public class ContentClickAndGiveLikeJob extends QuartzJobBean {

    @Autowired
    BaseRedisService baseRedisService;

    @Autowired
    ContentService contentService;


    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
       Set<String> clickKeys= baseRedisService.getKeysBlear(SecurityConstants.CONTENT_CLICK+"*");
       Set<String> giveLikeKeys= baseRedisService.getKeysBlear(SecurityConstants.GIVE_LIKES+"*");

       Set<String> fgiveLikeKeys= filterKeys(giveLikeKeys,SecurityConstants.GIVE_LIKES);
       Set<String> fclickKeys= filterKeys(clickKeys,SecurityConstants.CONTENT_CLICK);

       if(clickKeys!=null && ! clickKeys.isEmpty()){
          List<ContentDto> contentDtos= contentService.getByPks(fclickKeys);
          if(Checker.BeNotEmpty(contentDtos)){
              contentDtos.forEach(contentDto -> {
                  Integer clicks = (Integer) baseRedisService.get(SecurityConstants.CONTENT_CLICK+contentDto.getId());
                  contentDto.setClicks(clicks.intValue());
              });
              contentService.updateByPks(contentDtos);
              baseRedisService.removeByKeys(clickKeys);
          }
       }

        if(giveLikeKeys!=null && ! giveLikeKeys.isEmpty()){
            List<ContentDto> contentDtos= contentService.getByPks(fgiveLikeKeys);
            if(Checker.BeNotEmpty(contentDtos)){
                contentDtos.forEach(contentDto -> {
                    Integer giveLikes = (Integer) baseRedisService.get(SecurityConstants.GIVE_LIKES+contentDto.getId());
                    contentDto.setGiveLikes(giveLikes.intValue());
                });
                contentService.updateByPks(contentDtos);
                baseRedisService.removeByKeys(giveLikeKeys);
            }
        }
    }

    private Set<String> filterKeys(Set<String> keys,String prefix){
        Set<String> resKeys = new HashSet<>(16);
        if(keys!=null && !keys.isEmpty()){
            keys.forEach(key->{
                resKeys.add(key.replace(prefix,""));
            });
        }
        return resKeys;
    }
}
