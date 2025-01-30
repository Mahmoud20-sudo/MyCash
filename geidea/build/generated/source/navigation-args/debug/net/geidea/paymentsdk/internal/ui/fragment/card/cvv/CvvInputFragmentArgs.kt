package net.geidea.paymentsdk.`internal`.ui.fragment.card.cvv

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.String
import kotlin.jvm.JvmStatic

public data class CvvInputFragmentArgs(
  public val tokenId: String,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("tokenId", this.tokenId)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("tokenId", this.tokenId)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): CvvInputFragmentArgs {
      bundle.setClassLoader(CvvInputFragmentArgs::class.java.classLoader)
      val __tokenId : String?
      if (bundle.containsKey("tokenId")) {
        __tokenId = bundle.getString("tokenId")
        if (__tokenId == null) {
          throw IllegalArgumentException("Argument \"tokenId\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"tokenId\" is missing and does not have an android:defaultValue")
      }
      return CvvInputFragmentArgs(__tokenId)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): CvvInputFragmentArgs {
      val __tokenId : String?
      if (savedStateHandle.contains("tokenId")) {
        __tokenId = savedStateHandle["tokenId"]
        if (__tokenId == null) {
          throw IllegalArgumentException("Argument \"tokenId\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"tokenId\" is missing and does not have an android:defaultValue")
      }
      return CvvInputFragmentArgs(__tokenId)
    }
  }
}
