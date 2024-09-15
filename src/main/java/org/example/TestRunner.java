package org.example;

import org.example.services.*;
import org.example.data.data;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.sql.SQLException;

public class TestRunner {
    public static void main(String[] args) {
        String xmlFilePath = data.testPath;

        try {
            // Create new service
            CreateNewService createNewService = new CreateNewService();
            createNewService.create(xmlFilePath);

            // Apply payment status
            ApplyStatus applyStatus = new ApplyStatus();
            applyStatus.apply(xmlFilePath);

            // Test created data
            ServiceTestData serviceTestData = new ServiceTestData();
            serviceTestData.testData(xmlFilePath);

            // Clear database
            ClearDataBaseData clearDatabaseData = new ClearDataBaseData();
            clearDatabaseData.clearData(xmlFilePath);

            // Display data from table
            DisplayDataFromTable displayDataFromTable = new DisplayDataFromTable();
            displayDataFromTable.display(xmlFilePath);

        } catch (ParserConfigurationException | SAXException | IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
