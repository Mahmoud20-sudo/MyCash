package net.geidea.paymentsdk.`internal`.ui.fragment.card.auth

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

public class CardAuthFragmentDirections private constructor() {
  private data class GdActionGdCardauthfragmentToGdReceiptfragment(
    public val args: ReceiptArgs,
  ) : NavDirections {
    public override val actionId: Int = R.id.gd_action_gd_cardauthfragment_to_gd_receiptfragment

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
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
  }

  public companion object {
    public fun gdActionGdCardauthfragmentToGdReceiptfragment(args: ReceiptArgs): NavDirections =
        GdActionGdCardauthfragmentToGdReceiptfragment(args)

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
