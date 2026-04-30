package org.myproject.ui.pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.myproject.ui.elements.SearchPageElements;

public class SearchPage extends BasePage {
    public SearchPage(WebDriver driver, String url) {
        super(driver, url);
    }

    public SearchPage(WebDriver driver) {
        super(driver, "https://www.google.com");
    }

    public void executeSearch(boolean pressEnter) {
        var element = new SearchPageElements();

        Allure.step("Выполнить поиск", () -> {
            checkElement(element.SEARCH_BUTTON);

            if (pressEnter) pressKeys(element.SEARCH_BUTTON, "ENTER", Keys.ENTER);
            else clickOnElement(element.SEARCH_BUTTON);

            checkElement(element.RESULTS_TABLE);
        });
    }
}
