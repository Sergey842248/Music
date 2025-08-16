/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.future.music.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.future.music.R
import com.future.music.presentation.theme.MusicTheme

class MainActivity : ComponentActivity(), MessageClient.OnMessageReceivedListener {

    private lateinit var messageClient: MessageClient
    private val isPlaying = mutableStateOf(false)
    private val songTitle = mutableStateOf("No song playing")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        messageClient = Wearable.getMessageClient(this)

        setContent {
            WearApp(
                isPlaying = isPlaying.value,
                songTitle = songTitle.value,
                onPlayPause = {
                    isPlaying.value = !isPlaying.value
                    sendMessage("/play_pause", "")
                },
                onVolumeUp = {
                    sendMessage("/volume_up", "")
                },
                onVolumeDown = {
                    sendMessage("/volume_down", "")
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        Wearable.getMessageClient(this).addListener(this)
        sendMessage("/request_update", "")
    }

    override fun onPause() {
        super.onPause()
        Wearable.getMessageClient(this).removeListener(this)
    }

    private fun sendMessage(path: String, message: String) {
        Wearable.getNodeClient(this).connectedNodes.addOnSuccessListener { nodes ->
            for (node in nodes) {
                messageClient.sendMessage(node.id, path, message.toByteArray())
            }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        when (messageEvent.path) {
            "/is_playing" -> {
                isPlaying.value = String(messageEvent.data, Charsets.UTF_8).toBoolean()
            }
            "/song_title" -> {
                val title = String(messageEvent.data, Charsets.UTF_8)
                if (title.isNotEmpty()) {
                    songTitle.value = title
                }
            }
        }
    }
}

@Composable
fun WearApp(
    isPlaying: Boolean,
    songTitle: String,
    onPlayPause: () -> Unit,
    onVolumeUp: () -> Unit,
    onVolumeDown: () -> Unit
) {
    MusicTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = songTitle)
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onVolumeDown) {
                    Text("-")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = onPlayPause) {
                    Icon(
                        painter = rememberAsyncImagePainter(
                            if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                        ),
                        contentDescription = if (isPlaying) "Pause" else "Play"
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = onVolumeUp) {
                    Text("+")
                }
            }
        }
    }
}
