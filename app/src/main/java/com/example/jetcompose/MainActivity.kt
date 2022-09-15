package com.example.jetcompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetcompose.compose.ConversionMenu
import com.example.jetcompose.compose.DisplayTvShow
import com.example.jetcompose.model.TvShow
import com.example.jetcompose.ui.theme.JETCOMPOSETheme
import com.example.jetcompose.utils.intenToViewDetails

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JETCOMPOSETheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    /* LazyColumnDemo {
                         Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                     }*/
                    ConversionMenu(
                        list = TvShow.tvShowList(),
                        modifier = Modifier
                    )
                   /* DisplayTvShow(selectedItem = {
                        startActivity(intenToViewDetails(this, it))
                        Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
                    })*/
                }
            }
        }
    }
}

@Composable
fun LazyColumnDemo(selectedItem: (String) -> Unit) {
    LazyColumn {
        items(100) {
            Text(
                text = "User at $it",
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable { selectedItem("$it Selected") }
            )
            Divider(color = Color.Black, thickness = 5.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JETCOMPOSETheme {
    }
}
