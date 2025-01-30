package net.geidea.paymentsdk.`internal`.ui.fragment.card.form

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import java.math.BigDecimal
import kotlin.Suppress
import kotlin.jvm.JvmStatic
import net.geidea.paymentsdk.`internal`.ui.widget.Step

public data class CardInputFragmentArgs(
  public val step: Step?,
  public val downPaymentAmount: BigDecimal? = null,
) : NavArgs {
  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toBundle(): Bundle {
    val result = Bundle()
    if (Parcelable::class.java.isAssignableFrom(BigDecimal::class.java)) {
      result.putParcelable("downPaymentAmount", this.downPaymentAmount as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(BigDecimal::class.java)) {
      result.putSerializable("downPaymentAmount", this.downPaymentAmount as Serializable?)
    }
    if (Parcelable::class.java.isAssignableFrom(Step::class.java)) {
      result.putParcelable("step", this.step as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(Step::class.java)) {
      result.putSerializable("step", this.step as Serializable?)
    } else {
      throw UnsupportedOperationException(Step::class.java.name +
          " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    if (Parcelable::class.java.isAssignableFrom(BigDecimal::class.java)) {
      result.set("downPaymentAmount", this.downPaymentAmount as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(BigDecimal::class.java)) {
      result.set("downPaymentAmount", this.downPaymentAmount as Serializable?)
    }
    if (Parcelable::class.java.isAssignableFrom(Step::class.java)) {
      result.set("step", this.step as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(Step::class.java)) {
      result.set("step", this.step as Serializable?)
    } else {
      throw UnsupportedOperationException(Step::class.java.name +
          " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  public companion object {
    @JvmStatic
    @Suppress("DEPRECATION")
    public fun fromBundle(bundle: Bundle): CardInputFragmentArgs {
      bundle.setClassLoader(CardInputFragmentArgs::class.java.classLoader)
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
        throw IllegalArgumentException("Required argument \"step\" is missing and does not have an android:defaultValue")
      }
      return CardInputFragmentArgs(__step, __downPaymentAmount)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): CardInputFragmentArgs {
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
        throw IllegalArgumentException("Required argument \"step\" is missing and does not have an android:defaultValue")
      }
      return CardInputFragmentArgs(__step, __downPaymentAmount)
    }
  }
}
