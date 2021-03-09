package net.bytecms.addons.service;

import java.util.Map;

public abstract class PluginService extends BasePluginService {

     public abstract Object execute(String method, String requestMethod, Map<String,Object> params);

}
