package org.example.services;

import org.junit.Test;
import org.example.data.data;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.example.utils.XMLUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class UITesting {

    private WebDriver driver;
    private String expectedServiceId;
    private String expectedServiceName;
    private String expectedServiceBalance;
    private String expectedServiceStatus;

    @Test
    public void verifyServiceDataInHTML() throws Exception {
        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver", data.chromeDriverPath);
        driver = new ChromeDriver(options);

        try {
            File htmlFile = new File(data.frontendPath + "output.html");
            String fileUrl = htmlFile.toURI().toString();
            driver.get(fileUrl);

            boolean serviceIdDisplayed = isElementDisplayed(By.xpath("//table//td[text()='" + expectedServiceId + "']"));
            boolean serviceNameDisplayed = isElementDisplayed(By.xpath("//table//td[text()='" + expectedServiceName + "']"));
            boolean serviceBalanceDisplayed = isElementDisplayed(By.xpath("//table//td[text()='" + expectedServiceBalance + "']"));
            boolean serviceStatusDisplayed = isElementDisplayed(By.xpath("//table//td[text()='" + expectedServiceStatus + "']"));

            if (serviceIdDisplayed && serviceNameDisplayed && serviceBalanceDisplayed && serviceStatusDisplayed) {
                System.out.println("UI Check: Test PASSED.");
            } else {
                System.out.println("UI Check: Test FAILED.");
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private boolean isElementDisplayed(By by) {
        try {
            WebElement element = driver.findElement(by);
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            System.out.println("Element not found, locator: " + by);
            return false;
        }
    }

    public void runSeleniumTests(String xmlFilePath) throws Exception {
        File inputFile = new File(xmlFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();

        NodeList uiTestingList = doc.getElementsByTagName("uiTesting");
        if (uiTestingList.getLength() > 0) {
            Element uiTestingElement = (Element) uiTestingList.item(0);
            expectedServiceId = XMLUtils.getTagValue("serviceId", uiTestingElement);
            expectedServiceName = XMLUtils.getTagValue("expectedServiceName", uiTestingElement);
            expectedServiceBalance = XMLUtils.getTagValue("expectedServiceBalance", uiTestingElement);
            expectedServiceStatus = XMLUtils.getTagValue("expectedServiceStatus", uiTestingElement);
            verifyServiceDataInHTML();
        }
    }
}