package com.macdevelopers.composetaskapp.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.macdevelopers.composetaskapp.ui.theme.CardBackgroundWhite
import com.macdevelopers.composetaskapp.ui.theme.Dimens

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = CardBackgroundWhite,
    shape: Shape = RoundedCornerShape(Dimens.CardCornerRadius),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        content()
    }
}
