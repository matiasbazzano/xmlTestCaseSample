package org.example.utils;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLUtils {

    public static String getTagValue(String tagName, Element element) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        return (nodeList.getLength() > 0) ? nodeList.item(0).getTextContent().trim() : null;
    }
}
