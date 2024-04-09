package com.app.techzone.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.techzone.R
import com.app.techzone.SearchWidgetState
import com.app.techzone.Suggestion
import com.app.techzone.ui.theme.ForStroke
import com.app.techzone.ui.theme.RoundBorder28

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppBar(onSearchClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(bottomEnd = 28.dp, bottomStart = 28.dp)
            )
            .fillMaxWidth()
            .height(170.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.padding(top = 40.dp, bottom = 20.dp)) {
            Image(painter = painterResource(R.drawable.ic_logo), contentDescription = null)
        }
        SearchBar(
            query = "",
            onQueryChange = {},
            onSearch = {},
            active = false,
            onActiveChange = { onSearchClicked() },
            content = {},
            placeholder = {
                Text(
                    text = "Поиск в TechZone",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.scrim
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search Button",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background,
                dividerColor = ForStroke
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    text: String,
    searchSuggestions: List<Suggestion>,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    Surface(
        modifier = Modifier
            .height(335.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        SearchBar(
            modifier = Modifier
                .focusRequester(focusRequester)
                .width(255.dp),
            shape = RoundBorder28,
            query = text,
            onQueryChange = onTextChange,
            onSearch = onSearchClicked,
            active = true,
            onActiveChange = { },
            placeholder = {
                Text(
                    text = "Поиск в TechZone",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.scrim
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search Button",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    if (text.isNotEmpty()) {
                        Icon(
                            modifier = Modifier
                                .padding(end = 25.dp)
                                .clickable { onTextChange("") },
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Button",
                            tint = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                        )
                    }
                    Text(
                        text = "Отменить",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(end = 23.dp)
                            .clickable {
                                onTextChange("")
                                onCloseClicked()
                            }
                    )
                }
            },
            windowInsets = WindowInsets.statusBars,
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background,
                dividerColor = Color.Gray.copy(alpha = 0.1f),
            )
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = searchSuggestions.size,
                    key = { index -> searchSuggestions[index].id },
                    itemContent = { index ->
                        val suggestion = searchSuggestions[index]
                        Row(
                            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    top = 20.dp,
                                    end = 16.dp,
                                    bottom = 20.dp
                                )
                                .fillMaxSize()
                                .clickable {
                                    onTextChange(suggestion.name)
                                    onSearchClicked(suggestion.name)
                                    onCloseClicked()
                                }
                        ) {
                            Text(
                                text = suggestion.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                            )
                            Image(
                                imageVector = Icons.Outlined.ChevronRight,
                                contentDescription = null
                            )
                        }
                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))
                    }
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogCategorySearchBar(
    categoryName: String,
    onSearchTriggered: () -> Unit,
    onBackClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(176.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.tertiary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 52.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.Companion.Black,
                    modifier = Modifier.clickable(onClick = onBackClicked)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        categoryName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 1f)
                    )
                }
            }
            SearchBar(
                shape = RoundBorder28,
                query = "",
                onQueryChange = {},
                onSearch = { },
                active = false,
                onActiveChange = { onSearchTriggered() },
                placeholder = {
                    Text(
                        text = "Поиск в TechZone",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.scrim
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search Button",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                    dividerColor = Color.Gray.copy(alpha = 0.1f),
                ),
                content = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}