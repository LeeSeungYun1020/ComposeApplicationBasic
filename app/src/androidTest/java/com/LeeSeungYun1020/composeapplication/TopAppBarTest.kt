package com.LeeSeungYun1020.composeapplication

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.LeeSeungYun1020.composeapplication.ui.components.RallyTopAppBar
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TopAppBarTest {

    @get: Rule
    val composeTestRule = createComposeRule()

    @Before
    fun before() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens, onTabSelected = {}, currentScreen = RallyScreen.Accounts
            )
        }
    }

    @Test
    fun rallyTopAppBarTest() {
        Thread.sleep(500)
    }

    @Test
    fun rallyTopAppBarTest_currentTabSelected() {
        composeTestRule.onNodeWithContentDescription(RallyScreen.Accounts.name).assertIsSelected()
    }

    @Test
    fun rallyTopAppBarTest_currentLabelExists() {
        composeTestRule.onRoot().printToLog("currentLabelExists")
        composeTestRule.onNodeWithContentDescription(RallyScreen.Accounts.name).assertExists()
    }
}