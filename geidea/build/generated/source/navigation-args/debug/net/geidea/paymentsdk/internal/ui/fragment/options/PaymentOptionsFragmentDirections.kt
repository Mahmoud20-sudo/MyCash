package net.geidea.paymentsdk.`internal`.ui.fragment.options

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import java.io.Serializable
import java.math.BigDecimal
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import net.geidea.paymentsdk.GdNavigationDirections
import net.geidea.paymentsdk.R
import net.geidea.paymentsdk.`internal`.ui.fragment.receipt.ReceiptArgs
import net.geidea.paymentsdk.`internal`.ui.widget.Step

public class PaymentOptionsFragmentDirections private constructor() {
  private data class GdActionGdPaymentoptionsfragmentToGdMeezaqrpaymentfragment(
    public val merchantName: String?,
    public val qrMessage: String,
    public val qrCodeImageBase64: String,
    public val paymentIntentId: String,
    public val downPaymentAmount: BigDecimal? = null,
    public val step: Step? = null,
  ) : NavDirections {
    public override val actionId: Int =
        R.id.gd_action_gd_paymentoptionsfragment_to_gd_meezaqrpaymentfragment

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
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
  }

  private data class GdActionGdPaymentoptionsfragmentToGdCardGraph(
    public val downPaymentAmount: BigDecimal? = null,
    public val step: Step? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.gd_action_gd_paymentoptionsfragment_to_gd_card_graph

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
        }
        return result
      }
  }

  public companion object {
    public fun gdActionGdPaymentoptionsfragmentToGdMeezaqrpaymentfragment(
      merchantName: String?,
      qrMessage: String,
      qrCodeImageBase64: String,
      paymentIntentId: String,
      downPaymentAmount: BigDecimal? = null,
      step: Step? = null,
    ): NavDirections = GdActionGdPaymentoptionsfragmentToGdMeezaqrpaymentfragment(merchantName,
        qrMessage, qrCodeImageBase64, paymentIntentId, downPaymentAmount, step)

    public fun gdActionGdPaymentoptionsfragmentToGdValuGraph(): NavDirections =
        ActionOnlyNavDirections(R.id.gd_action_gd_paymentoptionsfragment_to_gd_valu_graph)

    public fun gdActionGdPaymentoptionsfragmentToGdShahryGraph(): NavDirections =
        ActionOnlyNavDirections(R.id.gd_action_gd_paymentoptionsfragment_to_gd_shahry_graph)

    public fun gdActionGdPaymentoptionsfragmentToGdSouhoolaverifycustomerfragment(): NavDirections =
        ActionOnlyNavDirections(R.id.gd_action_gd_paymentoptionsfragment_to_gd_souhoolaverifycustomerfragment)

    public fun gdActionGdPaymentoptionsfragmentToGdCardGraph(downPaymentAmount: BigDecimal? = null,
        step: Step? = null): NavDirections =
        GdActionGdPaymentoptionsfragmentToGdCardGraph(downPaymentAmount, step)

    public fun gdActionGlobalReceipt(args: ReceiptArgs): NavDirections =
        GdNavigationDirections.gdActionGlobalReceipt(args)
  }
}
