package net.bytecms.core.handler;
import net.bytecms.core.utils.ApiResult;
import lombok.Data;

@Data
public class CustomException extends RuntimeException  {

	public ApiResult r;

	public CustomException(ApiResult r) {
		super(r.getMessage());
		this.r = r;
	}

}
