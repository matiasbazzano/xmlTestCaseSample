package org.example.services;

import org.example.bd.SQLite;
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

public class ApplyStatus {

    private SQLite db = new SQLite();

    public void apply(String xmlFilePath) throws ParserConfigurationException, SAXException, IOException, SQLException {
        File inputFile = new File(xmlFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        NodeList applyStatusList = doc.getElementsByTagName("applyStatus");
        if (applyStatusList.getLength() > 0) {
            Element applyStatusElement = (Element) applyStatusList.item(0);
            String serviceId = XMLUtils.getTagValue("serviceId", applyStatusElement);
            String statusType = XMLUtils.getTagValue("statusType", applyStatusElement);

            db.updateServiceStatus(serviceId, statusType);
        }
    }
}
