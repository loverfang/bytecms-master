package net.bytecms.service.service.fragment;

import com.alibaba.fastjson.JSON;
import net.bytecms.core.annotation.CacheClear;
import net.bytecms.core.annotation.FieldDefault;
import net.bytecms.core.config.ThinkCmsConfig;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.handler.CustomException;
import net.bytecms.core.service.BaseServiceImpl;
import net.bytecms.service.api.fragment.FragmentAttributeService;
import net.bytecms.service.api.fragment.FragmentFileModelService;
import net.bytecms.service.api.fragment.FragmentService;
import net.bytecms.service.dto.fragment.FragmentDirectoryDto;
import net.bytecms.service.dto.fragment.FragmentDto;
import net.bytecms.service.dto.fragment.FragmentFileModelDto;
import net.bytecms.service.dto.model.CmsDefaultModelFieldDto;
import net.bytecms.service.entity.fragment.Fragment;
import net.bytecms.service.entity.fragment.FragmentFileModel;
import net.bytecms.service.mapper.fragment.FragmentFileModelMapper;
import net.bytecms.service.utils.DynamicFieldUtil;
import net.bytecms.core.utils.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * <p>
 * 页面片段文件模型 服务实现类
 * </p>
 *
 * @author LG
 * @since 2019-11-07
 */
@Transactional
@Service
public class FragmentFileModelServiceImpl extends BaseServiceImpl<FragmentFileModelDto, FragmentFileModel, FragmentFileModelMapper> implements FragmentFileModelService {

    @Autowired
    ThinkCmsConfig thinkCmsConfig;

    @Autowired
    FragmentAttributeService fragmentAttributeService;

    @Autowired
    FragmentService fragmentService;

    @Override
    public TreeFileInfo treeFragmentFiles() {
        TreeFileInfo treeFileInfo =new TreeFileInfo();
        treeFileInfo.setKey("-1").setTitle("根目录");
        Map<String,String> map= aliasMap();
        recursionTree(thinkCmsConfig.getSourceFragmentFilePath(),treeFileInfo,map);
        return  treeFileInfo;
    }

    private Map<String,String> aliasMap(){
        Map<String,String> map =new HashMap<>();
        List<FragmentFileModel> fragmentFileModels = super.list();
        if(Checker.BeNotEmpty(fragmentFileModels)){
            fragmentFileModels.forEach(fragmentFileModel->{
                map.put(fragmentFileModel.getId(),fragmentFileModel.getAlias());
                map.put(fragmentFileModel.getId()+"_code",fragmentFileModel.getCode());
            });
        }
        return map;
    }

    private  TreeFileInfo recursionTree(String path,TreeFileInfo treeFileInfoDto,Map<String,String> map){
        List<TreeFileInfo> treefiles= buildData(FileUtil.getFileList(path,null),map);
        if(Checker.BeNotEmpty(treefiles)){
            treeFileInfoDto.setChildren(treefiles);
            for (TreeFileInfo treeFileInfo : treeFileInfoDto.getChildren()) {
                String relativePath = path.replace(thinkCmsConfig.getSourceFragmentFilePath(),Constants.BLANK) + File.separator+ treeFileInfo.getFileInfo().getFileName();
                treeFileInfo.setRelativePath(relativePath);
                if (treeFileInfo.getFileInfo().isDirectory()) {
                    String filePath = path + File.separator+ treeFileInfo.getFileInfo().getFileName();
                    recursionTree(filePath,treeFileInfo, map);
                }
            }
        }
        return treeFileInfoDto;
    }

    private List<TreeFileInfo> buildData(List<FileInfo> files, Map<String,String> map){
        List<TreeFileInfo> treeFileInfos =new ArrayList<>();
        if(Checker.BeNotEmpty(files)){
            files.forEach(file->{
                TreeFileInfo treeFileInfo =new TreeFileInfo();
                treeFileInfo.setFileInfo(file);
                treeFileInfo.setIsLeaf(!file.isDirectory());
                Integer key=file.hashCode();
                if(file.isDirectory()){
                    treeFileInfo.setTitle(file.getFileName()).setKey(String.valueOf(key));;
                }else{
                    int index=file.getFileName().lastIndexOf(Constants.DOT);
                    String fileName = file.getFileName().substring(0,index);
                    treeFileInfo.setTitle(map.get(fileName)).setCode(map.get(fileName+"_code")).setKey(fileName);
                }
                treeFileInfos.add(treeFileInfo);
            });
        }
        return treeFileInfos;
    }


    @Override
    public ApiResult getFileContent(String path) {
        String filePath = thinkCmsConfig.getSourceFragmentFilePath()+File.separator+path;
        File file = new File(filePath);
        try {
            if (file.isFile()) {
                String fileContent= FileUtils.readFileToString(file, Constants.DEFAULT_CHARSET_NAME);
                return ApiResult.result(fileContent);
            }
        } catch (IOException e) {
            log.error("读取模板文件失败！");
            return null;
        }
        return null;
    }

    @Override
    public ApiResult setFileContent(String path, String content) {
        File file= checkFileIsExist(path);
        try {
            content=  URLDecoder.decode(content,Constants.DEFAULT_CHARSET_NAME);
            FileUtils.writeStringToFile(file,content, Constants.DEFAULT_CHARSET_NAME);
        } catch (IOException e) {
            log.error("写入模板文件失败！");
            return ApiResult.result(20001);
        }
        return ApiResult.result();
    }

    @Transactional
    @Override
    @CacheClear(key = "getFragmentFilePathByCode")
    public ApiResult deleteFile(String filePath) {
        filePath = thinkCmsConfig.getSourceFragmentFilePath()+File.separator+filePath;
        File file = new File(filePath);
        boolean res =true;
        if(file.exists()){
            if(file.isFile()){
                res=file.delete();
                if(res){
                    deleteFileData(file);
                }
            }else {
                recursionDeleteFile(file);
            }
        }
        if(res){
            return ApiResult.result();
        }else{
            return ApiResult.result(20000);
        }
    }

    private void deleteFileData(File file){
        if(Checker.BeNotNull(file)){
           String fileName= file.getName();
           if(Checker.BeNotBlank(fileName) && fileName.contains(Constants.DOT)){
               String fileId = fileName.substring(0,fileName.lastIndexOf(Constants.DOT));
               Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
               boolean isNum=pattern.matcher(fileId).matches();
               if(Checker.BeNotBlank(fileId)&& isNum ){
                   List<FragmentDto> fragments=fragmentService.listByField("fragment_file_model_id",fileId);
                   if(Checker.BeNotEmpty(fragments)){
                       Set<String> fragmentids = new HashSet<>();
                       fragments.forEach(fragment->{
                           fragmentids.add(fragment.getId());
                       });
                       fragmentAttributeService.deleteByFiled("fragment_id",fragmentids);
                   }
                   fragmentService.deleteByFiled("fragment_file_model_id",fileId);
                   deleteByPk(fileId);
               }
           }
        }

    }

    @Transactional
    @Override
    @CacheClear(keys = {"getFragmentFilePathByCode"})
    public void saveFragmentFile(FragmentFileModelDto v)  {
        checkerCodeIsExist(v.getCode(),"");
        if(Checker.BeNull(v.getFilePath())){
            v.setFilePath("");
        }
        String id=generateId();
        String fileName = id+Constants.DEFAULT_HTML_SUFFIX;
        File file = new File(thinkCmsConfig.getSourceFragmentFilePath()+File.separator+v.getFilePath()+File.separator+fileName);
        try {
            boolean res=file.createNewFile();
            if(res){ //文件创建成功保存数据库
                if(Checker.BeNotBlank(v.getFilePath())&&v.getFilePath().startsWith("\\")){
                    v.setFilePath(v.getFilePath().substring(1));
                }
                FragmentFileModelDto fileModelDto =new FragmentFileModelDto();
                fileModelDto.setId(id).setAlias(v.getAlias()).setSize(v.getSize()).setCode(v.getCode())
                .setFilePath(v.getFilePath()).setFileName(v.getFilePath()+File.separator+fileName);
                insert(fileModelDto);
            }
        }catch (Exception e){
            file.delete();
            log.error(e.getMessage());
            throw new CustomException(ApiResult.result(7000,"创建文件失败"));
        }
    }

    private void checkerCodeIsExist(String code,String id){
        FragmentFileModelDto fileModelDto=getByField("code",code);
        if(Checker.BeNotBlank(id)){ //b=编辑
            if(Checker.BeNotNull(fileModelDto) && !id.equals(fileModelDto.getId())){
                throw new CustomException(ApiResult.result(20012));
            }
        }else{// 新增
            if(Checker.BeNotNull(fileModelDto)){
                throw new CustomException(ApiResult.result(20012));
            }
        }

    }

    @Override
    public void saveFragmentDirectory(FragmentDirectoryDto v) {
        if(Checker.BeNull(v.getFilePath())){
            v.setFilePath("");
        }
        File file = new File(thinkCmsConfig.getSourceFragmentFilePath()+File.separator+v.getFilePath()+File.separator+v.getAlias());
        file.mkdir();
    }

    @Override
    public FragmentFileModelDto getInfoByPk(String id) {
        FragmentFileModelDto fileModelDto =getByPk(id);
        if(Checker.BeBlank(fileModelDto.getDefaultFieldList())){
            fileModelDto.setModelFieldDtos(listDefaultField());
        }else{
            List<CmsDefaultModelFieldDto> defaultModelFieldDtoList = JSON.parseArray(fileModelDto.getDefaultFieldList(),CmsDefaultModelFieldDto.class);
            fileModelDto.setModelFieldDtos(defaultModelFieldDtoList);
        }
        return fileModelDto;
    }

    @Override
    public List<CmsDefaultModelFieldDto> listDefaultField() {
        List<CmsDefaultModelFieldDto> cmsDefaultModelFieldDtos=new ArrayList<>();
        Field[] fields = Fragment.class.getDeclaredFields();
        for (Field f : fields) {
            boolean hasAnnotation = f.isAnnotationPresent(FieldDefault.class);
            if (hasAnnotation) {
                FieldDefault annotation = f.getAnnotation(FieldDefault.class);
                CmsDefaultModelFieldDto mfDto=new CmsDefaultModelFieldDto();
                mfDto.setVisibleCheck(annotation.visibleCheck())
                .setVisibleSwitch(annotation.visibleSwitch())
                .setInitCheck(annotation.initCheck())
                .setInitSwitch(annotation.initSwitch())
                .setFieldNote(annotation.fieldNote())
                .setReFieldNote(annotation.fieldNote())
                .setFieldType(f.getType().getTypeName().replace("java.lang.",""))
                .setInputType(annotation.inputType().getValue())
                .setFieldName(f.getName());
                cmsDefaultModelFieldDtos.add(mfDto);
            }
        }
        return cmsDefaultModelFieldDtos;
    }

    @CacheClear(keys = {"getFragmentFilePathByCode"})
    @Override
    public void updateFragmentDesignFile(FragmentFileModelDto v) {
        checkerCodeIsExist(v.getCode(),v.getId());
        if(Checker.BeBlank(v.getFilePath()))v.setFilePath(null);
        List<CmsDefaultModelFieldDto> cmsDefaultModelFields= v.getModelFieldDtos();
        String ckDefaultField= v.getDefaultFieldList();
        List<CmsDefaultModelFieldDto> cmsCheckedModelFields =new ArrayList<>();
        List<String> requireFied= new ArrayList<>();
        if(Checker.BeNotEmpty(cmsDefaultModelFields)){
            Map<String,String>  fieldTextMap =new HashMap<>(16);
            cmsDefaultModelFields.forEach(defaultModel->{
                if(defaultModel.getInitCheck()){
                    if(defaultModel.getInitSwitch()){
                        requireFied.add(defaultModel.getFieldName());
                    }
                    fieldTextMap.put(defaultModel.getFieldName(),defaultModel.getReFieldNote());
                    cmsCheckedModelFields.add(defaultModel);
                }
            });
            if(Checker.BeNotEmpty(requireFied)){
                v.setRequiredFieldList(JSON.toJSONString(requireFied));
            }
            if(!fieldTextMap.isEmpty()){
                v.setFieldTextMap(JSON.toJSONString(fieldTextMap));
            }
            if(Checker.BeNotEmpty(cmsCheckedModelFields)){
                v.setCheckedFieldList(JSON.toJSONString(cmsCheckedModelFields));
            }
            v.setDefaultFieldList(JSON.toJSONString(cmsDefaultModelFields));
        }
        super.updateByPk(v);

    }

    @Override
    public FragmentFileModelDto getFragmentDesignByPk(String id) {
        FragmentFileModelDto fragmentFileModelDto =getByPk(id);
        List<CmsDefaultModelFieldDto> allFields= DynamicFieldUtil.formaterField(fragmentFileModelDto.getCheckedFieldList(),fragmentFileModelDto.getExtendFieldList());
        if(Checker.BeNotEmpty(allFields)){
            fragmentFileModelDto.setModelFieldDtos(allFields);
        }
        return fragmentFileModelDto;
    }

    @Cacheable(value = Constants.cacheName, key = "#root.targetClass+'.'+#root.methodName+'.'+#p0")
    @Override
    public String getFragmentFilePathByCode(String code) {
         String fragmentPath= baseMapper.getFragmentFilePathByCode(code);
         if(Checker.BeNotBlank(fragmentPath)){
             fragmentPath = thinkCmsConfig.getSourceFragmentFilePath().replace(thinkCmsConfig.getSourceTempPath(),"")+fragmentPath;
         }
         return fragmentPath;
    }

    @Override
    public void downZip(String path,HttpServletResponse response) {
        String finalPath = thinkCmsConfig.getSourceFragmentFilePath();
        if(Checker.BeNotBlank(path)){
            finalPath=finalPath+File.separator+path;
        }
        File file = new File(finalPath);
        if(!file.exists()||file.isFile()){
            throw  new CustomException(ApiResult.result(20015));
        }
        String timeStr= DateTimeFormatter.ofPattern(Constants.YMDHM).format(LocalDateTime.now());
        String fileId= thinkCmsConfig.getSourceRootPath()+File.separator+timeStr+Constants.DEFAULT_ZIP_SUFFIX;
        File filePath=null;
        InputStream in = null;
        OutputStream out = null;
        try {
            ZipUtil.zip(finalPath,fileId);
            filePath = new File(fileId);
                if(filePath.exists()){
                    response.setCharacterEncoding(Constants.DEFAULT_CHARSET_NAME);
                    response.setHeader("content-disposition","attachment;fileName=fragment_"+timeStr+Constants.DEFAULT_ZIP_SUFFIX);
                    response.setHeader("FileName", "fragment_"+timeStr+Constants.DEFAULT_ZIP_SUFFIX);
                    response.setHeader("Access-Control-Expose-Headers", "FileName");
                    out=  response.getOutputStream();
                    in = new FileInputStream(filePath);
                    byte[] buffer  = new byte[1024];
                    int i=in.read(buffer );
                    while (i!=-1){
                        out.write(buffer , 0, i);//将缓冲区的数据输出到浏览器
                        i= in.read(buffer );
                    }
                }
        } catch (Exception e) {
           log.error(e.getMessage());
           throw  new CustomException(ApiResult.result(20015));
        }finally {
            if(Checker.BeNotNull(filePath) && Checker.BeNotNull(in)){
                try {
                    in.close();
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                filePath.delete();
            }
        }
    }

    private void recursionDeleteFile(File file){
        if(file.isFile()){
            if(file.delete()){
                deleteFileData(file);
            };
        }else{
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()){
                    recursionDeleteFile(f);
                }else {
                    if(f.delete()){
                        deleteFileData(file);
                    };
                }
            }
            if(file.delete()){
                deleteFileData(file);
            };
        }

    }

    private File checkFileIsExist(String path){
        String filePath = thinkCmsConfig.getSourceFragmentFilePath()+File.separator+path;
        File file = new File(filePath);
        if(!(file.exists()&& file.isFile())){
            throw  new CustomException(ApiResult.result(20000));
        }
        return file;
    }
}
