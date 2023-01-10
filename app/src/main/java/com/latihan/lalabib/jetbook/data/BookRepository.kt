package com.latihan.lalabib.jetbook.data

import com.latihan.lalabib.jetbook.model.Book
import com.latihan.lalabib.jetbook.model.BooksData
import com.latihan.lalabib.jetbook.model.OrderBook
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookRepository {

    private val orderBooks = mutableListOf<OrderBook>()

    init {
        if (orderBooks.isEmpty()) {
            BooksData.books.forEach {
                orderBooks.add(OrderBook(it, 0))
            }
        }
    }

    fun getAllBooks(): Flow<List<OrderBook>> {
        return flowOf(orderBooks)
    }

    fun getBookById(bookId: Long): OrderBook {
        return orderBooks.first {
            it.book.id == bookId
        }
    }

    fun getBooks(): List<Book> {
        return BooksData.books
    }

    fun searchBooks(query: String): List<Book> {
        return BooksData.books.filter {
            it.title.contains(query, ignoreCase = true)
        }
    }

    fun updateOrderBooks(bookId: Long, newCountValue: Int): Flow<Boolean> {
        val index = orderBooks.indexOfFirst { it.book.id == bookId }
        val result = if (index >= 0) {
            val orderBook = orderBooks[index]
            orderBooks[index] =
                orderBook.copy(book = orderBook.book, count = newCountValue)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    fun getAddedOrderBooks(): Flow<List<OrderBook>> {
        return getAllBooks()
            .map { orderBooks ->
                orderBooks.filter { orderBook ->
                    orderBook.count != 0
                }
            }
    }

    companion object {
        @Volatile
        private var instance: BookRepository? = null

        fun getInstance(): BookRepository =
            instance ?: synchronized(this) {
                BookRepository().apply {
                    instance = this
                }
            }
    }
}