package com.macdevelopers.composetaskapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    fontWeight: FontWeight? = null,
    color: Color = Color.Unspecified,
    onClick: (() -> Unit)? = null
) {
    Text(
        text = text,
        modifier = if (onClick != null) modifier.clickable { onClick() } else modifier,
        style = style,
        fontWeight = fontWeight,
        color = color
    )
}
