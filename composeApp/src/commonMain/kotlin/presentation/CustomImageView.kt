package presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage

@Composable
fun CustomImageView(
    modifier: Modifier = Modifier, imageUrl: String, imageOptions: ImageOptions = ImageOptions(
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center
    )
) {

//    val palette = rememberPaletteState(null)

    CoilImage(
        modifier = Modifier
            .then(modifier),
        imageModel = {
            imageUrl
        },
        imageOptions = imageOptions,
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        },
        failure = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Could not load Image!")
            }
        }
    )
}