package com.ibm.runtimes.events.coffeeshop;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

public class CoffeeshopUIIT {
    private static RemoteWebDriver driver;

    @BeforeAll
    public static void setupDriver() throws MalformedURLException {
        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),DesiredCapabilities.chrome());
    }

    @Test
    public void ordersViaHttpShouldBeFulfilled() {
        driver.get("http://coffeeshop-ui-coffeeshop.apps.nephew.os.fyre.ibm.com");
        Select orderMethodDropdown = new Select(driver.findElementById("orderMethod"));
        orderMethodDropdown.selectByVisibleText("HTTP");   
        driver.findElementById("order-button").click();

        WebElement queueTable = driver.findElementByCssSelector(".table");
        WebElement firstRow = queueTable.findElement(By.tagName("tr"));
        List<WebElement> cells = firstRow.findElements(By.tagName("td"));
        assertThat(cells, hasItem(hasProperty("Text", equalTo("IN PROGRESS)"))));

        // WebDriverWait wait = new WebDriverWait(driver,5);
        // wait.until(driver -> driver.)
    }
}