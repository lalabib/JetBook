package com.latihan.lalabib.jetbook.ui.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.latihan.lalabib.jetbook.data.BookRepository
import com.latihan.lalabib.jetbook.model.Book
import com.latihan.lalabib.jetbook.model.OrderBook
import com.latihan.lalabib.jetbook.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: BookRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<OrderBook>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<OrderBook>>>
        get() = _uiState

    private val _groupedBook = MutableStateFlow(
        repository.getBooks()
            .sortedBy { it.title }
            .groupBy { it.title[0] }
    )
    val groupedBooks: StateFlow<Map<Char, List<Book>>> get() = _groupedBook

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun search(newQuery: String) {
        _query.value = newQuery
        _groupedBook.value = repository.searchBooks(_query.value)
            .sortedBy { it.title }
            .groupBy { it.title[0] }
    }

    fun getAllBooks() {
        viewModelScope.launch {
            repository.getAllBooks()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { orderBooks ->
                    _uiState.value = UiState.Success(orderBooks)
                }
        }
    }
}