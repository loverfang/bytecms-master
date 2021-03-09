package net.bytecms.core.constants;
import lombok.Getter;

public enum SolrCoreEnum {
	DEFAULT_CORE("default_core","默认库")
	;

	@Getter
	private String value;


	@Getter
	private String label;

	SolrCoreEnum(String value, String label) {
		this.label = label;
		this.value = value;
	}
}
