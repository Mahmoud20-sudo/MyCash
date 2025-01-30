package net.geidea.paymentsdk.`internal`.ui.fragment.qr.r2p

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.String
import kotlin.jvm.JvmStatic

public data class MeezaQrRequestPaymentFragmentArgs(
  public val qrMessage: String,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("qrMessage", this.qrMessage)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("qrMessage", this.qrMessage)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): MeezaQrRequestPaymentFragmentArgs {
      bundle.setClassLoader(MeezaQrRequestPaymentFragmentArgs::class.java.classLoader)
      val __qrMessage : String?
      if (bundle.containsKey("qrMessage")) {
        __qrMessage = bundle.getString("qrMessage")
        if (__qrMessage == null) {
          throw IllegalArgumentException("Argument \"qrMessage\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"qrMessage\" is missing and does not have an android:defaultValue")
      }
      return MeezaQrRequestPaymentFragmentArgs(__qrMessage)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle):
        MeezaQrRequestPaymentFragmentArgs {
      val __qrMessage : String?
      if (savedStateHandle.contains("qrMessage")) {
        __qrMessage = savedStateHandle["qrMessage"]
        if (__qrMessage == null) {
          throw IllegalArgumentException("Argument \"qrMessage\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"qrMessage\" is missing and does not have an android:defaultValue")
      }
      return MeezaQrRequestPaymentFragmentArgs(__qrMessage)
    }
  }
}
