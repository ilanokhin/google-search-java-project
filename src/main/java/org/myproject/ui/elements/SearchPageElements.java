package org.myproject.ui.elements;

public class SearchPageElements extends BasePageElements {
    public final String[] SEARCH_FIELD = {"Поле поиска", "//textarea[@id='APjFqb']"};
    public final String[] HINT_LIST = {"Список подсказок", ".erkvQe>div>ul"};
    public final String[] HINT_LIST_ITEM = {"Строчка из списка подсказок", ".erkvQe>div>ul>li"};
    public final String[] SEARCH_BUTTON = {"Кнопка поиска", "input[name=btnK]"};
    public final String[] RESULTS_TABLE = {"Таблица с результатами", "#search"};
    public final String[] RESULTS_TABLE_ITEM_SITE = {"Сайт из таблицы с результатами", "#search a cite"};
    public final String[] PICTURES_BUTTON = {"Картинки", "//span[contains(text(),'Картинки')]"};
    public final String[] PICTURE_ITEM = {"Картинка в поисковой выдаче",
            "//div[@jscontroller='XW992c']/div[2]//img[string-length(@alt)>0]"};
    public final String[] SELECTED_PICTURE = {"Выбранная картинка",
            "//div[@aria-hidden='false']//div[@aria-hidden='false']//img[@jsname='JuXqh']"};
    public final String[] NEXT_PICTURE_BUTTON = {"Следующее изображение",
            "//div[@data-sci='1']//button[@aria-label='Следующее изображение']"};
    public final String[] PREVIOUS_PICTURE_BUTTON = {"Предыдущее изображение",
            "//div[@data-sci='2']//button[@aria-label='Предыдущее изображение']"};
}
