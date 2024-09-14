package org.example.services;

import org.example.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class SelectCreatedService {

    public String select(String xmlFilePath) throws ParserConfigurationException, SAXException, IOException, SQLException {
        File inputFile = new File(xmlFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        NodeList selectServiceList = doc.getElementsByTagName("selectCreatedService");
        String selectedServiceId = null;
        if (selectServiceList.getLength() > 0) {
            Element selectServiceElement = (Element) selectServiceList.item(0);
            selectedServiceId = XMLUtils.getTagValue("serviceId", selectServiceElement);
        }

        return selectedServiceId;
    }
}
