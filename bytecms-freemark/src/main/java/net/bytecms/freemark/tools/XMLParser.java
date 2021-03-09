package net.bytecms.freemark.tools;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class XMLParser {

    public static Document parserDocument(String fileName)  {
        SAXReader saxReader = new SAXReader();
        Document document=null;
        try {
            Resource resource = new ClassPathResource(fileName);
            InputStream inputStream = resource.getInputStream();
            // File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+fileName);
            document = saxReader.read(inputStream);
        }catch (DocumentException| IOException e){
            log.error("解析指令xnml异常："+e.getMessage());
        }
        return document;
    }

    public static Element parserElement(String fileName)  {
        Document document=parserDocument(fileName);
        return document.getRootElement();
    }

}
