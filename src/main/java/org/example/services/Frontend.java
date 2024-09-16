package org.example.services;

import org.example.bd.SQLite;
import org.example.data.data;
import org.example.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Frontend {

    private SQLite sqlite = new SQLite();

    public void generateHTML(String xmlFilePath) throws Exception {
        File inputFile = new File(xmlFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        NodeList frontendList = doc.getElementsByTagName("frontend");
        if (frontendList.getLength() > 0) {
            Element frontendElement = (Element) frontendList.item(0);
            String generateHTML = XMLUtils.getTagValue("generateHTML", frontendElement);
            if ("Y".equalsIgnoreCase(generateHTML)) {

                NodeList selectCreatedServiceList = doc.getElementsByTagName("selectCreatedService");
                if (selectCreatedServiceList.getLength() > 0) {
                    Element selectCreatedServiceElement = (Element) selectCreatedServiceList.item(0);
                    String serviceId = XMLUtils.getTagValue("serviceId", selectCreatedServiceElement);

                    SQLite.ServiceData service = sqlite.getServiceDataById(Integer.parseInt(serviceId));

                    File outputFile = new File(data.frontendPath + "output.html");
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                        writer.write("<html><head><title>Service Data</title></head><body>");
                        writer.write("<h1>Service Data</h1>");
                        writer.write("<table border='1'><tr><th>ID</th><th>Service Name</th><th>Service Balance</th><th>Status</th></tr>");

                        if (service != null) {
                            writer.write("<tr>");
                            writer.write("<td>" + serviceId + "</td>");
                            writer.write("<td>" + service.serviceName + "</td>");
                            writer.write("<td>" + service.serviceBalance + "</td>");
                            writer.write("<td>" + service.serviceStatus + "</td>");
                            writer.write("</tr>");
                        }

                        writer.write("</table>");
                        writer.write("</body></html>");
                    }
                }
            }
        }
    }
}