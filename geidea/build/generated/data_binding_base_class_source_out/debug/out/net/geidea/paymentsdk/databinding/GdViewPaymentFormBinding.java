// Generated by view binder compiler. Do not edit!
package net.geidea.paymentsdk.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import net.geidea.paymentsdk.R;
import net.geidea.paymentsdk.ui.widget.address.AddressInputView;
import net.geidea.paymentsdk.ui.widget.card.CardInputView;
import net.geidea.paymentsdk.ui.widget.email.EmailEditText;

public final class GdViewPaymentFormBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final LinearLayout addressesLinearLayout;

  @NonNull
  public final AddressInputView billingAddressInputView;

  @NonNull
  public final TextView billingAddressLabel;

  @NonNull
  public final LinearLayout cardBrandLogosLinearLayout;

  @NonNull
  public final CardInputView cardInputView;

  @NonNull
  public final EmailEditText customerEmailEditText;

  @NonNull
  public final TextInputLayout customerEmailInputLayout;

  @NonNull
  public final CheckBox sameAddressCheckbox;

  @NonNull
  public final AddressInputView shippingAddressInputView;

  @NonNull
  public final TextView shippingAddressLabel;

  private GdViewPaymentFormBinding(@NonNull LinearLayout rootView,
      @NonNull LinearLayout addressesLinearLayout,
      @NonNull AddressInputView billingAddressInputView, @NonNull TextView billingAddressLabel,
      @NonNull LinearLayout cardBrandLogosLinearLayout, @NonNull CardInputView cardInputView,
      @NonNull EmailEditText customerEmailEditText,
      @NonNull TextInputLayout customerEmailInputLayout, @NonNull CheckBox sameAddressCheckbox,
      @NonNull AddressInputView shippingAddressInputView, @NonNull TextView shippingAddressLabel) {
    this.rootView = rootView;
    this.addressesLinearLayout = addressesLinearLayout;
    this.billingAddressInputView = billingAddressInputView;
    this.billingAddressLabel = billingAddressLabel;
    this.cardBrandLogosLinearLayout = cardBrandLogosLinearLayout;
    this.cardInputView = cardInputView;
    this.customerEmailEditText = customerEmailEditText;
    this.customerEmailInputLayout = customerEmailInputLayout;
    this.sameAddressCheckbox = sameAddressCheckbox;
    this.shippingAddressInputView = shippingAddressInputView;
    this.shippingAddressLabel = shippingAddressLabel;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static GdViewPaymentFormBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static GdViewPaymentFormBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.gd_view_payment_form, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static GdViewPaymentFormBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.addressesLinearLayout;
      LinearLayout addressesLinearLayout = ViewBindings.findChildViewById(rootView, id);
      if (addressesLinearLayout == null) {
        break missingId;
      }

      id = R.id.billingAddressInputView;
      AddressInputView billingAddressInputView = ViewBindings.findChildViewById(rootView, id);
      if (billingAddressInputView == null) {
        break missingId;
      }

      id = R.id.billingAddressLabel;
      TextView billingAddressLabel = ViewBindings.findChildViewById(rootView, id);
      if (billingAddressLabel == null) {
        break missingId;
      }

      id = R.id.cardBrandLogosLinearLayout;
      LinearLayout cardBrandLogosLinearLayout = ViewBindings.findChildViewById(rootView, id);
      if (cardBrandLogosLinearLayout == null) {
        break missingId;
      }

      id = R.id.cardInputView;
      CardInputView cardInputView = ViewBindings.findChildViewById(rootView, id);
      if (cardInputView == null) {
        break missingId;
      }

      id = R.id.customerEmailEditText;
      EmailEditText customerEmailEditText = ViewBindings.findChildViewById(rootView, id);
      if (customerEmailEditText == null) {
        break missingId;
      }

      id = R.id.customerEmailInputLayout;
      TextInputLayout customerEmailInputLayout = ViewBindings.findChildViewById(rootView, id);
      if (customerEmailInputLayout == null) {
        break missingId;
      }

      id = R.id.sameAddressCheckbox;
      CheckBox sameAddressCheckbox = ViewBindings.findChildViewById(rootView, id);
      if (sameAddressCheckbox == null) {
        break missingId;
      }

      id = R.id.shippingAddressInputView;
      AddressInputView shippingAddressInputView = ViewBindings.findChildViewById(rootView, id);
      if (shippingAddressInputView == null) {
        break missingId;
      }

      id = R.id.shippingAddressLabel;
      TextView shippingAddressLabel = ViewBindings.findChildViewById(rootView, id);
      if (shippingAddressLabel == null) {
        break missingId;
      }

      return new GdViewPaymentFormBinding((LinearLayout) rootView, addressesLinearLayout,
          billingAddressInputView, billingAddressLabel, cardBrandLogosLinearLayout, cardInputView,
          customerEmailEditText, customerEmailInputLayout, sameAddressCheckbox,
          shippingAddressInputView, shippingAddressLabel);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
