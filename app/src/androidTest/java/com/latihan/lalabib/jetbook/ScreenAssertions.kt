package com.latihan.lalabib.jetbook

import androidx.navigation.NavController
import org.junit.Assert

fun NavController.assertCurrentRouteName(expectedRoutName: String) {
    Assert.assertEquals(expectedRoutName, currentBackStackEntry?.destination?.route)
}