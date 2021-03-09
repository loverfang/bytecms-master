package com.thinkcms.seo;

import net.bytecms.freemark.tools.XMLParser;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

public class XMLDemo {

    @Test
    public void test() throws FileNotFoundException, DocumentException {
        Element element= XMLParser.parserElement("directs.xml");
        List<Element> directs=element.elements("direct");
        for (Element direct : directs){
            String name=direct.elementText("name");
            Element element1=direct.element(name);
            element1.getText();
            element1.getStringValue();
            element1.addText("${title}");
            String xml=element1.asXML();
            //System.out.println(xml);
            xml=xml.replaceAll(name,"@"+name);
            System.out.println(xml);
            //System.out.println(a1);
        }
    }
}
