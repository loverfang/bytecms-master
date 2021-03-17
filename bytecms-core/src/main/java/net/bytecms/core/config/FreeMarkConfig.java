package net.bytecms.core.config;

import cn.hutool.core.io.FileUtil;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName: BeanConfig
 * @Author: LG
 * @Date: 2019/5/16 13:29
 * @Version: 1.0
 **/
@Slf4j
@Configuration
public class FreeMarkConfig {

    @Bean
    public freemarker.template.Configuration configuration(ByteCmsConfig byteCmsConfig) throws IOException, TemplateException {
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_28);
        File file = initDirectory(byteCmsConfig);
        configuration.setDirectoryForTemplateLoading(file);
        configuration.setObjectWrapper(new DefaultObjectWrapper(freemarker.template.Configuration.VERSION_2_3_28));
        configuration.setDefaultEncoding(Constants.DEFAULT_CHARSET_NAME); //这个一定要设置，不然在生成的页面中 会乱码
        configuration.setSetting("number_format","0.##");
        return configuration;
    }

    private File initDirectory(ByteCmsConfig byteCmsConfig)  {
        File root=null;
        String [] paths=new String[]{byteCmsConfig.getSourceRootPath(), byteCmsConfig.getSourceFragmentFilePath(),
        byteCmsConfig.getSourceTempPath(), byteCmsConfig.getSiteStaticFileRootPath(), byteCmsConfig.getLicensePath(),
        byteCmsConfig.getFileResourcePath()
        };
        int i=0;
        for(String path: paths){
            if(Checker.BeNotBlank(path)){
                File file =new File(path);
                if(!file.exists()){
                    file.mkdirs();
                }
                if(2==i){
                    root=file;
                }
            }
            i+=1;
        }
        try {
            fileMove(byteCmsConfig);
        }catch (IOException e){
            log.warn(e.getMessage());
        }
        return root;
    }

    private void fileMove(ByteCmsConfig byteCmsConfig) throws IOException {
        Resource resource = new ClassPathResource("license.dat");
        if(!resource.exists()){
            throw new IOException("授权文件不存在!");
        }
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
        } catch (IOException e) {
           throw new IOException("授权文件不存在!");
        }
        File file =new File(byteCmsConfig.getLicensePath()+ SecurityConstants.LICENSE_NAME);
        if(!FileUtil.exist(file)){
            FileUtil.writeFromStream(inputStream,file);
        }
    }
}
