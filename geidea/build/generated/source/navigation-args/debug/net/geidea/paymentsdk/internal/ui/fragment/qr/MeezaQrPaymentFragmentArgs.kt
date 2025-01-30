package net.geidea.paymentsdk.`internal`.ui.fragment.qr

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import java.math.BigDecimal
import kotlin.String
import kotlin.Suppress
import kotlin.jvm.JvmStatic
import net.geidea.paymentsdk.`internal`.ui.widget.Step

public data class MeezaQrPaymentFragmentArgs(
  public val merchantName: String?,
  public val qrMessage: String,
  public val qrCodeImageBase64: String,
  public val paymentIntentId: String,
  public val downPaymentAmount: BigDecimal? = null,
  public val step: Step? = null,
) : NavArgs {
  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("merchantName", this.merchantName)
    if (Parcelable::class.java.isAssignableFrom(BigDecimal::class.java)) {
      result.putParcelable("downPaymentAmount", this.downPaymentAmount as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(BigDecimal::class.java)) {
      result.putSerializable("downPaymentAmount", this.downPaymentAmount as Serializable?)
    }
    if (Parcelable::class.java.isAssignableFrom(Step::class.java)) {
      result.putParcelable("step", this.step as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(Step::class.java)) {
      result.putSerializable("step", this.step as Serializable?)
    }
    result.putString("qrMessage", this.qrMessage)
    result.putString("qrCodeImageBase64", this.qrCodeImageBase64)
    result.putString("paymentIntentId", this.paymentIntentId)
    return result
  }

  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("merchantName", this.merchantName)
    if (Parcelable::class.java.isAssignableFrom(BigDecimal::class.java)) {
      result.set("downPaymentAmount", this.downPaymentAmount as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(BigDecimal::class.java)) {
      result.set("downPaymentAmount", this.downPaymentAmount as Serializable?)
    }
    if (Parcelable::class.java.isAssignableFrom(Step::class.java)) {
      result.set("step", this.step as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(Step::class.java)) {
      result.set("step", this.step as Serializable?)
    }
    result.set("qrMessage", this.qrMessage)
    result.set("qrCodeImageBase64", this.qrCodeImageBase64)
    result.set("paymentIntentId", this.paymentIntentId)
    return result
  }

  public companion object {
    @JvmStatic
    @Suppress("DEPRECATION")
    public fun fromBundle(bundle: Bundle): MeezaQrPaymentFragmentArgs {
      bundle.setClassLoader(MeezaQrPaymentFragmentArgs::class.java.classLoader)
      val __merchantName : String?
      if (bundle.containsKey("merchantName")) {
        __merchantName = bundle.getString("merchantName")
      } else {
        throw IllegalArgumentException("Required argument \"merchantName\" is missing and does not have an android:defaultValue")
      }
      val __downPaymentAmount : BigDecimal?
      if (bundle.containsKey("downPaymentAmount")) {
        if (Parcelable::class.java.isAssignableFrom(BigDecimal::class.java) ||
            Serializable::class.java.isAssignableFrom(BigDecimal::class.java)) {
          __downPaymentAmount = bundle.get("downPaymentAmount") as BigDecimal?
        } else {
          throw UnsupportedOperationException(BigDecimal::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
      } else {
        __downPaymentAmount = null
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
      val __qrMessage : String?
      if (bundle.containsKey("qrMessage")) {
        __qrMessage = bundle.getString("qrMessage")
        if (__qrMessage == null) {
          throw IllegalArgumentException("Argument \"qrMessage\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"qrMessage\" is missing and does not have an android:defaultValue")
      }
      val __qrCodeImageBase64 : String?
      if (bundle.containsKey("qrCodeImageBase64")) {
        __qrCodeImageBase64 = bundle.getString("qrCodeImageBase64")
        if (__qrCodeImageBase64 == null) {
          throw IllegalArgumentException("Argument \"qrCodeImageBase64\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"qrCodeImageBase64\" is missing and does not have an android:defaultValue")
      }
      val __paymentIntentId : String?
      if (bundle.containsKey("paymentIntentId")) {
        __paymentIntentId = bundle.getString("paymentIntentId")
        if (__paymentIntentId == null) {
          throw IllegalArgumentException("Argument \"paymentIntentId\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"paymentIntentId\" is missing and does not have an android:defaultValue")
      }
      return MeezaQrPaymentFragmentArgs(__merchantName, __qrMessage, __qrCodeImageBase64,
          __paymentIntentId, __downPaymentAmount, __step)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle):
        MeezaQrPaymentFragmentArgs {
      val __merchantName : String?
      if (savedStateHandle.contains("merchantName")) {
        __merchantName = savedStateHandle["merchantName"]
      } else {
        throw IllegalArgumentException("Required argument \"merchantName\" is missing and does not have an android:defaultValue")
      }
      val __downPaymentAmount : BigDecimal?
      if (savedStateHandle.contains("downPaymentAmount")) {
        if (Parcelable::class.java.isAssignableFrom(BigDecimal::class.java) ||
            Serializable::class.java.isAssignableFrom(BigDecimal::class.java)) {
          __downPaymentAmount = savedStateHandle.get<BigDecimal?>("downPaymentAmount")
        } else {
          throw UnsupportedOperationException(BigDecimal::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
      } else {
        __downPaymentAmount = null
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
      val __qrMessage : String?
      if (savedStateHandle.contains("qrMessage")) {
        __qrMessage = savedStateHandle["qrMessage"]
        if (__qrMessage == null) {
          throw IllegalArgumentException("Argument \"qrMessage\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"qrMessage\" is missing and does not have an android:defaultValue")
      }
      val __qrCodeImageBase64 : String?
      if (savedStateHandle.contains("qrCodeImageBase64")) {
        __qrCodeImageBase64 = savedStateHandle["qrCodeImageBase64"]
        if (__qrCodeImageBase64 == null) {
          throw IllegalArgumentException("Argument \"qrCodeImageBase64\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"qrCodeImageBase64\" is missing and does not have an android:defaultValue")
      }
      val __paymentIntentId : String?
      if (savedStateHandle.contains("paymentIntentId")) {
        __paymentIntentId = savedStateHandle["paymentIntentId"]
        if (__paymentIntentId == null) {
          throw IllegalArgumentException("Argument \"paymentIntentId\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"paymentIntentId\" is missing and does not have an android:defaultValue")
      }
      return MeezaQrPaymentFragmentArgs(__merchantName, __qrMessage, __qrCodeImageBase64,
          __paymentIntentId, __downPaymentAmount, __step)
    }
  }
}
