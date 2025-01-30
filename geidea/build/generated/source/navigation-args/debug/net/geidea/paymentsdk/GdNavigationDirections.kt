package net.geidea.paymentsdk

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import java.io.Serializable
import java.lang.UnsupportedOperationException
import kotlin.Int
import kotlin.Suppress
import net.geidea.paymentsdk.`internal`.ui.fragment.receipt.ReceiptArgs

public class GdNavigationDirections private constructor() {
  private data class GdActionGlobalReceipt(
    public val args: ReceiptArgs,
  ) : NavDirections {
    public override val actionId: Int = R.id.gd_action_global_receipt

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
    public fun gdActionGlobalReceipt(args: ReceiptArgs): NavDirections = GdActionGlobalReceipt(args)
  }
}
