package net.bytecms.addons.manager;

import net.bytecms.addons.model.Plugin;
import net.bytecms.addons.parser.XMLParser;
import net.bytecms.addons.service.PluginService;
import org.dom4j.DocumentException;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
public class PluginManager {

    private URLClassLoader urlClassLoader;

    public PluginManager(List<Plugin> plugins) throws MalformedURLException {
        init(plugins);
    }

    private void init(List<Plugin> plugins) throws MalformedURLException {
        int size = plugins.size();
        URL[] urls = new URL[size];
        for(int i = 0; i < size; i++) {
            Plugin plugin = plugins.get(i);
            String filePath = plugin.getJar();
            urls[i] = new URL("file:"+filePath);
        }
        urlClassLoader = new URLClassLoader(urls);
    }

    /**
     * 获得插件
     * @param className
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public PluginService getInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        // 插件实例化对象，插件都是实现PluginService接口
        Class<?> clazz = urlClassLoader.loadClass(className);
        Object instance = clazz.newInstance();
        return (PluginService)instance;
    }


    /**
     * 获得插件
     * @param signaturer
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public PluginService getInstanceBySignaturer(String signaturer) throws ClassNotFoundException, InstantiationException, IllegalAccessException, DocumentException, FileNotFoundException {
        // 插件实例化对象，插件都是实现PluginService接口
        Class<?> clazz=null;
        List<Plugin> plugins= XMLParser.parsePlugins();
        if(plugins!=null&&!plugins.isEmpty()){
            for(Plugin plugin:plugins){
                if(signaturer.equals(plugin.getSignaturer())){
                    clazz = urlClassLoader.loadClass(plugin.getClassName());
                }
            }
        }
        if(clazz!=null){
            Object instance = clazz.newInstance();
            return (PluginService)instance;
        }else{
            return null;
        }
    }

}
