package net.bytecms.service.dto.navigation;

import net.bytecms.core.annotation.DirectMark;
import net.bytecms.core.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor
public class Navigation  extends BaseModel {

    @DirectMark
    private String name;


    @DirectMark
    private String url;



}
