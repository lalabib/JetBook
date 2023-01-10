package com.latihan.lalabib.jetbook.ui.screen.detail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.latihan.lalabib.jetbook.R
import com.latihan.lalabib.jetbook.model.Book
import com.latihan.lalabib.jetbook.model.OrderBook
import com.latihan.lalabib.jetbook.onNodeWithStringId
import com.latihan.lalabib.jetbook.ui.theme.JetBookTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailContentTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val fakeBookData = OrderBook(
        book = Book(
            6,
            R.drawable.book_6,
            "Melangkah",
            "Lorem ipsum",
            80000,
            "Js. Khairen",
            346,
            "22 Mar 2020",
            "Gramedia Widiasarana Indonesia",
        ),
        count = 0
    )

    @Before
    fun setUp() {
        composeTestRule.setContent { 
            JetBookTheme {
                DetailContent(
                    image = fakeBookData.book.image,
                    title = fakeBookData.book.title,
                    desc = fakeBookData.book.desc,
                    price = fakeBookData.book.price,
                    author = fakeBookData.book.author,
                    page = fakeBookData.book.page,
                    publicationYear = fakeBookData.book.publicationYear,
                    publisher = fakeBookData.book.publisher,
                    count = fakeBookData.count,
                    onBackClick = {},
                    onAddToCart = {}
                )
            }
        }
    }

    @Test
    fun detailContent_isDisplayed() {
        composeTestRule.onNodeWithText(fakeBookData.book.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.required_price,
                fakeBookData.book.price
            )
        ).assertIsDisplayed()
    }

    @Test
    fun increaseProduct_buttonEnabled() {
        composeTestRule.onNodeWithContentDescription("Order Button").assertIsNotEnabled()
        composeTestRule.onNodeWithStringId(R.string.plus_symbol).performClick()
        composeTestRule.onNodeWithContentDescription("Order Button").assertIsEnabled()
    }

    @Test
    fun increaseProduct_correctCounter() {
        composeTestRule.onNodeWithStringId(R.string.plus_symbol).performClick()
        composeTestRule.onNodeWithTag("count").assert(hasText("1"))
    }

    @Test
    fun decreaseProduct_stillZero() {
        composeTestRule.onNodeWithStringId(R.string.minus_symbol).performClick()
        composeTestRule.onNodeWithTag("count").assert(hasText("0"))
    }
}