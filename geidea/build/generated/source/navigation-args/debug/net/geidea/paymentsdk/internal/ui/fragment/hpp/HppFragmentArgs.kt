package net.geidea.paymentsdk.`internal`.ui.fragment.hpp

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

public data class HppFragmentArgs(
  public val finalPaymentData: PaymentData,
) : NavArgs {
  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toBundle(): Bundle {
    val result = Bundle()
    if (Parcelable::class.java.isAssignableFrom(PaymentData::class.java)) {
      result.putParcelable("finalPaymentData", this.finalPaymentData as Parcelable)
    } else if (Serializable::class.java.isAssignableFrom(PaymentData::class.java)) {
      result.putSerializable("finalPaymentData", this.finalPaymentData as Serializable)
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
      result.set("finalPaymentData", this.finalPaymentData as Parcelable)
    } else if (Serializable::class.java.isAssignableFrom(PaymentData::class.java)) {
      result.set("finalPaymentData", this.finalPaymentData as Serializable)
    } else {
      throw UnsupportedOperationException(PaymentData::class.java.name +
          " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  public companion object {
    @JvmStatic
    @Suppress("DEPRECATION")
    public fun fromBundle(bundle: Bundle): HppFragmentArgs {
      bundle.setClassLoader(HppFragmentArgs::class.java.classLoader)
      val __finalPaymentData : PaymentData?
      if (bundle.containsKey("finalPaymentData")) {
        if (Parcelable::class.java.isAssignableFrom(PaymentData::class.java) ||
            Serializable::class.java.isAssignableFrom(PaymentData::class.java)) {
          __finalPaymentData = bundle.get("finalPaymentData") as PaymentData?
        } else {
          throw UnsupportedOperationException(PaymentData::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (__finalPaymentData == null) {
          throw IllegalArgumentException("Argument \"finalPaymentData\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"finalPaymentData\" is missing and does not have an android:defaultValue")
      }
      return HppFragmentArgs(__finalPaymentData)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): HppFragmentArgs {
      val __finalPaymentData : PaymentData?
      if (savedStateHandle.contains("finalPaymentData")) {
        if (Parcelable::class.java.isAssignableFrom(PaymentData::class.java) ||
            Serializable::class.java.isAssignableFrom(PaymentData::class.java)) {
          __finalPaymentData = savedStateHandle.get<PaymentData?>("finalPaymentData")
        } else {
          throw UnsupportedOperationException(PaymentData::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (__finalPaymentData == null) {
          throw IllegalArgumentException("Argument \"finalPaymentData\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"finalPaymentData\" is missing and does not have an android:defaultValue")
      }
      return HppFragmentArgs(__finalPaymentData)
    }
  }
}
