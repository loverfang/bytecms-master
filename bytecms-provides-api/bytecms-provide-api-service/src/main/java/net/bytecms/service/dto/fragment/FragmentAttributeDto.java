package net.bytecms.service.dto.fragment;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;
/**
 * <p>
 * 
 * </p>
 *
 * @author LG
 * @since 2019-11-07
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FragmentAttributeDto extends BaseModel {

    private static final long serialVersionUID = 1L;

        /**
        * 主键
        */
        private String id;


        /**
        * 页面片段 数据id
        */
        private String fragmentId;



        /**
        * 数据
        */
        private String data;











}
