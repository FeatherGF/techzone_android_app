package com.app.techzone.ui.theme.app_bars

import androidx.compose.runtime.Composable

/*** Главный компонент выбирающий какой из TopBar отобразить. Варианты:
 *    - [DefaultAppBar]. Отображается только для UI. Присутствует логотип компании, а поисковая строка,
 *      которая по нажатию вызывает другой TopBar, который ответственнен за поисковую строку полностью
 *    - [SearchAppBar]. Отображается по клику поисковой строки из [DefaultAppBar]. Работает как и
 *      ожидается от обычной поисковой строки. При вводе текста для поиска идет фильтрация по списку
 *      предложений поиска.
 *    - Также имеется возможность не отобразить ни один TopBar. Например для раздела "Профиль"
 *
 *   @param searchWidgetState - состояние виджета. Варианты:
 *    - [SearchWidgetState.OPEN]
 *    - [SearchWidgetState.CLOSED]
 *    - [SearchWidgetState.HIDDEN]
 *   @param searchTextState состояние текста в поле ввода поискового запроса
 *   @param searchSuggestions состояние списка предложений для вводимого запроса
 *   @param onTextChange lambda функция для изменения состояния текста запроса при изменении текста
 *   в поле ввода
 *   @param onCloseClicked lambda функция для изменения состояния на [SearchWidgetState.CLOSED].
 *   Используется для закрытия целевой поисковой строки [SearchAppBar]. При этом действующим
 *   компонентом станет [DefaultAppBar], чья главная функция - правильное UI отображение
 *   @param onSearchClicked lambda функция для срабатывания при открытии поисковой строки [SearchAppBar]
 *   @param onSearchTriggered lambda функция для срабатывания при отправке запроса на поиск
 */
@Composable
fun MainAppBar(
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    searchSuggestions: List<Suggestion>,
    categoryName: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    onBackClicked: () -> Unit
) {
    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            DefaultAppBar(
                onSearchClicked = onSearchTriggered
            )
        }

        SearchWidgetState.OPENED -> {
            SearchAppBar(
                text = searchTextState,
                searchSuggestions = searchSuggestions,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked,
                onSearchClicked = onSearchClicked
            )
        }

        SearchWidgetState.CATALOG_OPENED -> {
            CatalogCategorySearchBar(
                categoryName = categoryName,
                onSearchTriggered = onSearchTriggered,
                onBackClicked = onBackClicked
            )
        }

        SearchWidgetState.HIDDEN -> {}
    }
}
