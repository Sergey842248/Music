package code.name.monkey.retromusic.fragments.albums

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Long
import kotlin.jvm.JvmStatic

public data class AlbumDetailsFragmentArgs(
  public val extraAlbumId: Long,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putLong("extra_album_id", this.extraAlbumId)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("extra_album_id", this.extraAlbumId)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): AlbumDetailsFragmentArgs {
      bundle.setClassLoader(AlbumDetailsFragmentArgs::class.java.classLoader)
      val __extraAlbumId : Long
      if (bundle.containsKey("extra_album_id")) {
        __extraAlbumId = bundle.getLong("extra_album_id")
      } else {
        throw IllegalArgumentException("Required argument \"extra_album_id\" is missing and does not have an android:defaultValue")
      }
      return AlbumDetailsFragmentArgs(__extraAlbumId)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): AlbumDetailsFragmentArgs {
      val __extraAlbumId : Long?
      if (savedStateHandle.contains("extra_album_id")) {
        __extraAlbumId = savedStateHandle["extra_album_id"]
        if (__extraAlbumId == null) {
          throw IllegalArgumentException("Argument \"extra_album_id\" of type long does not support null values")
        }
      } else {
        throw IllegalArgumentException("Required argument \"extra_album_id\" is missing and does not have an android:defaultValue")
      }
      return AlbumDetailsFragmentArgs(__extraAlbumId)
    }
  }
}
