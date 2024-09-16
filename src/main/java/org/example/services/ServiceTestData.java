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

public class ServiceTestData {

    private SQLite db = new SQLite();

    public void testData(String xmlFilePath) throws ParserConfigurationException, SAXException, IOException, SQLException {
        File inputFile = new File(xmlFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        SelectCreatedService selectService = new SelectCreatedService();
        String selectedServiceId = selectService.select(xmlFilePath);

        NodeList serviceTestDataList = doc.getElementsByTagName("serviceTestData");
        String expectedServiceName = null;
        String expectedServiceBalance = null;
        String expectedServiceStatus = null;
        if (serviceTestDataList.getLength() > 0) {
            Element serviceTestDataElement = (Element) serviceTestDataList.item(0);
            expectedServiceName = XMLUtils.getTagValue("expectedServiceName", serviceTestDataElement);
            expectedServiceBalance = XMLUtils.getTagValue("expectedServiceBalance", serviceTestDataElement);
            expectedServiceStatus = XMLUtils.getTagValue("expectedServiceStatus", serviceTestDataElement);
        }

        if (selectedServiceId != null) {
            int serviceId = Integer.parseInt(selectedServiceId);
            SQLite.ServiceData serviceData = db.getServiceDataById(serviceId);

            if (expectedServiceName != null && expectedServiceBalance != null && expectedServiceStatus != null && serviceData != null) {
                String formattedServiceBalance = serviceData.serviceBalance;

                if (expectedServiceName.equals(serviceData.serviceName) &&
                        expectedServiceBalance.equals(formattedServiceBalance) &&
                        expectedServiceStatus.equals(serviceData.serviceStatus)) {
                    System.out.println("\nDB Check: Test PASSED.");
                } else {
                    System.out.println("\nDB Check: Test FAILED.\n");
                    System.out.println("Expected:\nserviceName: " + expectedServiceName + "\nserviceBalance: " + expectedServiceBalance + "\nserviceStatus: " + expectedServiceStatus + "\n");
                    System.out.println("Actual:\nserviceName: " + serviceData.serviceName + "\nserviceBalance: " + formattedServiceBalance + "\nserviceStatus: " + serviceData.serviceStatus);
                }
            } else {
                System.out.println("Test failed: Service data not found.");
            }
        } else {
            System.out.println("Test failed: Selected service ID not found.");
        }
    }
}