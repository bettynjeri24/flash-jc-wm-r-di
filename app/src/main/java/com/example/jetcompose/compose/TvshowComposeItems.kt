package com.example.jetcompose.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.jetcompose.model.TvShow

@Composable
fun DisplayTvShow(selectedItem: (TvShow) -> Unit) {
    val tvShow = remember { TvShow.tvShowList() }
    LazyColumn(contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)) {
        items(
            items = tvShow,
            itemContent = { TvShowListItem(tvShow = it, selectedItem = selectedItem) }
        )
    }
}

@Composable
fun TvShowListItem(tvShow: TvShow, selectedItem: (TvShow) -> Unit) {
    Card(
        modifier = Modifier.padding(10.dp).fillMaxWidth(),
        elevation = 10.dp,
        shape = RoundedCornerShape(corner = CornerSize(10.dp))
    ) {
        Row(
            modifier = Modifier.padding(5.dp).fillMaxWidth().clickable { selectedItem(tvShow) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            TvImage(tvShow = tvShow)
            Column {
                Text(
                    text = tvShow.name,
                    style = MaterialTheme.typography.h5
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tvShow.overView,
                    style = MaterialTheme.typography.body1,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = tvShow.year, style = MaterialTheme.typography.h5)
                    Text(text = tvShow.rating, style = MaterialTheme.typography.h5)
                }
            }
        }
    }
}

@Composable
fun TvImage(tvShow: TvShow) {
    Image(
        painter = painterResource(id = tvShow.image),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier.padding(4.dp).height(140.dp).width(100.dp).clip(
            RoundedCornerShape(corner = CornerSize(10.dp))
        )
    )
}
