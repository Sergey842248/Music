package code.name.monkey.retromusic.fragments.other

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Int
import kotlin.jvm.JvmStatic

public data class DetailListFragmentArgs(
  public val type: Int,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putInt("type", this.type)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("type", this.type)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): DetailListFragmentArgs {
      bundle.setClassLoader(DetailListFragmentArgs::class.java.classLoader)
      val __type : Int
      if (bundle.containsKey("type")) {
        __type = bundle.getInt("type")
      } else {
        throw IllegalArgumentException("Required argument \"type\" is missing and does not have an android:defaultValue")
      }
      return DetailListFragmentArgs(__type)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): DetailListFragmentArgs {
      val __type : Int?
      if (savedStateHandle.contains("type")) {
        __type = savedStateHandle["type"]
        if (__type == null) {
          throw IllegalArgumentException("Argument \"type\" of type integer does not support null values")
        }
      } else {
        throw IllegalArgumentException("Required argument \"type\" is missing and does not have an android:defaultValue")
      }
      return DetailListFragmentArgs(__type)
    }
  }
}
