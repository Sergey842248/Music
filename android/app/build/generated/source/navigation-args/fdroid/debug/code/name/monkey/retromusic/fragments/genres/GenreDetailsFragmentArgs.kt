package code.name.monkey.retromusic.fragments.genres

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import code.name.monkey.retromusic.model.Genre
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import kotlin.Suppress
import kotlin.jvm.JvmStatic

public data class GenreDetailsFragmentArgs(
  public val extraGenre: Genre,
) : NavArgs {
  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toBundle(): Bundle {
    val result = Bundle()
    if (Parcelable::class.java.isAssignableFrom(Genre::class.java)) {
      result.putParcelable("extra_genre", this.extraGenre as Parcelable)
    } else if (Serializable::class.java.isAssignableFrom(Genre::class.java)) {
      result.putSerializable("extra_genre", this.extraGenre as Serializable)
    } else {
      throw UnsupportedOperationException(Genre::class.java.name + " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    if (Parcelable::class.java.isAssignableFrom(Genre::class.java)) {
      result.set("extra_genre", this.extraGenre as Parcelable)
    } else if (Serializable::class.java.isAssignableFrom(Genre::class.java)) {
      result.set("extra_genre", this.extraGenre as Serializable)
    } else {
      throw UnsupportedOperationException(Genre::class.java.name + " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  public companion object {
    @JvmStatic
    @Suppress("DEPRECATION")
    public fun fromBundle(bundle: Bundle): GenreDetailsFragmentArgs {
      bundle.setClassLoader(GenreDetailsFragmentArgs::class.java.classLoader)
      val __extraGenre : Genre?
      if (bundle.containsKey("extra_genre")) {
        if (Parcelable::class.java.isAssignableFrom(Genre::class.java) || Serializable::class.java.isAssignableFrom(Genre::class.java)) {
          __extraGenre = bundle.get("extra_genre") as Genre?
        } else {
          throw UnsupportedOperationException(Genre::class.java.name + " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (__extraGenre == null) {
          throw IllegalArgumentException("Argument \"extra_genre\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"extra_genre\" is missing and does not have an android:defaultValue")
      }
      return GenreDetailsFragmentArgs(__extraGenre)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): GenreDetailsFragmentArgs {
      val __extraGenre : Genre?
      if (savedStateHandle.contains("extra_genre")) {
        if (Parcelable::class.java.isAssignableFrom(Genre::class.java) || Serializable::class.java.isAssignableFrom(Genre::class.java)) {
          __extraGenre = savedStateHandle.get<Genre?>("extra_genre")
        } else {
          throw UnsupportedOperationException(Genre::class.java.name + " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (__extraGenre == null) {
          throw IllegalArgumentException("Argument \"extra_genre\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"extra_genre\" is missing and does not have an android:defaultValue")
      }
      return GenreDetailsFragmentArgs(__extraGenre)
    }
  }
}
