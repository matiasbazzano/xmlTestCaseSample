package org.example.services;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class CleanDirectory {

    public void clean(String xmlFilePath) throws Exception {
        File inputFile = new File(xmlFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        NodeList directoryNodes = doc.getElementsByTagName("cleanDirectory");

        for (int i = 0; i < directoryNodes.getLength(); i++) {
            Element element = (Element) directoryNodes.item(i);
            String dirPath = element.getElementsByTagName("path").item(0).getTextContent();
            cleanDirectory(dirPath);
        }
    }

    private void cleanDirectory(String dirPath) {
        File directory = new File(dirPath);

        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    cleanDirectory(file.getAbsolutePath());
                }
                file.delete();
            }
        }
    }
}
