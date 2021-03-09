package net.bytecms.freemark.tools;

import net.bytecms.service.dto.category.CmsCategoryDto;
import net.bytecms.core.utils.Checker;

import java.util.HashMap;

public class CategoryPageHelp extends HashMap<String, Object> {

    public CategoryPageHelp(CmsCategoryDto category, int pageNo){
        this.buildPageHelp(category,pageNo);
    }

    public CategoryPageHelp(CmsCategoryDto category,int pageNo,int pageCount){
        this.buildPageHelp(category,pageNo,pageCount);
    }


    public void buildPageHelp(CmsCategoryDto category,Integer pageNo){
        if(Checker.BeNotNull(category)){
            this.put("categoryId",category.getId());
            this.put("categoryParentId",category.getParentId());
            this.put("pageSize",category.getPageSize());
        }
        this.put("pageNo",pageNo);
    }

    public void buildPageHelp(CmsCategoryDto category,Integer pageNo,Integer pageCount){
        if(Checker.BeNotNull(category)){
            this.put("categoryName",category.getName());
            this.put("categoryUrl",category.getContentPath());
            this.put("categoryId",category.getId());
            this.put("categoryParentId",category.getParentId());
            this.put("pageSize",category.getPageSize());
        }
        this.put("pageNo",pageNo);
        this.put("pageCount",pageCount);
    }
}
