package net.bytecms.service.dto.fragment;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 页面片段文件模型
 * </p>
 *
 * @author LG
 * @since 2019-11-07
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FragmentDirectoryDto extends BaseModel {

    private static final long serialVersionUID = 1L;


        /**
        * 别名
        */
        @NotBlank(message = "名称不能为空")
        private String alias;

        /**
        * 路径
        */
        private String filePath ="";











}
