/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.LeeSeungYun1020.composeapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.LeeSeungYun1020.composeapplication.data.UserData
import com.LeeSeungYun1020.composeapplication.ui.accounts.AccountsBody
import com.LeeSeungYun1020.composeapplication.ui.accounts.SingleAccountBody
import com.LeeSeungYun1020.composeapplication.ui.bills.BillsBody
import com.LeeSeungYun1020.composeapplication.ui.components.RallyTabRow
import com.LeeSeungYun1020.composeapplication.ui.overview.OverviewBody
import com.LeeSeungYun1020.composeapplication.ui.theme.RallyTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyApp()
        }
    }
}

@Composable
fun RallyApp() {
    RallyTheme {
        val allScreens = RallyScreen.values().toList()
        val navController = rememberNavController()
        val backStackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = RallyScreen.fromRoute(
            backStackEntry.value?.destination?.route
        )
        Scaffold(topBar = {
            RallyTabRow(
                allScreens = allScreens,
                onTabSelected = { screen -> navController.navigate(screen.name) },
                currentScreen = currentScreen
            )
        }) { innerPadding ->
            RallyNavHost(
                navController = navController, modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun RallyNavHost(
    navController: NavHostController, modifier: Modifier = Modifier
) {
    val accountsName = RallyScreen.Accounts.name

    NavHost(
        navController = navController,
        startDestination = RallyScreen.Overview.name,
        modifier = modifier
    ) {
        composable(RallyScreen.Overview.name) {
            OverviewBody(onClickSeeAllAccounts = {
                navController.navigate(RallyScreen.Accounts.name)
            }, onClickSeeAllBills = {
                navController.navigate(RallyScreen.Bills.name)
            }, onAccountClick = { name ->
                navigateToSingleAccount(navController, name)
            })
        }
        composable(RallyScreen.Accounts.name) {
            AccountsBody(accounts = UserData.accounts) { name ->
                navigateToSingleAccount(navController, name)
            }
        }
        composable(RallyScreen.Bills.name) {
            BillsBody(bills = UserData.bills)
        }
        composable(route = "$accountsName/{name}", arguments = listOf(navArgument("name") {
            type = NavType.StringType
        }), deepLinks = listOf(navDeepLink { uriPattern = "rally://$accountsName/{name}" })
        ) { entry ->
            val accountName = entry.arguments?.getString("name")
            val account = UserData.getAccount(accountName)
            SingleAccountBody(account = account)
        }
    }
}

private fun navigateToSingleAccount(
    navController: NavHostController, accountName: String
) {
    navController.navigate("${RallyScreen.Accounts.name}/$accountName")
}