package org.example;

import org.example.services.*;
import org.example.data.data;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.sql.SQLException;

public class TestRunner {
    public static void main(String[] args) {
        String xmlFilePath = data.testPath + "testCase1.xml";

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

            // Generate HTML with service data
            Frontend frontend = new Frontend();
            frontend.generateHTML(xmlFilePath);

            // Run Selenium tests
            UITesting uiTesting = new UITesting();
            uiTesting.runSeleniumTests(xmlFilePath);

            // Clear database
            ClearDataBaseData clearDatabaseData = new ClearDataBaseData();
            clearDatabaseData.clearData(xmlFilePath);

            // Clean UI folder
            // CleanDirectory cleanDirectory = new CleanDirectory();
            // cleanDirectory.clean(xmlFilePath);

            // Display data from table
            DisplayDataFromTable displayDataFromTable = new DisplayDataFromTable();
            displayDataFromTable.display(xmlFilePath);

        } catch (ParserConfigurationException | SAXException | IOException | SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
