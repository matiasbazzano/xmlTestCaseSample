package org.example.actions;

import org.example.bd.SQLite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.xml.sax.SAXException;

public class TestExecutor {

    private SQLite db = new SQLite();

    public void executeTestFromXML(String xmlFilePath) throws ParserConfigurationException, SAXException, IOException, SQLException {
        File inputFile = new File(xmlFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        NodeList sqlStatementList = doc.getElementsByTagName("sqlStatement");
        NodeList serviceTestDataList = doc.getElementsByTagName("serviceTestData");

        Connection conn = db.connect();

        // INSERT
        if (sqlStatementList.getLength() > 0) {
            String sqlInsert = sqlStatementList.item(0).getTextContent().trim();
            executeSql(conn, sqlInsert);
        }

        // SELECT
        String sqlSelect = null;
        if (sqlStatementList.getLength() > 1) {
            sqlSelect = sqlStatementList.item(1).getTextContent().trim();
        }

        String expectedServiceName = null;
        String expectedServiceBalance = null;
        if (serviceTestDataList.getLength() > 0) {
            Element serviceElement = (Element) serviceTestDataList.item(0);
            expectedServiceName = getTagValue("expectedServiceName", serviceElement);
            expectedServiceBalance = getTagValue("expectedServiceBalance", serviceElement);
        }

        String dbServiceName = null;
        String dbServiceBalance = null;
        if (sqlSelect != null) {
            PreparedStatement pstmt = conn.prepareStatement(sqlSelect);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                dbServiceName = rs.getString("serviceName");
                dbServiceBalance = rs.getString("serviceBalance");
            }
            rs.close();
            pstmt.close();
        }

        conn.close();

        if (expectedServiceName != null && expectedServiceBalance != null &&
                expectedServiceName.equals(dbServiceName) && expectedServiceBalance.equals(dbServiceBalance)) {
            System.out.println("Test passed.");
        } else {
            System.out.println("Test failed.");
            System.out.println("Expected - serviceName: " + expectedServiceName + ", serviceBalance: " + expectedServiceBalance);
            System.out.println("Actual - serviceName: " + dbServiceName + ", serviceBalance: " + dbServiceBalance);
        }
    }

    private void executeSql(Connection conn, String sql) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }
    }

    private String getTagValue(String tagName, Element element) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        return (nodeList.getLength() > 0) ? nodeList.item(0).getTextContent().trim() : null;
    }
}
