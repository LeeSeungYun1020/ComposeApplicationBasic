package com.LeeSeungYun1020.composeapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WellnessScreen(
    modifier: Modifier = Modifier,
    wellnessViewModel: WellnessViewModel = viewModel()
) {
    Column(modifier = modifier) {
        StatefulCounter()
        WellnessTasksList(list = wellnessViewModel.tasks, onCheckedTask = { task, changed ->
            wellnessViewModel.changeTaskChanged(task, changed)
        }, onCloseTask = { task -> wellnessViewModel.remove(task) })
    }
}