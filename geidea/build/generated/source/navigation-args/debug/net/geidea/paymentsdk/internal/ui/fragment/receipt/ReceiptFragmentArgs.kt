package net.geidea.paymentsdk.`internal`.ui.fragment.receipt

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import kotlin.Suppress
import kotlin.jvm.JvmStatic

public data class ReceiptFragmentArgs(
  public val args: ReceiptArgs,
) : NavArgs {
  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toBundle(): Bundle {
    val result = Bundle()
    if (Parcelable::class.java.isAssignableFrom(ReceiptArgs::class.java)) {
      result.putParcelable("args", this.args as Parcelable)
    } else if (Serializable::class.java.isAssignableFrom(ReceiptArgs::class.java)) {
      result.putSerializable("args", this.args as Serializable)
    } else {
      throw UnsupportedOperationException(ReceiptArgs::class.java.name +
          " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    if (Parcelable::class.java.isAssignableFrom(ReceiptArgs::class.java)) {
      result.set("args", this.args as Parcelable)
    } else if (Serializable::class.java.isAssignableFrom(ReceiptArgs::class.java)) {
      result.set("args", this.args as Serializable)
    } else {
      throw UnsupportedOperationException(ReceiptArgs::class.java.name +
          " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  public companion object {
    @JvmStatic
    @Suppress("DEPRECATION")
    public fun fromBundle(bundle: Bundle): ReceiptFragmentArgs {
      bundle.setClassLoader(ReceiptFragmentArgs::class.java.classLoader)
      val __args : ReceiptArgs?
      if (bundle.containsKey("args")) {
        if (Parcelable::class.java.isAssignableFrom(ReceiptArgs::class.java) ||
            Serializable::class.java.isAssignableFrom(ReceiptArgs::class.java)) {
          __args = bundle.get("args") as ReceiptArgs?
        } else {
          throw UnsupportedOperationException(ReceiptArgs::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (__args == null) {
          throw IllegalArgumentException("Argument \"args\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"args\" is missing and does not have an android:defaultValue")
      }
      return ReceiptFragmentArgs(__args)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): ReceiptFragmentArgs {
      val __args : ReceiptArgs?
      if (savedStateHandle.contains("args")) {
        if (Parcelable::class.java.isAssignableFrom(ReceiptArgs::class.java) ||
            Serializable::class.java.isAssignableFrom(ReceiptArgs::class.java)) {
          __args = savedStateHandle.get<ReceiptArgs?>("args")
        } else {
          throw UnsupportedOperationException(ReceiptArgs::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (__args == null) {
          throw IllegalArgumentException("Argument \"args\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"args\" is missing and does not have an android:defaultValue")
      }
      return ReceiptFragmentArgs(__args)
    }
  }
}
