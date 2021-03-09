package net.bytecms.core.constants;

import lombok.Getter;

public enum DirectiveNameEnum {

	CMS_CONTENT_DIRECTIVE("_d_content","content","获取文章详情","com.thinkcms.service.dto.content.ContentDto"),

	CMS_TOP_TAG_DIRECTIVE("_d_top_tag","contents","根据置顶标识获取文章"),

	CMS_ONE_CONTENT_DIRECTIVE("_d_single_page","content","获取单页内容"),

	CMS_CATEGORY_LIST_DIRECTIVE("_d_content_list","contents","获取分类分页数据"),

	CMS_CATEGORY_TREE_DIRECTIVE("_d_category_tree","categorys","导航栏树"),

	CMS_CATEGORY_INFO_DIRECTIVE("_d_category_info","category","获取分类详情[ID]"),

	CMS_CATEGORY_BY_CODE_DIRECTIVE("_d_category_by_code","categorys","获取分类详情[codes]"),

	CMS_CHILD_CATEGORY_DIRECTIVE("_d_child_category","childCategory","获取子分类列表[categoryId]"),

	CMS_CATEGORY_CONTENT_FOR_PAGE_HOME_DIRECTIVE("_d_content_by_category","contentOfcategory","获取分类内容[codes]"),

	CMS_CATEGORY_CONTENG_BYCODE("_d_category_content","contents","获取分类内容[code]"),

	CMS_ATTACH_DIRECTIVE("_d_attach","attachs","获取内容附件[contentId]"),

	CMS_CONTENT_TAGS_DIRECTIVE("_d_content_tags","tags","获取内容标签[contentId]"),

	CMS_TAGS_DIRECTIVE("_d_tags","tags","获取标签云"),

	CMS_SITE_DIRECTIVE("_d_site","site","获取网站配置"),

	CMS_CONTENT_RELATED_DIRECTIVE("_d_related","relateds","获取相关推荐[contentId]"),

	CMS_CONTENT_NEXT_DIRECTIVE("_d_content_next","next","获取下一篇文章[contentId]"),

	CMS_CONTENT_PREVIOUS_DIRECTIVE("_d_content_pre","previous","获取上一篇文章[contentId]"),

	CMS_CLICKS_TOP_DIRECTIVE("_d_clicks_top","tops","获取热门点击[maxRowNum]"),

	CMS_RECOMM_DIRECTIVE("_d_recomms","recomms","获取置顶荐[rowNum,categoryId]"),

	CMS_HOT_DIRECTIVE("_d_hots","hots","获取热门内容"),

	CMS_NAVIGATION("_d_navigation","navigations","获取热门内容"),

	CMS_NOTICE_DIRECTIVE("_d_notices","notices","获取公告内容"),

	CMS_UPTODATE_DIRECTIVE("_d_uptodates","contents","获取最新内容"),

	CMS_FRAGMENT_DATA_DIRECTIVE("_d_fragment_data","fragmentData","获取页面片段数据[code]"),

	CMS_PARENT_CATEGORY_DIRECTIVE("_d_parent_category","parentCategorys","获取父分类列表[categoryId]");


	@Getter
	private String value;

	@Getter
	private String code;

	@Getter
	private String name;

	@Getter
	private String sourceTarget;


	DirectiveNameEnum(String value,String code,String name) {
		this.name = name;
		this.value = value;
		this.code = code;
	}

	DirectiveNameEnum(String value,String code,String name,String sourceTarget) {
		this.name = name;
		this.value = value;
		this.code = code;
		this.sourceTarget=sourceTarget;
	}
}
