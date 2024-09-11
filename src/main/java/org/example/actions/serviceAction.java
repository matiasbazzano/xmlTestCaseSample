package org.example.actions;

import org.example.data.XMLTags;
import org.example.data.data;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;

public class serviceAction {

    public String validateInputAndGetDetails(String inputValue) throws ParserConfigurationException, SAXException, IOException {
        File inputFile = new File(data.testPath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        NodeList serviceActionList = doc.getElementsByTagName("service");

        for (int i = 0; i < serviceActionList.getLength(); i++) {
            Element serviceElement = (Element) serviceActionList.item(i);
            String currentInput = getTagValue(XMLTags.input, serviceElement);
            String serviceId = getTagValue(XMLTags.serviceId, serviceElement);
            String output = getTagValue(XMLTags.output, serviceElement);

            if (inputValue.equals(currentInput)) {
                return "serviceId: " + serviceId + ", output: " + output;
            }
        }
        return "No valid output found for the given input.";
    }

    private String getTagValue(String tagName, Element element) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        return (nodeList.getLength() > 0) ? nodeList.item(0).getTextContent().trim() : null;
    }
}