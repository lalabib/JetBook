package com.latihan.lalabib.jetbook.ui.screen.cart

import com.latihan.lalabib.jetbook.model.OrderBook

data class CartState(
    val orderBook: List<OrderBook>,
    val totalRequiredPrice: Int
)