package com.latihan.lalabib.jetbook.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.latihan.lalabib.jetbook.data.BookRepository
import com.latihan.lalabib.jetbook.model.Book
import com.latihan.lalabib.jetbook.model.OrderBook
import com.latihan.lalabib.jetbook.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: BookRepository): ViewModel() {

    private val _uiState: MutableStateFlow<UiState<OrderBook>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<OrderBook>>
        get() = _uiState

    fun getBookById(bookId: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getBookById(bookId))
        }
    }

    fun addToCart(book: Book, count: Int) {
        viewModelScope.launch {
            repository.updateOrderBooks(book.id, count)
        }
    }
}