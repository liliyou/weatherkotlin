package com.example.search.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.search.presentation.theme.SearchBarBackground
import com.example.search.presentation.theme.SearchTextPrimary

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "輸入完整城市名（如：台北市）",
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(SearchBarBackground)
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            if (query.isEmpty()) {
                Text(
                    text = placeholder,
                    color = SearchTextPrimary.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
            if (!readOnly) {
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = SearchTextPrimary,
                        fontSize = 14.sp
                    ),
                    cursorBrush = SolidColor(SearchTextPrimary),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "搜尋",
            tint = SearchTextPrimary,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E2A47)
@Composable
private fun SearchBarPreview() {
    SearchBar(
        query = "",
        onQueryChange = {},
        onSearch = {},
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF1E2A47)
@Composable
private fun SearchBarWithTextPreview() {
    SearchBar(
        query = "台北",
        onQueryChange = {},
        onSearch = {},
        modifier = Modifier.padding(16.dp)
    )
}
