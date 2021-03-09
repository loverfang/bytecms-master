package net.bytecms.system.service.system;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import net.bytecms.core.api.BaseRedisService;
import net.bytecms.core.config.ThinkCmsConfig;
import net.bytecms.core.handler.CustomException;
import net.bytecms.core.model.LicenseProperties;
import net.bytecms.core.model.PageDto;
import net.bytecms.core.utils.ApiResult;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SecurityConstants;

import net.bytecms.system.api.system.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author dl
 * @since 2018-03-19
 */
@Service
public class LicenseServiceImpl implements LicenseService {

    @Autowired
    ThinkCmsConfig thinkCmsConfig;

    @Autowired
    BaseRedisService baseRedisService;

    @Override
    public PageDto<LicenseProperties> listPage(PageDto<LicenseProperties> roleDto) {
        List<LicenseProperties> propertiesList=new ArrayList<>();
        String path=thinkCmsConfig.getLicensePath()+ SecurityConstants.LICENSE_NAME;
        if(Checker.BeNotBlank(path)){
            Map<String,Object> params=new LinkedHashMap<>(16);
            boolean exist=FileUtil.exist(path);
            if(exist){
                List<String> properties= FileUtil.readUtf8Lines(path);
                if(Checker.BeNotEmpty(properties)){
                    properties.forEach(propertie->{
                        if(Checker.BeNotBlank(propertie)){
                            String[] kv=propertie.split(SecurityConstants.LICENSE_NAME_SPLIT);
                            params.put(kv[0],kv[1]);
                        }
                    });
                }
                if(!params.isEmpty()){
                    LicenseProperties license= JSON.parseObject(JSON.toJSONString(params), LicenseProperties.class);
                    if(Checker.BeNotNull(license)){
                        String[] startEnd= license.getStartStopTime().split("~");
                        if(startEnd.length>0){
                            Date now = DateUtil.date();
                            Date start = DateUtil.parse(startEnd[0]);
                            Date end = DateUtil.parse(startEnd[1]);
                            int isSatrt=DateUtil.compare(now,start);
                            int isEnd=DateUtil.compare(now,end);
                            long betweenDay = DateUtil.between(now, end, DateUnit.DAY);
                            if(isSatrt==-1){
                                license.setStatus("未生效");
                            }else if(isEnd==1){
                                license.setStatus("已过期");
                            }else{
                                license.setStatus("正常");
                            }
                            if(betweenDay<=20){
                                license.setStatus("即将过期");
                            }
                        }
                        propertiesList.add(license);
                    }

                }
            }
        }
        return new PageDto<>(1,propertiesList);
    }

    @Override
    public ApiResult importLicense(MultipartFile file) {
            if(Checker.BeNotNull(file)){
                checkFile(file);
                String basePath= thinkCmsConfig.getLicensePath()+SecurityConstants.LICENSE_NAME;
                byte[] bs = new byte[1024];
                InputStream inputStream = null;
                OutputStream os = null;
                int len;
                try {
                    inputStream = file.getInputStream();
                    String fileName = file.getOriginalFilename();
                    os = new FileOutputStream(basePath);
                    // 开始读取
                    while ((len = inputStream.read(bs)) != -1) {
                        os.write(bs, 0, len);
                    }
                } catch (IOException e) {
                    throw new CustomException(ApiResult.result(20026));
                }finally {
                    // 完毕，关闭所有链接
                    try {
                        os.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new CustomException(ApiResult.result(20026));
                    }
                }
                baseRedisService.removeByKeys(Arrays.asList(SecurityConstants.LOGIN_DATE_FRIST,SecurityConstants.LICENSE_NAME));
            }
            return ApiResult.result();
    }

    @Override
    public ApiResult search() {
        ApiResult apiResult=ApiResult.result();
        Object object=baseRedisService.get(SecurityConstants.LICENSE_NAME);
        if(object!=null){
            Map<String,Object> objectMap= ( Map<String,Object>)object;
            LicenseProperties properties=JSON.parseObject(JSON.toJSONString(objectMap), LicenseProperties.class);
            apiResult.put("authorizeDesc",properties.getAuthorizeDesc());
        }else{
            String path=thinkCmsConfig.getLicensePath()+SecurityConstants.LICENSE_NAME;
            File file =new File(path);
            if(file.exists()){
                Map<String,Object> params=licentsToMap(file);
                LicenseProperties properties=JSON.parseObject(JSON.toJSONString(params), LicenseProperties.class);
                if(Checker.BeBlank(properties.getSignaturer())){
                    apiResult.put(SecurityConstants.AUTHORIZEDESC,"未授权");
                }
                String sign = params.get(SecurityConstants.SIGNATURER).toString();
                params.remove(SecurityConstants.SIGNATURER);
                try {
                    //boolean valid = VerifyLicenseUtil.verify(params.toString().getBytes(), SecurityConstants.PUBLIC_KEY, sign);
                    boolean valid = false;
                    if(valid){
                        apiResult.put(SecurityConstants.AUTHORIZEDESC,properties.getAuthorizeDesc());
                    }else{
                        apiResult.put(SecurityConstants.AUTHORIZEDESC,"未授权");
                    }
                }catch (Exception e){
                    apiResult.put(SecurityConstants.AUTHORIZEDESC,"未授权");
                }
            }else{
                apiResult.put(SecurityConstants.AUTHORIZEDESC,"未授权");
            }
        }
        return apiResult;
    }

    private Map<String,Object> licentsToMap(File file){
        Map<String,Object> params=new LinkedHashMap<>(16);
        List<String> properties= FileUtil.readUtf8Lines(file);
        if(Checker.BeNotEmpty(properties)){
            properties.forEach(propertie->{
                if(Checker.BeNotBlank(propertie)){
                    String[] kv=propertie.split(SecurityConstants.LICENSE_NAME_SPLIT);
                    params.put(kv[0],kv[1]);
                }
            });
        }
        return params;
    }

    private void checkFile(MultipartFile file){
        String name = net.bytecms.core.utils.FileUtil.getSuffix(file.getOriginalFilename());
        if(!"dat".equals(name)){
            throw new CustomException(ApiResult.result(20033));
        }
    }
}
