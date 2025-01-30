package net.geidea.paymentsdk.`internal`.ui.fragment.card.auth

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import kotlin.String
import kotlin.Suppress
import kotlin.jvm.JvmStatic
import net.geidea.paymentsdk.`internal`.ui.widget.Step
import net.geidea.paymentsdk.flow.pay.PaymentData

public data class CardAuthFragmentArgs(
  public val finalPaymentData: PaymentData,
  public val step: Step? = null,
  public val cvv: String? = null,
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
    if (Parcelable::class.java.isAssignableFrom(Step::class.java)) {
      result.putParcelable("step", this.step as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(Step::class.java)) {
      result.putSerializable("step", this.step as Serializable?)
    }
    result.putString("cvv", this.cvv)
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
    if (Parcelable::class.java.isAssignableFrom(Step::class.java)) {
      result.set("step", this.step as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(Step::class.java)) {
      result.set("step", this.step as Serializable?)
    }
    result.set("cvv", this.cvv)
    return result
  }

  public companion object {
    @JvmStatic
    @Suppress("DEPRECATION")
    public fun fromBundle(bundle: Bundle): CardAuthFragmentArgs {
      bundle.setClassLoader(CardAuthFragmentArgs::class.java.classLoader)
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
      val __step : Step?
      if (bundle.containsKey("step")) {
        if (Parcelable::class.java.isAssignableFrom(Step::class.java) ||
            Serializable::class.java.isAssignableFrom(Step::class.java)) {
          __step = bundle.get("step") as Step?
        } else {
          throw UnsupportedOperationException(Step::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
      } else {
        __step = null
      }
      val __cvv : String?
      if (bundle.containsKey("cvv")) {
        __cvv = bundle.getString("cvv")
      } else {
        __cvv = null
      }
      return CardAuthFragmentArgs(__finalPaymentData, __step, __cvv)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): CardAuthFragmentArgs {
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
      val __step : Step?
      if (savedStateHandle.contains("step")) {
        if (Parcelable::class.java.isAssignableFrom(Step::class.java) ||
            Serializable::class.java.isAssignableFrom(Step::class.java)) {
          __step = savedStateHandle.get<Step?>("step")
        } else {
          throw UnsupportedOperationException(Step::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
      } else {
        __step = null
      }
      val __cvv : String?
      if (savedStateHandle.contains("cvv")) {
        __cvv = savedStateHandle["cvv"]
      } else {
        __cvv = null
      }
      return CardAuthFragmentArgs(__finalPaymentData, __step, __cvv)
    }
  }
}
