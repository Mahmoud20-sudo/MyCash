package net.geidea.paymentsdk.`internal`.ui.fragment.card.start

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import java.io.Serializable
import java.lang.UnsupportedOperationException
import java.math.BigDecimal
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import net.geidea.paymentsdk.GdCardGraphDirections
import net.geidea.paymentsdk.R
import net.geidea.paymentsdk.`internal`.ui.fragment.receipt.ReceiptArgs
import net.geidea.paymentsdk.`internal`.ui.widget.Step
import net.geidea.paymentsdk.flow.pay.PaymentData

public class CardPaymentStartFragmentDirections private constructor() {
  private data class GdActionGdCardflowstartfragmentToGdCardinputfragment(
    public val step: Step?,
    public val downPaymentAmount: BigDecimal? = null,
  ) : NavDirections {
    public override val actionId: Int =
        R.id.gd_action_gd_cardflowstartfragment_to_gd_cardinputfragment

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
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
  }

  private data class GdActionGdCardflowstartfragmentToGdCvvinputfragment(
    public val tokenId: String,
  ) : NavDirections {
    public override val actionId: Int =
        R.id.gd_action_gd_cardflowstartfragment_to_gd_cvvinputfragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("tokenId", this.tokenId)
        return result
      }
  }

  private data class GdActionGdCardflowstartfragmentToGdCardauthfragment(
    public val finalPaymentData: PaymentData,
    public val step: Step? = null,
    public val cvv: String? = null,
  ) : NavDirections {
    public override val actionId: Int =
        R.id.gd_action_gd_cardflowstartfragment_to_gd_cardauthfragment

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
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
  }

  public companion object {
    public fun gdActionGdCardflowstartfragmentToGdCardinputfragment(step: Step?,
        downPaymentAmount: BigDecimal? = null): NavDirections =
        GdActionGdCardflowstartfragmentToGdCardinputfragment(step, downPaymentAmount)

    public fun gdActionGdCardflowstartfragmentToGdCvvinputfragment(tokenId: String): NavDirections =
        GdActionGdCardflowstartfragmentToGdCvvinputfragment(tokenId)

    public fun gdActionGdCardflowstartfragmentToGdCardauthfragment(
      finalPaymentData: PaymentData,
      step: Step? = null,
      cvv: String? = null,
    ): NavDirections = GdActionGdCardflowstartfragmentToGdCardauthfragment(finalPaymentData, step,
        cvv)

    public fun gdActionGlobalCardauthfragment(
      finalPaymentData: PaymentData,
      step: Step? = null,
      cvv: String? = null,
    ): NavDirections = GdCardGraphDirections.gdActionGlobalCardauthfragment(finalPaymentData, step,
        cvv)

    public fun gdActionGlobalGdPaymentoptionsfragment(downPaymentAmount: BigDecimal? = null,
        step: Step? = null): NavDirections =
        GdCardGraphDirections.gdActionGlobalGdPaymentoptionsfragment(downPaymentAmount, step)

    public fun gdActionGlobalReceipt(args: ReceiptArgs): NavDirections =
        GdCardGraphDirections.gdActionGlobalReceipt(args)
  }
}
