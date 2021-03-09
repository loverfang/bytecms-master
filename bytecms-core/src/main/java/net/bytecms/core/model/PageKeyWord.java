package net.bytecms.core.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PageKeyWord {

    private String title;

    private String keywords;

    private String description;
}
