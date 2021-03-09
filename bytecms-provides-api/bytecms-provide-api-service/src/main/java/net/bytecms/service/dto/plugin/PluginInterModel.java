package net.bytecms.service.dto.plugin;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class PluginInterModel {

    public String signaturer;

    public String api;

    public String requestMethod;

    public Map<String,Object> params=new LinkedHashMap<>();

}
