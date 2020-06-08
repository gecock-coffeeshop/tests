package com.ibm.runtimes.events.coffeeshop;

import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matchers;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CoffeeshopUIIT {
    private static final String QUEUE_TABLE_FIRST_ROW = "//table/tbody/tr[1]";
    private static RemoteWebDriver driver;

    @BeforeAll
    public static void setupDriver() throws MalformedURLException {
        String seleniumHost = System.getenv("SELENIUM_URI");
        if (seleniumHost == null) {
            seleniumHost = "http://localhost:4444/wd/hub";
        }
        driver = new RemoteWebDriver(new URL(seleniumHost),DesiredCapabilities.chrome());
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    public void ordersViaHttpShouldBeFulfilled() {
        checkOrderIsFulfilled("HTTP", "IN_PROGRESS");

    }

    @Test
    public void ordersViaKafkaShouldBeFulfilled() {
        checkOrderIsFulfilled("Messaging / Kafka", "IN_QUEUE");
    }

    private void checkOrderIsFulfilled(String orderMethod, String expectedInProgressMessage) {
        driver.get(System.getenv("COFFEESHOP_URI"));
        Select orderMethodDropdown = new Select(driver.findElementById("orderMethod"));
        orderMethodDropdown.selectByVisibleText(orderMethod); 
        driver.findElementById("order-button").click();

        WebElement firstRow = driver.findElementByXPath(QUEUE_TABLE_FIRST_ROW);
        assertThat(firstRow.getText(), StringContains.containsString(expectedInProgressMessage));
    
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(QUEUE_TABLE_FIRST_ROW), "READY"));
    }
     
}