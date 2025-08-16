package code.name.monkey.retromusic.fragments.playlists

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Long
import kotlin.jvm.JvmStatic

public data class PlaylistDetailsFragmentArgs(
  public val extraPlaylistId: Long,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putLong("extra_playlist_id", this.extraPlaylistId)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("extra_playlist_id", this.extraPlaylistId)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): PlaylistDetailsFragmentArgs {
      bundle.setClassLoader(PlaylistDetailsFragmentArgs::class.java.classLoader)
      val __extraPlaylistId : Long
      if (bundle.containsKey("extra_playlist_id")) {
        __extraPlaylistId = bundle.getLong("extra_playlist_id")
      } else {
        throw IllegalArgumentException("Required argument \"extra_playlist_id\" is missing and does not have an android:defaultValue")
      }
      return PlaylistDetailsFragmentArgs(__extraPlaylistId)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): PlaylistDetailsFragmentArgs {
      val __extraPlaylistId : Long?
      if (savedStateHandle.contains("extra_playlist_id")) {
        __extraPlaylistId = savedStateHandle["extra_playlist_id"]
        if (__extraPlaylistId == null) {
          throw IllegalArgumentException("Argument \"extra_playlist_id\" of type long does not support null values")
        }
      } else {
        throw IllegalArgumentException("Required argument \"extra_playlist_id\" is missing and does not have an android:defaultValue")
      }
      return PlaylistDetailsFragmentArgs(__extraPlaylistId)
    }
  }
}
