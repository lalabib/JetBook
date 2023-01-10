package com.latihan.lalabib.jetbook.ui.screen.home

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.latihan.lalabib.jetbook.di.Injection
import com.latihan.lalabib.jetbook.ui.ViewModelFactory
import com.latihan.lalabib.jetbook.ui.common.UiState
import com.latihan.lalabib.jetbook.ui.components.BookItem
import com.latihan.lalabib.jetbook.ui.components.SearchBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Long) -> Unit
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getAllBooks()
            }

            is UiState.Success -> {
                HomeContent(
                    modifier = modifier,
                    navigateToDetail = navigateToDetail
                )
            }

            is UiState.Error -> {}
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
) {
    val groupedBooks by viewModel.groupedBooks.collectAsState()
    val query by viewModel.query
    Column {
        LazyColumn(
            modifier = modifier.testTag("BookList")
        ) {
            item {
                SearchBar(
                    query = query,
                    onQueryChange = viewModel::search,
                    modifier = Modifier.background(MaterialTheme.colors.primary)
                )
            }
            groupedBooks.forEach { (_, books) ->
                items(books, key = { it.id }) { book ->
                    BookItem(
                        image = book.image,
                        title = book.title,
                        author = book.author,
                        price = book.price,
                        modifier = modifier
                            .animateItemPlacement(tween(durationMillis = 100))
                            .clickable { navigateToDetail(book.id) }
                    )
                }
            }
        }
    }
}