package net.bytecms.service.dto.webfile;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TemplateDto {

    @NotBlank(message = "文件名称不能为空")
    private String fileName;

    @NotBlank(message = "文件路径不能为空")
    private String filePath;

    private Boolean isDirectory ;
}
