package com.example.weatherkotlin.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherkotlin.ui.components.WeatherSearchBar
import com.example.weatherkotlin.ui.theme.WeatherBackground
import com.example.weatherkotlin.ui.theme.WeatherCardBackground
import com.example.weatherkotlin.ui.theme.WeatherTextPrimary
import com.example.weatherkotlin.ui.theme.WeatherTextSecondary
import com.example.weatherkotlin.ui.theme.WeatherkotlinTheme

data class SearchResult(
    val cityName: String,
    val lat: Double,
    val lon: Double
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    searchResults: List<SearchResult>,
    suggestedCities: List<String>,
    onResultClick: (SearchResult) -> Unit,
    onSuggestedClick: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WeatherBackground)
    ) {
        WeatherSearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            showCloseButton = true,
            onClose = onClose,
            modifier = Modifier.padding(16.dp)
        )

        if (query.isEmpty()) {
            // 顯示建議地點 chips
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                suggestedCities.forEach { city ->
                    SuggestedCityChip(
                        cityName = city,
                        onClick = { onSuggestedClick(city) }
                    )
                }
            }
        } else {
            // 顯示搜尋結果列表
            LazyColumn {
                items(searchResults) { result ->
                    SearchResultItem(
                        result = result,
                        onClick = { onResultClick(result) }
                    )
                    HorizontalDivider(
                        color = WeatherTextSecondary.copy(alpha = 0.3f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestedCityChip(
    cityName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = WeatherCardBackground
    ) {
        Text(
            text = cityName,
            color = WeatherTextPrimary,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun SearchResultItem(
    result: SearchResult,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = result.cityName,
        color = WeatherTextPrimary,
        fontSize = 16.sp,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    WeatherkotlinTheme {
        SearchScreen(
            query = "台北",
            onQueryChange = {},
            onSearch = {},
            searchResults = listOf(
                SearchResult("台北", 25.0330, 121.5654),
                SearchResult("台北市", 25.0330, 121.5654)
            ),
            suggestedCities = listOf("桃園", "高雄", "台中", "新竹"),
            onResultClick = {},
            onSuggestedClick = {},
            onClose = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenEmptyPreview() {
    WeatherkotlinTheme {
        SearchScreen(
            query = "",
            onQueryChange = {},
            onSearch = {},
            searchResults = emptyList(),
            suggestedCities = listOf("桃園", "高雄", "台中", "新竹"),
            onResultClick = {},
            onSuggestedClick = {},
            onClose = {}
        )
    }
}
