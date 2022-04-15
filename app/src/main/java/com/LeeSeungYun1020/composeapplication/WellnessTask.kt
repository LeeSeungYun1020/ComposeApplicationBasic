package com.LeeSeungYun1020.composeapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class WellnessTask(val id: Int, val label: String, var initiallyChecked: Boolean = false) {
    var checked by mutableStateOf(initiallyChecked)
}
