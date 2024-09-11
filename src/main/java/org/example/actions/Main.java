package org.example.actions;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        serviceAction action = new serviceAction();

        String input = "1";
        String expectedOutput = "serviceId: 001, output: 5000";

        String result = action.validateInputAndGetDetails(input);

        System.out.println(result);

        if (expectedOutput.equals(result)) {
            System.out.println("Success.");
        } else {
            System.out.println("Unexpected output.");
            System.out.println("Expected output: " + expectedOutput);
        }
    }
}
