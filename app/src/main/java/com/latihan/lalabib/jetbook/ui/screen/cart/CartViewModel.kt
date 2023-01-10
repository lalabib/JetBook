package com.latihan.lalabib.jetbook.ui.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.latihan.lalabib.jetbook.data.BookRepository
import com.latihan.lalabib.jetbook.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val repository: BookRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<CartState>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<CartState>>
        get() = _uiState

    fun getAddedOrderBooks() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getAddedOrderBooks()
                .collect { orderBook ->
                    val totalRequiredPrice =
                        orderBook.sumOf { it.book.price * it.count }
                    _uiState.value = UiState.Success(CartState(orderBook, totalRequiredPrice))
                }
        }
    }

    fun updateOrderBook(bookId: Long, count: Int) {
        viewModelScope.launch {
            repository.updateOrderBooks(bookId, count)
                .collect { isUpdated ->
                    if (isUpdated) {
                        getAddedOrderBooks()
                    }
                }
        }
    }
}