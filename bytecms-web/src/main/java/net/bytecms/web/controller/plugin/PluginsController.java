package net.bytecms.web.controller.plugin;

import net.bytecms.service.api.plugin.PluginService;
import net.bytecms.service.dto.plugin.PluginInterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("plugins")
public class PluginsController {

    @Autowired
    PluginService pluginService;

    @RequestMapping("index")
    public void index(@RequestBody PluginInterModel pluginInterModel){
        pluginService.doIt(pluginInterModel);
    }

}
