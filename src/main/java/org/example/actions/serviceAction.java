package org.example.actions;

import org.example.bd.SQLite;
import org.example.data.XMLTags;
import org.example.data.data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;

public class serviceAction {

    private SQLite db = new SQLite();

    public String validateInputAndGetDetails(String inputServiceId) throws ParserConfigurationException, SAXException, IOException, SQLException {
        File inputFile = new File(data.testPath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        NodeList serviceActionList = doc.getElementsByTagName("serviceTestData");

        String xmlServiceName = null;
        String xmlServiceBalance = null;

        for (int i = 0; i < serviceActionList.getLength(); i++) {
            Element serviceElement = (Element) serviceActionList.item(i);
            String serviceId = getTagValue(XMLTags.serviceId, serviceElement);
            if (inputServiceId.equals(serviceId)) {
                xmlServiceName = getTagValue(XMLTags.expectedServiceName, serviceElement);
                xmlServiceBalance = getTagValue(XMLTags.expectedServiceBalance, serviceElement);
                break;
            }
        }

        if (xmlServiceName == null || xmlServiceBalance == null) {
            return "No valid output found for the given input.";
        }

        String dbServiceName = null;
        String dbServiceBalance = null;

        String sql = "SELECT serviceName, serviceBalance FROM services WHERE id = ?";
        Connection conn = db.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, inputServiceId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            dbServiceName = rs.getString("serviceName");
            dbServiceBalance = rs.getString("serviceBalance");
        }

        rs.close();
        pstmt.close();
        conn.close();

        if (xmlServiceName.equals(dbServiceName) && xmlServiceBalance.equals(dbServiceBalance)) {
            return "Match found (Success Test): serviceName: " + dbServiceName + ", serviceBalance: " + dbServiceBalance;
        } else {
            return "Data mismatch.\nTest Data (XML): serviceName: " + xmlServiceName + ", serviceBalance: " + xmlServiceBalance +
                    "\nDB: serviceName: " + dbServiceName + ", serviceBalance: " + dbServiceBalance;
        }
    }

    private String getTagValue(String tagName, Element element) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        return (nodeList.getLength() > 0) ? nodeList.item(0).getTextContent().trim() : null;
    }
}