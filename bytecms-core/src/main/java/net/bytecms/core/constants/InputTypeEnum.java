package net.bytecms.core.constants;

import lombok.Getter;

public enum InputTypeEnum {
	TEXT("text","文本"),
	TEXTAREA("textarea","textarea文本"),
	NUMBER("number","数字"),
	DATE("date","日期"),
	FILE("file","文件"),
	PICTURE("picture","图片"),
	SELECT("select","下拉框"),
	EDITOR("editor","富文本编辑器"),
	BOOLEAN("boolean","switch是否"),
	TAGS("tags","标签"),
	;

	@Getter
	private String label;
	@Getter
	private String value;

	InputTypeEnum(String value,String label) {
		this.label = label;
		this.value = value;
	}
}
