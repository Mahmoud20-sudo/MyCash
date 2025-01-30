package net.geidea.paymentsdk

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import kotlin.Suppress
import kotlin.jvm.JvmStatic
import net.geidea.paymentsdk.flow.pay.PaymentData

public data class GdNavigationArgs(
  public val paymentData: PaymentData,
) : NavArgs {
  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toBundle(): Bundle {
    val result = Bundle()
    if (Parcelable::class.java.isAssignableFrom(PaymentData::class.java)) {
      result.putParcelable("paymentData", this.paymentData as Parcelable)
    } else if (Serializable::class.java.isAssignableFrom(PaymentData::class.java)) {
      result.putSerializable("paymentData", this.paymentData as Serializable)
    } else {
      throw UnsupportedOperationException(PaymentData::class.java.name +
          " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    if (Parcelable::class.java.isAssignableFrom(PaymentData::class.java)) {
      result.set("paymentData", this.paymentData as Parcelable)
    } else if (Serializable::class.java.isAssignableFrom(PaymentData::class.java)) {
      result.set("paymentData", this.paymentData as Serializable)
    } else {
      throw UnsupportedOperationException(PaymentData::class.java.name +
          " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  public companion object {
    @JvmStatic
    @Suppress("DEPRECATION")
    public fun fromBundle(bundle: Bundle): GdNavigationArgs {
      bundle.setClassLoader(GdNavigationArgs::class.java.classLoader)
      val __paymentData : PaymentData?
      if (bundle.containsKey("paymentData")) {
        if (Parcelable::class.java.isAssignableFrom(PaymentData::class.java) ||
            Serializable::class.java.isAssignableFrom(PaymentData::class.java)) {
          __paymentData = bundle.get("paymentData") as PaymentData?
        } else {
          throw UnsupportedOperationException(PaymentData::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (__paymentData == null) {
          throw IllegalArgumentException("Argument \"paymentData\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"paymentData\" is missing and does not have an android:defaultValue")
      }
      return GdNavigationArgs(__paymentData)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): GdNavigationArgs {
      val __paymentData : PaymentData?
      if (savedStateHandle.contains("paymentData")) {
        if (Parcelable::class.java.isAssignableFrom(PaymentData::class.java) ||
            Serializable::class.java.isAssignableFrom(PaymentData::class.java)) {
          __paymentData = savedStateHandle.get<PaymentData?>("paymentData")
        } else {
          throw UnsupportedOperationException(PaymentData::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (__paymentData == null) {
          throw IllegalArgumentException("Argument \"paymentData\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"paymentData\" is missing and does not have an android:defaultValue")
      }
      return GdNavigationArgs(__paymentData)
    }
  }
}
