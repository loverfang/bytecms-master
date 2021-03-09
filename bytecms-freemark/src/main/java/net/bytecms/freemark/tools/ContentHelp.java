package net.bytecms.freemark.tools;
import net.bytecms.core.constants.Constants;
import net.bytecms.service.dto.content.ContentDto;
import net.bytecms.core.utils.Checker;

import java.util.HashMap;

public class ContentHelp extends HashMap<String, Object> {

    public ContentHelp(ContentDto contentDto){
        this.buildContentHelp(contentDto);
    }


    public void buildContentHelp(ContentDto contentDto){
        if(Checker.BeNotNull(contentDto)){
            this.put(Constants.contentId,contentDto.getId());
            this.put(Constants.categoryId,contentDto.getCategoryId());
            this.put(Constants.hasRelated,contentDto.getHasRelated());
            this.put(Constants.hasTag,Checker.BeNotBlank(contentDto.getTagIds()));
            this.put(Constants.hasFiles,contentDto.getHasFiles());
            this.put(Constants.title, Checker.BeNotBlank(contentDto.getTitle()) ? contentDto.getTitle() : "");
            this.put(Constants.keywords, Checker.BeNotBlank(contentDto.getKeywords()) ? contentDto.getKeywords() : "");
            this.put(Constants.description, Checker.BeNotBlank(contentDto.getDescription()) ? contentDto.getDescription() : "");
        }
    }

}
