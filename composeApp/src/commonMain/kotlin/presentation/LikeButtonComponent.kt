package presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.LikeButtonComponent(
    modifier: Modifier = Modifier,
    alignment: Alignment,
    isLiked: Boolean,
    likeButtonClicked: (isLiked: Boolean) -> Unit
) {

    val isLikedState = remember {
        mutableStateOf(isLiked)
    }

    Box(
        modifier = Modifier
            .align(alignment)
            .then(modifier),
    ) {
        IconButton(onClick = {
            isLikedState.value = !isLikedState.value
            likeButtonClicked(isLikedState.value)
        }) {
            if (isLikedState.value) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Like Button",
                    tint = Color.Red
                )
            } else {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Like Button",
                    tint = Color.Red
                )
            }
        }
    }
}