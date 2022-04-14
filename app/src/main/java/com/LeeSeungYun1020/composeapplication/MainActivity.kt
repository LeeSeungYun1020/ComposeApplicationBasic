package com.LeeSeungYun1020.composeapplication

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.LeeSeungYun1020.composeapplication.ui.theme.ComposeApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeApplicationTheme {
                ImageList()
            }
        }
    }
}

@Composable
fun MyApp() {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "LayoutsCodelab") }, actions = {
            IconButton(onClick = { }) {
                Icon(Icons.Filled.Favorite, contentDescription = "favorite")
            }
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Star, contentDescription = "star")
            }
        })
    }) { innerPadding ->
        BodyContent(
            Modifier
                .padding(innerPadding)
                .padding(8.dp)
        )
    }
}

@Composable
fun SimpleList() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        repeat(100) {
            Text("Item #$it")
        }
    }
}

@Composable
fun LazyList() {
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState) {
        items(100) {
            Text(text = "Item #$it")
        }
    }
}

@Composable
fun ImageListItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberImagePainter(
                data = "https://developer.android.com/images/brand/Android_Robot.png"
            ), contentDescription = "Android Logo", modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}

@Composable
fun ImageList() {
    val listSize = 100
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }, modifier = Modifier
                .weight(1f)
                .padding(4.dp)) {
                Text(text = "Scroll to the top")
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(listSize - 1)
                    }
                }, modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            ) {
                Text(text = "Scroll to the end")
            }
        }
        LazyColumn(state = scrollState) {
            items(listSize) {
                ImageListItem(index = it)
            }
        }
    }
}

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = this.then(layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
    val firstBaseline = placeable[FirstBaseline]

    val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
    val height = placeable.height + placeableY

    layout(placeable.width, height) {
        placeable.placeRelative(0, placeableY)
    }
})

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier, content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier, content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        var yPosition = 0
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = yPosition)
                yPosition += placeable.height
            }
        }
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    MyOwnColumn(modifier.padding(8.dp)) {
        Text("MyOwnColumn")
        Text("places items")
        Text("vertically.")
        Text("We've done it by hand!")
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeApplicationTheme {
        BodyContent()
    }
}