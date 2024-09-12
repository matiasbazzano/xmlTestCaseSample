package org.example.actions;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        serviceAction action = new serviceAction();

        // ID of the service to check
        String inputServiceId = "1";

        try {
            String result = action.validateInputAndGetDetails(inputServiceId);
            System.out.println(result);
        } catch (ParserConfigurationException | SAXException | IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
