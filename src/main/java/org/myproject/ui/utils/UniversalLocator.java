package org.myproject.ui.utils;

import org.openqa.selenium.By;

public class UniversalLocator {
    public static By locator(String css_or_xpath_locator) {
        if (css_or_xpath_locator.startsWith("/")) return By.xpath(css_or_xpath_locator);
        else return By.cssSelector(css_or_xpath_locator);
    }
}
