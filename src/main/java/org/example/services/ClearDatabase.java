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

public class ClearDatabase {

    private SQLite db = new SQLite();

    public void clearData(String xmlFilePath) throws ParserConfigurationException, SAXException, IOException, SQLException {
        File inputFile = new File(xmlFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        NodeList clearDatabaseList = doc.getElementsByTagName("clearDatabase");
        if (clearDatabaseList.getLength() > 0) {
            Element clearDatabaseElement = (Element) clearDatabaseList.item(0);
            String selectOption = XMLUtils.getTagValue("selectOption", clearDatabaseElement);

            if ("Y".equals(selectOption)) {
                db.deleteAll();
            }
        }
    }
}