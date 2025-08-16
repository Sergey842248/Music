package code.name.monkey.retromusic

import android.content.*
import android.media.AudioManager
import android.os.IBinder
import android.view.KeyEvent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import code.name.monkey.retromusic.service.MusicService
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService

class MusicMessageService : WearableListenerService(), ServiceConnection {

    private lateinit var audioManager: AudioManager
    private var musicService: MusicService? = null
    private var isBound = false

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                MusicService.PLAY_STATE_CHANGED -> {
                    musicService?.let {
                        sendMessage("/is_playing", it.isPlaying.toString())
                    }
                }
                MusicService.META_CHANGED -> {
                    musicService?.let {
                        sendMessage("/is_playing", it.isPlaying.toString())
                        sendMessage("/song_title", it.currentSong.title)
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, Context.BIND_AUTO_CREATE)

        val filter = IntentFilter()
        filter.addAction(MusicService.PLAY_STATE_CHANGED)
        filter.addAction(MusicService.META_CHANGED)
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(this)
            isBound = false
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        when (messageEvent.path) {
            "/play_pause" -> {
                val downEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
                audioManager.dispatchMediaKeyEvent(downEvent)
                val upEvent = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
                audioManager.dispatchMediaKeyEvent(upEvent)
            }
            "/volume_up" -> {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI)
            }
            "/volume_down" -> {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI)
            }
            "/request_update" -> {
                musicService?.let {
                    sendMessage("/is_playing", it.isPlaying.toString())
                    sendMessage("/song_title", it.currentSong.title)
                }
            }
        }
    }

    private fun sendMessage(path: String, message: String) {
        Wearable.getNodeClient(this).connectedNodes.addOnSuccessListener { nodes ->
            for (node in nodes) {
                Wearable.getMessageClient(this).sendMessage(node.id, path, message.toByteArray())
            }
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        musicService = (service as MusicService.MusicBinder).service
        isBound = true
        musicService?.let {
            sendMessage("/is_playing", it.isPlaying.toString())
            sendMessage("/song_title", it.currentSong.title)
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
        isBound = false
    }
}
