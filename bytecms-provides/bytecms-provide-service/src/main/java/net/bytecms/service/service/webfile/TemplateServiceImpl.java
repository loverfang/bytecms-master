package net.bytecms.service.service.webfile;

import net.bytecms.core.config.ByteCmsConfig;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.constants.DirectiveNameEnum;
import net.bytecms.core.handler.CustomException;
import net.bytecms.freemark.tools.XMLParser;
import net.bytecms.service.api.webfile.TemplateService;
import net.bytecms.service.dto.webfile.TemplateDto;
import lombok.extern.slf4j.Slf4j;
import net.bytecms.core.utils.*;
import org.apache.commons.io.FileUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    ByteCmsConfig byteCmsConfig;

    @Override
    public TreeFileInfo treeTempFile() {
        TreeFileInfo treeFileInfo = new TreeFileInfo();
        treeFileInfo.setKey("-1").setTitle("根目录");
        recursionTree(byteCmsConfig.getSourceTempPath(), treeFileInfo);
        return treeFileInfo;
    }

    @Override
    public ApiResult getFileContent(String path) {
        String filePath = byteCmsConfig.getSourceTempPath() + File.separator + path;
        File file = new File(filePath);
        try {
            if (file.isFile()) {
                String fileContent = FileUtils.readFileToString(file, Constants.DEFAULT_CHARSET_NAME);
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
        File file = checkFileIsExist(path);
        try {
            content = URLDecoder.decode(content, Constants.DEFAULT_CHARSET_NAME);
            FileUtils.writeStringToFile(file, content, Constants.DEFAULT_CHARSET_NAME);
        } catch (IOException e) {
            log.error("写入模板文件失败！");
            return ApiResult.result(20001);
        }
        return ApiResult.result();
    }

    @Override
    public ApiResult deleteFile(String filePath) {
        filePath = byteCmsConfig.getSourceTempPath() + File.separator + filePath;
        File file = new File(filePath);
        boolean res = true;
        if (file.exists()) {
            if (file.isFile()) {
                res = file.delete();
            } else {
                recursionDeleteFile(file);
            }
        }
        if (res) {
            return ApiResult.result();
        } else {
            return ApiResult.result(20000);
        }
    }

    @Override
    public void saveTemp(TemplateDto templateDto) {
        if(Checker.BeNull(templateDto.getFilePath())){
            templateDto.setFilePath("");
        }
        if("\\".equals(templateDto.getFilePath())){
            templateDto.setFilePath("");
        }
        String fileName = templateDto.getIsDirectory()?templateDto.getFileName():templateDto.getFileName()+Constants.DEFAULT_HTML_SUFFIX;
        String filePath= byteCmsConfig.getSourceTempPath()+templateDto.getFilePath()+File.separator+fileName;
        File file = new File(filePath.replace("\\","/"));
        checkerFileIsExist(file);
        try {
            if(templateDto.getIsDirectory()){
                file.mkdir();
            }else{
                file.createNewFile();
            }
        }catch (Exception e){
            file.delete();
            log.error(e.getMessage());
            throw new CustomException(ApiResult.result(7000,"创建文件失败"));
        }
    }

    private void checkerFileIsExist(File file){
       if(file.exists()){
           log.error("模板文件已存在!");
           throw new CustomException(ApiResult.result(7000,"模板文件已存在!"));
       }
    }

    private void recursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    recursionDeleteFile(f);
                } else {
                    f.delete();
                }
            }
            file.delete();
        }

    }

    private File checkFileIsExist(String path) {
        String filePath = byteCmsConfig.getSourceTempPath() + File.separator + path;
        File file = new File(filePath);
        if (!(file.exists() && file.isFile())) {
            throw new CustomException(ApiResult.result(20000));
        }
        return file;
    }

    private TreeFileInfo recursionTree(String path, TreeFileInfo treeFileInfoDto) {
        List<TreeFileInfo> treefiles = buildData(FileUtil.getFileList(path, null));
        if (Checker.BeNotEmpty(treefiles)) {
            treeFileInfoDto.setChildren(treefiles);
            for (TreeFileInfo treeFileInfo : treeFileInfoDto.getChildren()) {
                String relativePath = path.replace(byteCmsConfig.getSourceTempPath(), "") + File.separator + treeFileInfo.getFileInfo().getFileName();
                treeFileInfo.setIsLeaf(!treeFileInfo.getFileInfo().isDirectory()).setRelativePath(relativePath);
                if (treeFileInfo.getFileInfo().isDirectory()) {
                    String filePath = path + File.separator + treeFileInfo.getFileInfo().getFileName();
                    recursionTree(filePath, treeFileInfo);
                }
            }
        }
        return treeFileInfoDto;
    }

    private List<TreeFileInfo> buildData(List<FileInfo> files) {
        List<TreeFileInfo> treeFileInfos = new ArrayList<>();
        if (Checker.BeNotEmpty(files)) {
            files.forEach(file -> {
                if (file.isDirectory() && "fragment".equals(file.getFileName())) {
                    return;
                }
                TreeFileInfo treeFileInfo = new TreeFileInfo();
                treeFileInfo.setFileInfo(file);
                Integer key = file.hashCode();
                treeFileInfo.setTitle(file.getFileName()).setKey(String.valueOf(key));
                treeFileInfos.add(treeFileInfo);
            });
        }
        return treeFileInfos;
    }

    @Override
    public void downZip(String path,HttpServletResponse response) {
        String tempPath = byteCmsConfig.getSourceTempPath();
        if(Checker.BeNotBlank(path)){
            tempPath=tempPath+File.separator+path;
        }
        File file = new File(tempPath);
        if(!file.exists()||file.isFile()){
            throw  new CustomException(ApiResult.result(20015));
        }
        String timeStr= DateTimeFormatter.ofPattern(Constants.YMDHM).format(LocalDateTime.now());
        String fileId= byteCmsConfig.getSourceRootPath()+File.separator+timeStr+Constants.DEFAULT_ZIP_SUFFIX;
        File filePath=null;
        InputStream in = null;
        OutputStream out = null;
        try {
            ZipUtil.zip(tempPath,fileId);
            filePath = new File(fileId);
            if(filePath.exists()){
                response.setCharacterEncoding(Constants.DEFAULT_CHARSET_NAME);
                response.setHeader("content-disposition","attachment;fileName=temp_"+timeStr+Constants.DEFAULT_ZIP_SUFFIX);
                response.setHeader("FileName", "temp_"+timeStr+Constants.DEFAULT_ZIP_SUFFIX);
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

    @Override
    public void importFile(MultipartFile multipartFile, Integer type) {
        String path = byteCmsConfig.getSourceTempPath();
       String suffix= FileUtil.getSuffix(multipartFile.getOriginalFilename());
       if(!Constants.DEFAULT_ZIP_SUFFIX.equals(Constants.DOT+suffix)){
           throw new CustomException(ApiResult.result(20016));
       }
       if(type ==1){
           path = byteCmsConfig.getSourceFragmentFilePath();
       }
        File toFile =null;
        try {
            toFile = new File(multipartFile.getOriginalFilename());
            InputStream ins= multipartFile.getInputStream();
            inputStreamToFile(ins, toFile);
            ins.close();
            ZipUtil.unZip(toFile,path);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ApiResult.result(20017));
        }
        toFile.delete();
    }

    @Cacheable(value = Constants.cacheName, key = "#root.targetClass+'.'+#root.methodName" , unless="#result == null")
    @Override
    public ApiResult getDirects() {
        List<Map<String, String>> directives=new ArrayList<>(16);
        Element element=XMLParser.parserElement("directs.xml");
        List<Element> directs=element.elements("direct");
        for (DirectiveNameEnum directiveNameEnum : DirectiveNameEnum.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("code", directiveNameEnum.getCode());
            map.put("name", directiveNameEnum.getName());
            map.put("value", directiveNameEnum.getValue());
            map.put("example", getDirectsExample(directiveNameEnum,directs));
            directives.add(map);
        }
        return ApiResult.result(directives);
    }

    private String getDirectsExample(DirectiveNameEnum directiveNameEnum,List<Element> directs){
        String example="";
        for (Element direct : directs){
            String name=direct.elementText("name");
            if(directiveNameEnum.getValue().equals(name)){
                Element directElement=direct.element("example");
                String directExample=directElement.getText();
                if(Checker.BeNotBlank(directExample)){
                    example=directExample.replace("《","<").replace("》",">").replace("&amp;","&");
                }
            }
        }
        return example;
    }


    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void testrecursionTree(){
//        TreeFileInfo treeFileInfo =new TreeFileInfo();
//        TreeFileInfo fileInfo= new TemplateServiceImpl().recursionTree("E:\\新建文件夹",treeFileInfo);
//        System.out.println(JSON.toJSONString(fileInfo));
//    }

}
