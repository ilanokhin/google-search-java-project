package org.myproject.ui.pages;

import org.myproject.ui.enums.ClickableOptions;
import org.myproject.ui.elements.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.myproject.ui.utils.UniversalLocator.locator;

public class BasePage {
    private final WebDriver driver;
    private final String url;

    public BasePage(WebDriver driver, String url) {
        this.driver = driver;
        this.url = url;
    }

    public void open(String url) {
        Allure.step(String.format("Перейти по адресу %s", url), () -> driver.get(url));
    }

    public void open() {
        open(this.url);
    }

    public void screenshot(String screenshotPath) throws IOException {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

        if (screenshotPath != null && !screenshotPath.isEmpty()) {
            try (FileOutputStream fileStream = new FileOutputStream(screenshotPath)) {
                fileStream.write(screenshot);
            }
        }
    }

    public byte[] screenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public boolean checkElement(String[] elementData, boolean isPresent, ClickableOptions isClickable,
                                boolean makeScreenshot) {
        boolean result;
        Element element = new Element(driver, locator(elementData[1]));
        String allureStep = String.format("Проверить элемент '%s' на ", elementData[0]);
        String assertError = String.format("Элемент '%s' ", elementData[0]);
        String errorDescription;

        if (!isPresent) {
            allureStep += "отсутствие";
            result = element.isNotPresent();
            assertError += "присутствует";
        } else if (isClickable == ClickableOptions.NOT_CLICKABLE) {
            allureStep += "некликабельность";
            result = element.isNotClickable();
            assertError += "кликабельный";
        } else if (isClickable == ClickableOptions.DOES_NOT_MATTER) {
            allureStep += "наличие";
            result = element.isPresent();
            assertError += "отсутствует";
        } else {
            allureStep += "наличие и кликабельность";
            result = element.isClickable();
            assertError += "некликабельный или отсутствует";
        }

        errorDescription = assertError;

        Allure.step(allureStep, () -> {
            if (makeScreenshot) {
                Allure.addAttachment("Скриншот", "image/png", new ByteArrayInputStream(screenshot()), ".png");
            }

            assertTrue(result, errorDescription);
        });

        return result;
    }

    public boolean checkElement(String[] elementData) {
        return checkElement(elementData, true, ClickableOptions.CLICKABLE, true);
    }

    public void typeInElement(String[] elementData, String text) {
        Element element = new Element(driver, locator(elementData[1]));
        Allure.step(String.format("Ввести текст '%s' в элемент '%s'", text, elementData[0]), () -> {
            element.typeText(text);
        });
    }

    public void clickOnElement(String[] elementData) {
        Element element = new Element(driver, locator(elementData[1]));
        Allure.step(String.format("Кликнуть по элементу '%s'", elementData[0]), element::click);
    }

    public void pressKeys(String[] elementData, String keysName, CharSequence... keys) {
        Element element = new Element(driver, locator(elementData[1]));
        Allure.step(String.format("Нажать %s на элементе '%s'", keysName, elementData[0]), () -> element.press(keys));
    }

    public List<String> getElementsAttribute(String[] elementData, String attribute, int startIndex, int endIndex) {
        Element element = new Element(driver, locator(elementData[1]));
        String[] attributeArray = element.attributeValues(attribute);
        List<String> attributeList;
        int finishIndex;

        if (endIndex < 0) finishIndex = attributeArray.length;
        else finishIndex = endIndex;

        attributeList = new ArrayList<>(Arrays.asList(attributeArray).subList(startIndex, finishIndex));
        return attributeList;
    }

    public List<String> getElementsAttribute(String[] elementData, String attribute) {
        return getElementsAttribute(elementData, attribute, 0, -1);
    }

    public String getElementAttribute(String[] elementData, String attribute) {
        Element element = new Element(driver, locator(elementData[1]));
        return element.attribute(attribute);
    }

    public boolean compareElements(String[] element1Data, String[] element2Data, String attributeToCompare,
                                   boolean makeScreenshot) {
        String element1 = getElementAttribute(element1Data, attributeToCompare);
        String element2 = getElementAttribute(element2Data, attributeToCompare);
        String secondName = element2Data[0];

        Allure.step(String.format("Проверить соответствие элементов '%s' и '%s'", element1Data[0], secondName), () -> {
            if (makeScreenshot) {
                Allure.addAttachment("Скриншот", "image/png", new ByteArrayInputStream(screenshot()), ".png");
            }

            assertEquals(element1, element2, String.format("Элементы '%s' и '%s' не соответствуют",
                    element1Data[0], secondName));
        });

        return element1.equals(element2);
    }

    public boolean compareElements(String[] element1Data, String attributeToCompare, String element2OldValue,
                                   String element2Name, boolean makeScreenshot) {
        String element1 = getElementAttribute(element1Data, attributeToCompare);
        String element2 = element2OldValue;
        String secondName = element2Name;

        Allure.step(String.format("Проверить соответствие элементов '%s' и '%s'", element1Data[0], secondName), () -> {
            if (makeScreenshot) {
                Allure.addAttachment("Скриншот", "image/png", new ByteArrayInputStream(screenshot()), ".png");
            }

            assertEquals(element1, element2, String.format("Элементы '%s' и '%s' не соответствуют",
                    element1Data[0], secondName));
        });

        return element1.equals(element2);
    }

    public boolean isElementChanged(String[] elementData, String oldAttributeValue, String attribute,
                                    boolean makeScreenshot) {
        Element element = new Element(driver, locator(elementData[1]));
        boolean result = element.isAttributeChanged(attribute, oldAttributeValue);

        Allure.step(String.format("Проверить, что элемент '%s' изменился", elementData[0]), () -> {
            if (makeScreenshot) {
                Allure.addAttachment("Скриншот", "image/png", new ByteArrayInputStream(screenshot()), ".png");
            }

            assertTrue(result, String.format("Элемент '%s' не изменился", elementData[0]));
        });

        return result;
    }
}
