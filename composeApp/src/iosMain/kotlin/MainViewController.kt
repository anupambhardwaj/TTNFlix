import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import io.ktor.client.engine.darwin.*
import ui.network.ApiServiceClient

fun MainViewController() = ComposeUIViewController {
    App(
        client = remember {
            ApiServiceClient(createHttpClient(Darwin.create()))
        }
    )
}