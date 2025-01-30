package net.geidea.paymentsdk.`internal`.ui.fragment.qr

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import java.io.Serializable
import java.lang.UnsupportedOperationException
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import net.geidea.paymentsdk.GdNavigationDirections
import net.geidea.paymentsdk.R
import net.geidea.paymentsdk.`internal`.ui.fragment.receipt.ReceiptArgs

public class MeezaQrPaymentFragmentDirections private constructor() {
  private data class GdActionGdMeezaqrpaymentfragmentToGdReceiptfragment(
    public val args: ReceiptArgs,
  ) : NavDirections {
    public override val actionId: Int =
        R.id.gd_action_gd_meezaqrpaymentfragment_to_gd_receiptfragment

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

  private data class GdActionGdMeezaqrpaymentfragmentToGdMeezaqrrequestpaymentfragment(
    public val qrMessage: String,
  ) : NavDirections {
    public override val actionId: Int =
        R.id.gd_action_gd_meezaqrpaymentfragment_to_gd_meezaqrrequestpaymentfragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("qrMessage", this.qrMessage)
        return result
      }
  }

  public companion object {
    public fun gdActionGdMeezaqrpaymentfragmentToGdReceiptfragment(args: ReceiptArgs): NavDirections
        = GdActionGdMeezaqrpaymentfragmentToGdReceiptfragment(args)

    public fun gdActionGdMeezaqrpaymentfragmentToGdMeezaqrrequestpaymentfragment(qrMessage: String):
        NavDirections = GdActionGdMeezaqrpaymentfragmentToGdMeezaqrrequestpaymentfragment(qrMessage)

    public fun gdActionGlobalReceipt(args: ReceiptArgs): NavDirections =
        GdNavigationDirections.gdActionGlobalReceipt(args)
  }
}
