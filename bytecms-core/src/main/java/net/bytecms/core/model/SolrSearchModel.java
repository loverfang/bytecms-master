package net.bytecms.core.model;
import lombok.Data;

@Data
public class SolrSearchModel extends BaseModel{

    private String keyWords;

    private String author;

    private String categoryId;

    private String content;

    private String cover;

    private String description;

    private String editor;

    private String publishDate;

}
