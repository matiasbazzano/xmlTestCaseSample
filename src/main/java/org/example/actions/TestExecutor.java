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

        NodeList createServiceList = doc.getElementsByTagName("createNewService");
        if (createServiceList.getLength() > 0) {
            Element createServiceElement = (Element) createServiceList.item(0);
            String serviceId = getTagValue("serviceId", createServiceElement);
            String serviceName = getTagValue("serviceName", createServiceElement);
            String serviceBalance = getTagValue("serviceBalance", createServiceElement);

            db.insert(serviceId, serviceName, serviceBalance, null);
        }

        NodeList applyStatusList = doc.getElementsByTagName("applyStatus");
        if (applyStatusList.getLength() > 0) {
            Element applyStatusElement = (Element) applyStatusList.item(0);
            String serviceId = getTagValue("serviceId", applyStatusElement);
            String statusType = getTagValue("statusType", applyStatusElement);

            db.updateServiceStatus(serviceId, statusType);
        }

        NodeList selectServiceList = doc.getElementsByTagName("selectCreatedService");
        String selectedServiceId = null;
        if (selectServiceList.getLength() > 0) {
            Element selectServiceElement = (Element) selectServiceList.item(0);
            selectedServiceId = getTagValue("serviceId", selectServiceElement);
        }

        NodeList serviceTestDataList = doc.getElementsByTagName("serviceTestData");
        String expectedServiceName = null;
        String expectedServiceBalance = null;
        String expectedServiceStatus = null;
        if (serviceTestDataList.getLength() > 0) {
            Element serviceTestDataElement = (Element) serviceTestDataList.item(0);
            expectedServiceName = getTagValue("expectedServiceName", serviceTestDataElement);
            expectedServiceBalance = getTagValue("expectedServiceBalance", serviceTestDataElement);
            expectedServiceStatus = getTagValue("expectedServiceStatus", serviceTestDataElement);
        }

        if (selectedServiceId != null) {
            int serviceId = Integer.parseInt(selectedServiceId);
            SQLite.ServiceData serviceData = db.getServiceDataById(serviceId);

            if (expectedServiceName != null && expectedServiceBalance != null && expectedServiceStatus != null && serviceData != null) {
                String formattedServiceBalance = serviceData.serviceBalance;

                if (expectedServiceName.equals(serviceData.serviceName) &&
                        expectedServiceBalance.equals(formattedServiceBalance) &&
                        expectedServiceStatus.equals(serviceData.serviceStatus)) {
                    System.out.println("\nTest passed.");
                } else {
                    System.out.println("\nTest failed.\n");
                    System.out.println("Expected:\nserviceName: " + expectedServiceName + "\nserviceBalance: " + expectedServiceBalance + "\nserviceStatus: " + expectedServiceStatus + "\n");
                    System.out.println("Actual:\nserviceName: " + serviceData.serviceName + "\nserviceBalance: " + formattedServiceBalance + "\nserviceStatus: " + serviceData.serviceStatus);
                }
            } else {
                System.out.println("Test failed: Service data not found.");
            }
        }

        NodeList clearDataBaseDataList = doc.getElementsByTagName("clearDataBaseData");
        if (serviceTestDataList.getLength() > 0) {
            Element clearDataBaseDataElement = (Element) clearDataBaseDataList.item(0);
            String selectOption = getTagValue("selectOption", clearDataBaseDataElement);

            if ("Y".equals(selectOption)) {
                db.deleteAll();
            }
        }
    }

    private String getTagValue(String tagName, Element element) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        return (nodeList.getLength() > 0) ? nodeList.item(0).getTextContent().trim() : null;
    }
}