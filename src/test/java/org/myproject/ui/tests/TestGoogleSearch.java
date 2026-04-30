package org.myproject.ui.tests;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.myproject.ui.elements.SearchPageElements;
import org.myproject.ui.pages.SearchPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тестирование поиска в Гугл")
@Tag("google_search_suite")
public class TestGoogleSearch extends BaseTest {
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("Поиск в Гугл: параметр isEnterPressed")
    @Tag("google_search")
    public void testGoogleSearch(boolean isEnterPressed) {
        var page = new SearchPage(driver);
        var element = new SearchPageElements();

        page.open();
        page.checkElement(element.SEARCH_FIELD);
        page.typeInElement(element.SEARCH_FIELD, "Яндекс");
        page.checkElement(element.HINT_LIST);

        Allure.step("Проверить, что в списке подсказок есть слово 'яндекс' (не в рекламном блоке)", () -> {
            String hints = String.join("\n", page.getElementsAttribute(element.HINT_LIST_ITEM, "text", 1, -1));
            Allure.addAttachment("Список подсказок", "text/plain", hints, ".txt");
            assertTrue(hints.contains("яндекс"), "Слова 'яндекс' нет в списке подсказок");
        });

        page.executeSearch(isEnterPressed);

        Allure.step("Проверить, что в первых 5 результатах поиска есть ссылка на yandex.ru", () -> {
            String sites = String.join("\n", page.getElementsAttribute(element.RESULTS_TABLE_ITEM_SITE, "text", 0, -5));
            Allure.addAttachment("Сайты из результатов поиска", "text/plain", sites, ".txt");
            assertTrue(sites.contains("yandex.ru"), "Домен yandex.ru отсутствует среди первых пяти результатов");
        });
    }

    @Test
    @DisplayName("Картинки в Гугл")
    @Tag("pictures_in_google")
    public void testPicturesInGoogle() {
        String[] secondPictureSrc = new String[1];

        var page = new SearchPage(driver);
        var element = new SearchPageElements();

        testGoogleSearch(false);
        page.checkElement(element.PICTURES_BUTTON);
        page.clickOnElement(element.PICTURES_BUTTON);

        Allure.step("Открыть вторую картинку, проверить, что она открылась", () -> {
            page.checkElement(element.PICTURE_ITEM);
            page.clickOnElement(element.PICTURE_ITEM);
            page.checkElement(element.SELECTED_PICTURE);
            page.compareElements(element.PICTURE_ITEM, element.SELECTED_PICTURE, "alt", true);
            secondPictureSrc[0] = page.getElementAttribute(element.SELECTED_PICTURE, "src");
        });

        Allure.step("При нажатии кнопки 'Следующее изображение' картинка должна измениться", () -> {
            page.checkElement(element.NEXT_PICTURE_BUTTON);
            page.clickOnElement(element.NEXT_PICTURE_BUTTON);
            page.checkElement(element.SELECTED_PICTURE);
            page.isElementChanged(element.SELECTED_PICTURE, secondPictureSrc[0], "src", true);
        });

        page.checkElement(element.PREVIOUS_PICTURE_BUTTON);
        page.clickOnElement(element.PREVIOUS_PICTURE_BUTTON);

        Allure.step("Проверить, что картинка изменилась на вторую картинку", () -> {
            page.checkElement(element.SELECTED_PICTURE);
            page.compareElements(element.SELECTED_PICTURE, "src", secondPictureSrc[0], "Вторая картинка", true);
        });
    }
}
