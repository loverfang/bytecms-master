package net.bytecms.addons.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Plugin {

    public String name;

    public String version;

    public String author;

    public String jar;

    public String className;

    public String signaturer;

}
