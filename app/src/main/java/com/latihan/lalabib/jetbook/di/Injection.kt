package com.latihan.lalabib.jetbook.di

import com.latihan.lalabib.jetbook.data.BookRepository

object Injection{
    fun provideRepository(): BookRepository {
        return BookRepository.getInstance()
    }
}
