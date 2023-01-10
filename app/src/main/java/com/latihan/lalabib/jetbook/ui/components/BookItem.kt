package com.latihan.lalabib.jetbook.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.latihan.lalabib.jetbook.R
import com.latihan.lalabib.jetbook.ui.theme.JetBookTheme
import com.latihan.lalabib.jetbook.ui.theme.Shapes

@Composable
fun BookItem(
    image: Int,
    title: String,
    author: String,
    price: Int,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = 2.dp,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
                .sizeIn(minHeight = 72.dp)
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(85.dp)
                    .clip(Shapes.medium)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Text(
                    text = stringResource(id = R.string.author, author),
                    style = MaterialTheme.typography.subtitle2,
                )
                Text(
                    text = stringResource(id = R.string.required_price, price),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.secondary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookItemPreview() {
    JetBookTheme {
        BookItem(R.drawable.book_4, "nama buku", "Tara Westover", 80000)
    }
}