package net.bytecms.service.service.plugin;

import net.bytecms.addons.manager.PluginManager;
import net.bytecms.addons.parser.XMLParser;
import net.bytecms.addons.service.PluginService;
import net.bytecms.core.utils.Checker;
import net.bytecms.service.dto.plugin.PluginInterModel;

public abstract class PluginInterpreter {

    public PluginService interpreter(PluginInterModel pluginInterModel)  {
        if(check(pluginInterModel)){
            String signaturer=pluginInterModel.getSignaturer();
            try {
                PluginManager pluginManager=new PluginManager(XMLParser.parsePlugins());
                PluginService pluginService=pluginManager.getInstanceBySignaturer(signaturer);
                return pluginService;
            }catch (Exception e){
                return null;
            }
        }
        return null;
    }

    private boolean check(PluginInterModel pluginInterModel){
        if(Checker.BeNull(pluginInterModel)){
            return false;
        }
        if(Checker.BeBlank(pluginInterModel.getSignaturer())){
            return false;
        }
        if(Checker.BeBlank(pluginInterModel.getApi())){
            return false;
        }
        if(Checker.BeBlank(pluginInterModel.requestMethod)){
            pluginInterModel.setRequestMethod("GET");
        }
        return true;
    }
}
