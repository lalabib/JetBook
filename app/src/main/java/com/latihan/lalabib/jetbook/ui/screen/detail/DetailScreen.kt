package com.latihan.lalabib.jetbook.ui.screen.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.latihan.lalabib.jetbook.R
import com.latihan.lalabib.jetbook.di.Injection
import com.latihan.lalabib.jetbook.ui.ViewModelFactory
import com.latihan.lalabib.jetbook.ui.common.UiState
import com.latihan.lalabib.jetbook.ui.components.OrderButton
import com.latihan.lalabib.jetbook.ui.components.ProductCounter
import com.latihan.lalabib.jetbook.ui.theme.JetBookTheme

@Composable
fun DetailScreen(
    bookId: Long,
    viewModel: DetailViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository()
        )
    ),
    navigateBack: () -> Unit,
    navigateToCart: () -> Unit
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getBookById(bookId)
            }

            is UiState.Success -> {
                val data = uiState.data
                DetailContent(
                    image = data.book.image,
                    title = data.book.title,
                    desc = data.book.desc,
                    price = data.book.price,
                    author = data.book.author,
                    page = data.book.page,
                    publicationYear = data.book.publicationYear,
                    publisher = data.book.publisher,
                    count = data.count,
                    onBackClick = navigateBack,
                    onAddToCart = { count ->
                        viewModel.addToCart(data.book, count)
                        navigateToCart()
                    }
                )
            }

            is UiState.Error -> {}
        }
    }
}

@Composable
fun DetailContent(
    @DrawableRes image: Int,
    title: String,
    desc: String,
    price: Int,
    author: String,
    page: Int,
    publicationYear: String,
    publisher: String,
    count: Int,
    onBackClick: () -> Unit,
    onAddToCart: (count: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var totalPrice by rememberSaveable { mutableStateOf(0) }
    var orderCount by rememberSaveable { mutableStateOf(count) }

    Column(modifier = Modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Box {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = modifier
                        .height(420.dp)
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 16.dp, bottomEnd = 16.dp
                            )
                        )
                )
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.LightGray, RoundedCornerShape(16.dp))
                        .clickable { onBackClick() }
                )
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h4.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Row {
                    Text(
                        text = stringResource(R.string.required_price, price),
                        style = MaterialTheme.typography.h5.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                    ProductCounter(
                        1,
                        orderCount,
                        onProductIncreased = { orderCount++ },
                        onProductDecreased = { if (orderCount > 0) orderCount-- },
                        modifier = Modifier
                            .padding(start = 50.dp)
                    )
                }
                Text(
                    text = stringResource(id = R.string.detail_book),
                    style = MaterialTheme.typography.h6,
                    modifier = modifier.padding(top = 20.dp)
                )
                Text(
                    text = stringResource(id = R.string.author, author),
                    modifier = modifier.padding(top = 8.dp)
                )
                Text(
                    text = stringResource(id = R.string.page, page),
                    modifier = modifier.padding(top = 2.dp)
                )
                Text(
                    text = stringResource(id = R.string.publication_year, publicationYear),
                    modifier = modifier.padding(top = 2.dp)
                )
                Text(
                    text = stringResource(id = R.string.publisher, publisher),
                    modifier = modifier.padding(top = 2.dp, end = 6.dp)
                )
                Text(
                    text = stringResource(id = R.string.desc_book),
                    style = MaterialTheme.typography.h6,
                    modifier = modifier.padding(top = 20.dp)
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Justify,
                    modifier = modifier.padding(top = 6.dp)
                )
            }
        }
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            totalPrice = price * orderCount
            OrderButton(
                text = stringResource(R.string.add_to_cart, totalPrice),
                enabled = orderCount > 0,
                onClick = {
                    onAddToCart(orderCount)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailContentPreview() {
    JetBookTheme {
        DetailContent(
            image = R.drawable.book_6,
            title = "Melangkah",
            desc = stringResource(id = R.string.lorem_ipsum),
            price = 80000,
            author = "Js. Khairen",
            page = 346,
            publicationYear = "22 Mar 2020",
            publisher = "Gramedia Widiasarana Indonesia",
            count = 2,
            onBackClick = {},
            onAddToCart = {}
        )
    }
}