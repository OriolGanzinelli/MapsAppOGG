package com.example.mapsappogg.models

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.mapsappogg.viewmodel.MyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(myViewModel: MyViewModel) {
    val searchText by myViewModel.searchText.observeAsState("")
    SearchBar(
        query = searchText,
        onQueryChange = {
            myViewModel.onSearchTextChange(it)
        },
        active = true,
        onActiveChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f),
        colors = SearchBarDefaults.colors(
            containerColor = Color.White,
            dividerColor = Color.LightGray,
            inputFieldColors = TextFieldDefaults.colors(
                Color.Black
            )
        ),
        onSearch = { },
        placeholder = {
            Text(text = "SEARCH MARKERS")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Refresh, contentDescription = "DELETE QUERY",
                tint = Color.Black,
                modifier = Modifier.clickable {
                    myViewModel.onSearchTextChange("")
                    myViewModel.getSavedMarkers()
                }
            )
        }
    )
    { }
}