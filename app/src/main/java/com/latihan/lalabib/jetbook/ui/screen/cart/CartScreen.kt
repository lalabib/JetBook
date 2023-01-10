package com.latihan.lalabib.jetbook.ui.screen.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.latihan.lalabib.jetbook.R
import com.latihan.lalabib.jetbook.di.Injection
import com.latihan.lalabib.jetbook.ui.ViewModelFactory
import com.latihan.lalabib.jetbook.ui.common.UiState
import com.latihan.lalabib.jetbook.ui.components.CartItem
import com.latihan.lalabib.jetbook.ui.components.OrderButton

@Composable
fun CarteScreen(
    viewModel: CartViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository()
        )
    ),
    onOrderButtonClicked: (String) -> Unit
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getAddedOrderBooks()
            }

            is UiState.Success -> {
                CartContent(
                    state = uiState.data,
                    onProductCountChanged = { bookId, count ->
                        viewModel.updateOrderBook(bookId, count)
                    },
                    onOrderButtonClicked = onOrderButtonClicked
                )
            }

            is UiState.Error -> {}
        }
    }
}

@Composable
fun CartContent(
    state: CartState,
    onProductCountChanged: (id: Long, count: Int) -> Unit,
    onOrderButtonClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val shareMessage = stringResource(
        R.string.share_message,
        state.orderBook.count(),
        state.totalRequiredPrice
    )

    Column {
        TopAppBar(backgroundColor = MaterialTheme.colors.surface) {
            Text(
                text = stringResource(id = R.string.menu_cart),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
        Column(modifier = modifier.weight(1f)) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.orderBook, key = { it.book.id }) { item ->
                    CartItem(
                        bookId = item.book.id,
                        image = item.book.image,
                        title = item.book.title,
                        totalPrice = item.book.price * item.count,
                        count = item.count,
                        onProductCountChanged = onProductCountChanged
                    )
                }
            }
        }
        Column(modifier = modifier.padding(16.dp)) {
            OrderButton(
                text = stringResource(id = R.string.total_order, state.totalRequiredPrice),
                enabled = state.orderBook.isNotEmpty(),
                onClick = {
                    onOrderButtonClicked(shareMessage)
                },

            )
        }
    }
}