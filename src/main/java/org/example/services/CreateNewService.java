package org.example.services;

import org.example.utils.XMLUtils;
import org.example.bd.SQLite;

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

public class CreateNewService {

    private SQLite db = new SQLite();

    public void create(String xmlFilePath) throws ParserConfigurationException, SAXException, IOException, SQLException {
        File inputFile = new File(xmlFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        NodeList createServiceList = doc.getElementsByTagName("createNewService");
        if (createServiceList.getLength() > 0) {
            Element createServiceElement = (Element) createServiceList.item(0);
            String serviceId = XMLUtils.getTagValue("serviceId", createServiceElement);
            String serviceName = XMLUtils.getTagValue("serviceName", createServiceElement);
            String serviceBalance = XMLUtils.getTagValue("serviceBalance", createServiceElement);

            db.insert(serviceId, serviceName, serviceBalance, null);
        }
    }
}
