package net.bytecms.service.service.webfile;

import com.google.common.collect.Lists;
import net.bytecms.core.config.ByteCmsConfig;
import net.bytecms.core.constants.Constants;
import net.bytecms.core.handler.CustomException;
import net.bytecms.service.api.webfile.WebStaticFileService;
import net.bytecms.service.dto.webfile.TemplateDto;
import lombok.extern.slf4j.Slf4j;
import net.bytecms.core.utils.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class WebStaticFileServiceImpl implements WebStaticFileService {

    @Autowired
    ByteCmsConfig byteCmsConfig;

    @Override
    public List<FileInfo> listPage(String path, String parentPath) {
        String basePath= byteCmsConfig.getSiteStaticFileRootPath();
        String newPath= Checker.BeNotBlank(path)?basePath+path:basePath;
        List<FileInfo> allfileInfos=new ArrayList<>(16);
        if(Checker.BeNotBlank(path) && Checker.BeNotBlank(parentPath)){
            FileInfo fileInfo=new FileInfo();
            String parent=parentPath.substring(0,parentPath.lastIndexOf(File.separator));
            fileInfo.setFileName("返回上一级").setParentRelativePath(parent);
            if(!File.separator.equals(parentPath)){
                fileInfo.setRelativePath(parent);
            }else{
                fileInfo.setRelativePath("");
            }
            allfileInfos.add(fileInfo);
        }
        List<FileInfo> fileInfos= FileUtil.getFileList(newPath,FileUtil.ORDERFIELD_FILENAME);
        allfileInfos.addAll(fileInfos);
        if(Checker.BeNotEmpty(fileInfos)){
            for (FileInfo fileInfo : fileInfos) {
                fileInfo.setRelativePath(path + File.separator + fileInfo.getFileName());
                fileInfo.setParentRelativePath(path + File.separator);
            }
        }
        return Checker.BeNotEmpty(allfileInfos)?allfileInfos:Lists.newArrayList();
    }

    @Override
    public ApiResult uploadFile(List<MultipartFile> files, String filePath) {
        if(Checker.BeNotEmpty(files)){
            checkerFileSize(files);
            String basePath= byteCmsConfig.getSiteStaticFileRootPath();
            String finalPath=basePath+filePath;
            byte[] bs = new byte[1024];
            for (MultipartFile file : files) {
                InputStream inputStream = null;
                OutputStream os = null;
                int len;
                try {
                    inputStream = file.getInputStream();
                    String fileName = file.getOriginalFilename();
                    os = new FileOutputStream(finalPath+File.separator+fileName);
                    // 开始读取
                    while ((len = inputStream.read(bs)) != -1) {
                        os.write(bs, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return ApiResult.result(20026);
                }finally {
                    // 完毕，关闭所有链接
                    try {
                        os.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return ApiResult.result(20026);
                    }
                }
            }
        }
        return ApiResult.result();
    }

    private void checkerFileSize(List<MultipartFile> files){
        if(Checker.BeNotEmpty(files)){
            files.forEach(file->{
                if(file.getSize()>=100*1024*1024){
                    throw new CustomException(ApiResult.result(20027));
                }
            });
        }
    }

    @Override
    public ApiResult createFile(String filePath, String fileName) {
        String basePath = byteCmsConfig.getSiteStaticFileRootPath();
        String finalPath = basePath+filePath+File.separator+fileName;
        File file=new File(finalPath);
        file.mkdir();
        return ApiResult.result();
    }

    @Override
    public ApiResult getFileContent(String path) {
        String filePath = byteCmsConfig.getSiteStaticFileRootPath() + File.separator + path;
        File file = new File(filePath);
        try {
            if (file.isFile()) {
                String fileContent = FileUtils.readFileToString(file, Constants.DEFAULT_CHARSET_NAME);
                return ApiResult.result(fileContent);
            }
        } catch (IOException e) {
            log.error("读取資源文件失败！");
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
            log.error("写入文件失败！");
            return ApiResult.result(20001);
        }
        return ApiResult.result();
    }

    @Override
    public ApiResult deleteFile(String filePath) {
        filePath = byteCmsConfig.getSiteStaticFileRootPath() + File.separator + filePath;
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
        String fileName = templateDto.getIsDirectory()?templateDto.getFileName():templateDto.getFileName()+Constants.DEFAULT_HTML_SUFFIX;
        File file = new File(byteCmsConfig.getSiteStaticFileRootPath()+File.separator+templateDto.getFilePath()+File.separator+fileName);
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
        String filePath = byteCmsConfig.getSiteStaticFileRootPath() + File.separator + path;
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
                String relativePath = path.replace(byteCmsConfig.getSiteStaticFileRootPath(), "") + File.separator + treeFileInfo.getFileInfo().getFileName();
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
        String tempPath = byteCmsConfig.getSiteStaticFileRootPath();
        String fileName = path.substring(path.lastIndexOf(File.separator),path.length());
        if(Checker.BeNotBlank(path)){
            tempPath=tempPath+File.separator+path;
        }
        File file = new File(tempPath);
        if(!file.exists()||file.isFile()){
            throw  new CustomException(ApiResult.result(20015));
        }
        if(file.listFiles().length!=0){
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
                    response.setHeader("content-disposition","attachment;fileName="+fileName+Constants.DEFAULT_ZIP_SUFFIX);
                    response.setHeader("FileName", fileName+Constants.DEFAULT_ZIP_SUFFIX);
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
        }else{
            try {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().print("该目录为空");
            } catch (IOException e) {
                e.printStackTrace();
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
