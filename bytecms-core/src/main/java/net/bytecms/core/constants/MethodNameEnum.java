package net.bytecms.core.constants;

import lombok.Getter;

public enum MethodNameEnum {

	LBCMS_METHOD_PAGE("计算分页","_m_page"),

	LBCMS_FRAGMENT_IMPORT("页面片段导入","_m_fragment_import");

	@Getter
	private String name;
	@Getter
	private String value;

	MethodNameEnum(String name, String value) {
		this.name = name;
		this.value = value;
	}
}
