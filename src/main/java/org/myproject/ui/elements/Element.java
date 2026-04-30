package org.myproject.ui.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;
import java.util.List;

public class Element {
    private final By locator;
    private final WebDriverWait wait;
    private final List<WebElement> elements;
    private final int size;

    public Element(WebDriver driver, By locator, int timeout) {
        this.locator = locator;
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
        wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        elements = driver.findElements(locator);
        size = elements.size();
    }

    public Element(WebDriver driver, By locator) {
        this(driver, locator, 4);
    }

    @Override
    public String toString() {
        return String.format("\nЭлемент(локатор: %s, количество: %d)", locator, size);
    }

    public boolean isPresent() {
        return size > 0;
    }

    public boolean isClickable() {
        return isPresent() && elements.get(0).isEnabled();
    }

    public boolean isNotClickable() {
        return isPresent() && !elements.get(0).isEnabled();
    }

    public boolean isNotPresent() {
        return !isPresent();
    }

    public void typeText(String text) {
        elements.get(0).sendKeys(text);
    }

    public void click() {
        elements.get(0).click();
    }

    public String[] attributeValues(String attribute) {
        String[] attributeValues = new String[size];

        if (attribute.equals("text")) {
            for (int i = 0; i < size; i++) {
                attributeValues[i] = elements.get(i).getText();
            }
        } else {
            for (int i = 0; i < size; i++) {
                attributeValues[i] = elements.get(i).getAttribute(attribute);
                if (attributeValues[i] != null) attributeValues[i] = attributeValues[i].replaceAll("<[^>]*>", "");
            }
        }

        return attributeValues;
    }

    public void press(CharSequence... keysToPress) {
        elements.get(0).sendKeys(keysToPress);
    }

    public String attribute(String attribute) {
        String attributeValue;

        if (attribute.equals("text")) attributeValue = elements.get(0).getText();
        else {
            attributeValue = elements.get(0).getAttribute(attribute);
            if (attributeValue != null) attributeValue = attributeValue.replaceAll("<[^>]*>", "");
        }

        return attributeValue;
    }

    public boolean isAttributeChanged(String attribute, String oldValue) {
        try {
            wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(locator, attribute, oldValue)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
