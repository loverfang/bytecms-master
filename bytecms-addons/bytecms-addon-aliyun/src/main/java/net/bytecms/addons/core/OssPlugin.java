package net.bytecms.addons.core;
import net.bytecms.addons.interceptor.AInterceptor;
import net.bytecms.addons.service.OssService;
import net.bytecms.addons.service.OssServiceImpl;
import net.bytecms.addons.service.PluginService;
import java.util.Map;

public class OssPlugin extends PluginService {

    @Override
    public Object execute(String api, String requestMethod, Map<String,Object> params) {
        try {
            OssService ossService=getService(OssServiceImpl.class,new AInterceptor());
            return executeMethod(ossService,api,requestMethod,params);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return null;
    }

}
