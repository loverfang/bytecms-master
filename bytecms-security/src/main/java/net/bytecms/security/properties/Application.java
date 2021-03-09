package net.bytecms.security.properties;

import lombok.Data;
import java.util.List;

@Data
public class Application {
    /**
     * 应用名称
     */
    public String appName;
    /**
     * 需要验证的地址
     */
    public List<String> authc;
    /**
     * 忽略的地址
     */
    public List<String> ignore;
}
