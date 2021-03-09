package net.bytecms.service.dto.category;
import net.bytecms.core.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
/**
 * <p>
 * 分类-模型 关系表 一个分类可以针对不同的模型投稿（本系统暂时只是一对一后期可扩展）
 * </p>
 *
 * @author LG
 * @since 2019-11-06
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CmsCategoryModelDto extends BaseModel {

    private static final long serialVersionUID = 1L;

        /**
        * 分类ID
        */
        private String categoryId;


        /**
        * 模型编码
        */
        private String modelId;


        /**
        * 内容模板路径
        */
        private String templatePath;











}
