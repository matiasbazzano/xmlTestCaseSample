package org.example.actions;

import org.example.data.data;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        TestExecutor testExecutor = new TestExecutor();
        try {
            testExecutor.executeTestFromXML(data.testPath);
        } catch (ParserConfigurationException | SAXException | IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
