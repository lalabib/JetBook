package com.latihan.lalabib.jetbook.model

data class Book (
    val id: Long,
    val image: Int,
    val title: String,
    val desc: String,
    val price: Int,
    val author: String,
    val page: Int,
    val publicationYear: String,
    val publisher: String
)