package com.LeeSeungYun1020.composeapplication

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.LeeSeungYun1020.composeapplication.ui.theme.ComposeApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeApplicationTheme {

            }
        }
    }
}

@Composable
fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Text(
            text = text1,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start)
        )
        Divider(
            color = Color.Black, modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        // IntrinsicSize.Min으로 지정하였기 때문에 높이가 전체 화면을 차지하는 대신 텍스트와 맞춰짐
        Text(
            text = text2,
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End)
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeApplicationTheme {
        androidx.compose.material.Surface {
            TwoTexts(text1 = "Hi", text2 = "there")
        }
    }
}