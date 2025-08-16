package code.name.monkey.retromusic.fragments.artists

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.String
import kotlin.jvm.JvmStatic

public data class AlbumArtistDetailsFragmentArgs(
  public val extraArtistName: String,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("extra_artist_name", this.extraArtistName)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("extra_artist_name", this.extraArtistName)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): AlbumArtistDetailsFragmentArgs {
      bundle.setClassLoader(AlbumArtistDetailsFragmentArgs::class.java.classLoader)
      val __extraArtistName : String?
      if (bundle.containsKey("extra_artist_name")) {
        __extraArtistName = bundle.getString("extra_artist_name")
        if (__extraArtistName == null) {
          throw IllegalArgumentException("Argument \"extra_artist_name\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"extra_artist_name\" is missing and does not have an android:defaultValue")
      }
      return AlbumArtistDetailsFragmentArgs(__extraArtistName)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): AlbumArtistDetailsFragmentArgs {
      val __extraArtistName : String?
      if (savedStateHandle.contains("extra_artist_name")) {
        __extraArtistName = savedStateHandle["extra_artist_name"]
        if (__extraArtistName == null) {
          throw IllegalArgumentException("Argument \"extra_artist_name\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"extra_artist_name\" is missing and does not have an android:defaultValue")
      }
      return AlbumArtistDetailsFragmentArgs(__extraArtistName)
    }
  }
}
