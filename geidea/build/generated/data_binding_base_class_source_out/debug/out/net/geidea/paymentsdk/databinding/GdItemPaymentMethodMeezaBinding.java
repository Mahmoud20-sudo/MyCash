// Generated by view binder compiler. Do not edit!
package net.geidea.paymentsdk.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import net.geidea.paymentsdk.R;
import net.geidea.paymentsdk.internal.ui.widget.AspectRatioImageView;
import net.geidea.paymentsdk.ui.widget.phone.PhoneNumberEditText;

public final class GdItemPaymentMethodMeezaBinding implements ViewBinding {
  @NonNull
  private final View rootView;

  @NonNull
  public final AspectRatioImageView logoImageView;

  @NonNull
  public final PhoneNumberEditText phoneNumberEditText;

  @NonNull
  public final TextInputLayout phoneNumberInputLayout;

  @NonNull
  public final RadioButton radioButton;

  @NonNull
  public final LinearLayout radioButtonContent;

  private GdItemPaymentMethodMeezaBinding(@NonNull View rootView,
      @NonNull AspectRatioImageView logoImageView, @NonNull PhoneNumberEditText phoneNumberEditText,
      @NonNull TextInputLayout phoneNumberInputLayout, @NonNull RadioButton radioButton,
      @NonNull LinearLayout radioButtonContent) {
    this.rootView = rootView;
    this.logoImageView = logoImageView;
    this.phoneNumberEditText = phoneNumberEditText;
    this.phoneNumberInputLayout = phoneNumberInputLayout;
    this.radioButton = radioButton;
    this.radioButtonContent = radioButtonContent;
  }

  @Override
  @NonNull
  public View getRoot() {
    return rootView;
  }

  @NonNull
  public static GdItemPaymentMethodMeezaBinding inflate(@NonNull LayoutInflater inflater,
      @NonNull ViewGroup parent) {
    if (parent == null) {
      throw new NullPointerException("parent");
    }
    inflater.inflate(R.layout.gd_item_payment_method_meeza, parent);
    return bind(parent);
  }

  @NonNull
  public static GdItemPaymentMethodMeezaBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.logoImageView;
      AspectRatioImageView logoImageView = ViewBindings.findChildViewById(rootView, id);
      if (logoImageView == null) {
        break missingId;
      }

      id = R.id.phoneNumberEditText;
      PhoneNumberEditText phoneNumberEditText = ViewBindings.findChildViewById(rootView, id);
      if (phoneNumberEditText == null) {
        break missingId;
      }

      id = R.id.phoneNumberInputLayout;
      TextInputLayout phoneNumberInputLayout = ViewBindings.findChildViewById(rootView, id);
      if (phoneNumberInputLayout == null) {
        break missingId;
      }

      id = R.id.radioButton;
      RadioButton radioButton = ViewBindings.findChildViewById(rootView, id);
      if (radioButton == null) {
        break missingId;
      }

      id = R.id.radioButtonContent;
      LinearLayout radioButtonContent = ViewBindings.findChildViewById(rootView, id);
      if (radioButtonContent == null) {
        break missingId;
      }

      return new GdItemPaymentMethodMeezaBinding(rootView, logoImageView, phoneNumberEditText,
          phoneNumberInputLayout, radioButton, radioButtonContent);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
