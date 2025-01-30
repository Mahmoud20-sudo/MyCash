package net.geidea.paymentsdk.`internal`.ui.fragment.bnpl.valu.otp

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import kotlin.Suppress
import kotlin.jvm.JvmStatic
import net.geidea.paymentsdk.`internal`.ui.widget.Step

public data class ValuOtpFragmentArgs(
  public val step: Step,
) : NavArgs {
  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toBundle(): Bundle {
    val result = Bundle()
    if (Parcelable::class.java.isAssignableFrom(Step::class.java)) {
      result.putParcelable("step", this.step as Parcelable)
    } else if (Serializable::class.java.isAssignableFrom(Step::class.java)) {
      result.putSerializable("step", this.step as Serializable)
    } else {
      throw UnsupportedOperationException(Step::class.java.name +
          " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    if (Parcelable::class.java.isAssignableFrom(Step::class.java)) {
      result.set("step", this.step as Parcelable)
    } else if (Serializable::class.java.isAssignableFrom(Step::class.java)) {
      result.set("step", this.step as Serializable)
    } else {
      throw UnsupportedOperationException(Step::class.java.name +
          " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  public companion object {
    @JvmStatic
    @Suppress("DEPRECATION")
    public fun fromBundle(bundle: Bundle): ValuOtpFragmentArgs {
      bundle.setClassLoader(ValuOtpFragmentArgs::class.java.classLoader)
      val __step : Step?
      if (bundle.containsKey("step")) {
        if (Parcelable::class.java.isAssignableFrom(Step::class.java) ||
            Serializable::class.java.isAssignableFrom(Step::class.java)) {
          __step = bundle.get("step") as Step?
        } else {
          throw UnsupportedOperationException(Step::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (__step == null) {
          throw IllegalArgumentException("Argument \"step\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"step\" is missing and does not have an android:defaultValue")
      }
      return ValuOtpFragmentArgs(__step)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): ValuOtpFragmentArgs {
      val __step : Step?
      if (savedStateHandle.contains("step")) {
        if (Parcelable::class.java.isAssignableFrom(Step::class.java) ||
            Serializable::class.java.isAssignableFrom(Step::class.java)) {
          __step = savedStateHandle.get<Step?>("step")
        } else {
          throw UnsupportedOperationException(Step::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
        if (__step == null) {
          throw IllegalArgumentException("Argument \"step\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"step\" is missing and does not have an android:defaultValue")
      }
      return ValuOtpFragmentArgs(__step)
    }
  }
}
