package com.LeeSeungYun1020.composeapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
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
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")
        composeTestRule.onNode(
            hasText(RallyScreen.Accounts.name.uppercase()) and hasParent(
                hasContentDescription(
                    RallyScreen.Accounts.name
                )
            ), useUnmergedTree = true
        ).assertExists()
    }

}