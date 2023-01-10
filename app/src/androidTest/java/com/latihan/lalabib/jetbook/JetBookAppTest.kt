package com.latihan.lalabib.jetbook

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.latihan.lalabib.jetbook.model.BooksData
import com.latihan.lalabib.jetbook.ui.navigation.Screen
import com.latihan.lalabib.jetbook.ui.theme.JetBookTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class JetBookAppTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            JetBookTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                JetBookApp(navController = navController)
            }
        }
    }

    @Test
    fun navHost_verifyStartDestination() {
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_clickItem_navigatesToDetailWithData() {
        composeTestRule.onNodeWithTag("BookList").performScrollToIndex(8)
        composeTestRule.onNodeWithText(BooksData.books[6].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailBook.route)
        composeTestRule.onNodeWithText(BooksData.books[6].title).assertIsDisplayed()
    }

    @Test
    fun navHost_bottomNavigation_isWorking() {
        composeTestRule.onNodeWithStringId(R.string.menu_home).performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
        composeTestRule.onNodeWithStringId(R.string.menu_cart).performClick()
        navController.assertCurrentRouteName(Screen.Cart.route)
        composeTestRule.onNodeWithStringId(R.string.menu_profile).performClick()
        navController.assertCurrentRouteName(Screen.Profile.route)
    }

    @Test
    fun navHost_clickItem_navigatesBack() {
        composeTestRule.onNodeWithTag("BookList").performScrollToIndex(10)
        composeTestRule.onNodeWithText(BooksData.books[10].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailBook.route)
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back)).performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_checkout_correctBackStack() {
        composeTestRule.onNodeWithText(BooksData.books[4].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailBook.route)
        composeTestRule.onNodeWithStringId(R.string.plus_symbol).performClick()
        composeTestRule.onNodeWithContentDescription("Order Button").performClick()
        navController.assertCurrentRouteName(Screen.Cart.route)
        composeTestRule.onNodeWithStringId(R.string.menu_home).performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }
}