package net.bytecms.service.service.plugin;
import net.bytecms.core.utils.Checker;
import net.bytecms.service.api.plugin.PluginService;
import net.bytecms.service.dto.plugin.PluginInterModel;
import org.springframework.stereotype.Service;

@Service
public class PluginServiceImpl extends PluginInterpreter implements PluginService {

    @Override
    public void doIt(PluginInterModel pluginInter) {
        net.bytecms.addons.service.PluginService interpreter=interpreter(pluginInter);
        if(Checker.BeNotNull(interpreter)){
            interpreter.execute(pluginInter.getApi(),pluginInter.getRequestMethod(),pluginInter.getParams());
        }
    }

}
