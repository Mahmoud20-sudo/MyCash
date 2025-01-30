package net.geidea.paymentsdk.`internal`.ui.fragment.bnpl.shahry.confirm

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.String
import kotlin.jvm.JvmStatic

public data class ShahryConfirmFragmentArgs(
  public val customerIdentifier: String,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("customerIdentifier", this.customerIdentifier)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("customerIdentifier", this.customerIdentifier)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): ShahryConfirmFragmentArgs {
      bundle.setClassLoader(ShahryConfirmFragmentArgs::class.java.classLoader)
      val __customerIdentifier : String?
      if (bundle.containsKey("customerIdentifier")) {
        __customerIdentifier = bundle.getString("customerIdentifier")
        if (__customerIdentifier == null) {
          throw IllegalArgumentException("Argument \"customerIdentifier\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"customerIdentifier\" is missing and does not have an android:defaultValue")
      }
      return ShahryConfirmFragmentArgs(__customerIdentifier)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): ShahryConfirmFragmentArgs {
      val __customerIdentifier : String?
      if (savedStateHandle.contains("customerIdentifier")) {
        __customerIdentifier = savedStateHandle["customerIdentifier"]
        if (__customerIdentifier == null) {
          throw IllegalArgumentException("Argument \"customerIdentifier\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"customerIdentifier\" is missing and does not have an android:defaultValue")
      }
      return ShahryConfirmFragmentArgs(__customerIdentifier)
    }
  }
}
