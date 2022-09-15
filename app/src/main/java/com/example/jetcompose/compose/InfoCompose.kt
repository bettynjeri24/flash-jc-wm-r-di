package com.example.jetcompose.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.jetcompose.model.TvShow

@Composable
fun ViewMoreTvShowInfo(tvShow: TvShow) {
    val scrollState = rememberScrollState()
    Card(
        modifier = Modifier.padding(10.dp),
        elevation = 10.dp,
        shape = RoundedCornerShape(corner = CornerSize(10.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(10.dp)
        ) {
            //
            Image(
                painter = painterResource(id = tvShow.image),
                contentDescription = tvShow.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Fit
            )
            //
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = tvShow.name, style = MaterialTheme.typography.h3)
            //
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = tvShow.overView, style = MaterialTheme.typography.h5)
            //
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Original release :${tvShow.name}", style = MaterialTheme.typography.h5)
            //
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "IMDB : ${tvShow.rating}", style = MaterialTheme.typography.h5)
        }
    }
}

@Composable
fun ConversionMenu(list: List<TvShow>, modifier: Modifier = Modifier) {
    var displayingText by remember { mutableStateOf("Select Movie") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }

    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Column {
        OutlinedTextField(
            value = displayingText,
            onValueChange = { displayingText = it },
            textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            modifier = modifier
                .fillMaxWidth()
                .onGloballyPositioned { cordinates ->
                    textFieldSize = cordinates.size.toSize()
                },
            label = { Text(text = "Conversion type") },
            trailingIcon = {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier.clickable { expanded = !expanded }
                )
            },
            readOnly = true
        )

        //
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = modifier.width(
                with(LocalDensity.current) {
                    textFieldSize.width.toDp()
                }
            )
        ) {
            list.forEach { tvShow ->
                DropdownMenuItem(onClick = {
                    displayingText = tvShow.name
                    expanded = false
                }) {
                    Text(
                        text = tvShow.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
