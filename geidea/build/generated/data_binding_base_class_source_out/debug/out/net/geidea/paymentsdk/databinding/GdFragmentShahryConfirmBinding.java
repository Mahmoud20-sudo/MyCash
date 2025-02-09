// Generated by view binder compiler. Do not edit!
package net.geidea.paymentsdk.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import net.geidea.paymentsdk.R;

public final class GdFragmentShahryConfirmBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final GdIncludeAppbarWithStepperBinding appBarWithStepper;

  @NonNull
  public final Button confirmButton;

  @NonNull
  public final CircularProgressIndicator confirmButtonProgress;

  @NonNull
  public final CoordinatorLayout coordinatorLayout;

  @NonNull
  public final GdIncludeDownPaymentOptionsBinding downPaymentOptions;

  @NonNull
  public final TextView hintTextView;

  @NonNull
  public final TextView merchantNameTextView;

  @NonNull
  public final NestedScrollView nestedScrollView;

  @NonNull
  public final TextInputEditText orderTokenEditText;

  @NonNull
  public final TextInputLayout orderTokenInputLayout;

  @NonNull
  public final GdIncludePayUpfrontBinding payUpfront;

  @NonNull
  public final TextView shahryIdTextView;

  @NonNull
  public final TextView termsTextView;

  @NonNull
  public final TextView totalAmountTextView;

  private GdFragmentShahryConfirmBinding(@NonNull CoordinatorLayout rootView,
      @NonNull GdIncludeAppbarWithStepperBinding appBarWithStepper, @NonNull Button confirmButton,
      @NonNull CircularProgressIndicator confirmButtonProgress,
      @NonNull CoordinatorLayout coordinatorLayout,
      @NonNull GdIncludeDownPaymentOptionsBinding downPaymentOptions,
      @NonNull TextView hintTextView, @NonNull TextView merchantNameTextView,
      @NonNull NestedScrollView nestedScrollView, @NonNull TextInputEditText orderTokenEditText,
      @NonNull TextInputLayout orderTokenInputLayout,
      @NonNull GdIncludePayUpfrontBinding payUpfront, @NonNull TextView shahryIdTextView,
      @NonNull TextView termsTextView, @NonNull TextView totalAmountTextView) {
    this.rootView = rootView;
    this.appBarWithStepper = appBarWithStepper;
    this.confirmButton = confirmButton;
    this.confirmButtonProgress = confirmButtonProgress;
    this.coordinatorLayout = coordinatorLayout;
    this.downPaymentOptions = downPaymentOptions;
    this.hintTextView = hintTextView;
    this.merchantNameTextView = merchantNameTextView;
    this.nestedScrollView = nestedScrollView;
    this.orderTokenEditText = orderTokenEditText;
    this.orderTokenInputLayout = orderTokenInputLayout;
    this.payUpfront = payUpfront;
    this.shahryIdTextView = shahryIdTextView;
    this.termsTextView = termsTextView;
    this.totalAmountTextView = totalAmountTextView;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static GdFragmentShahryConfirmBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static GdFragmentShahryConfirmBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.gd_fragment_shahry_confirm, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static GdFragmentShahryConfirmBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.appBarWithStepper;
      View appBarWithStepper = ViewBindings.findChildViewById(rootView, id);
      if (appBarWithStepper == null) {
        break missingId;
      }
      GdIncludeAppbarWithStepperBinding binding_appBarWithStepper = GdIncludeAppbarWithStepperBinding.bind(appBarWithStepper);

      id = R.id.confirmButton;
      Button confirmButton = ViewBindings.findChildViewById(rootView, id);
      if (confirmButton == null) {
        break missingId;
      }

      id = R.id.confirmButtonProgress;
      CircularProgressIndicator confirmButtonProgress = ViewBindings.findChildViewById(rootView, id);
      if (confirmButtonProgress == null) {
        break missingId;
      }

      CoordinatorLayout coordinatorLayout = (CoordinatorLayout) rootView;

      id = R.id.downPaymentOptions;
      View downPaymentOptions = ViewBindings.findChildViewById(rootView, id);
      if (downPaymentOptions == null) {
        break missingId;
      }
      GdIncludeDownPaymentOptionsBinding binding_downPaymentOptions = GdIncludeDownPaymentOptionsBinding.bind(downPaymentOptions);

      id = R.id.hintTextView;
      TextView hintTextView = ViewBindings.findChildViewById(rootView, id);
      if (hintTextView == null) {
        break missingId;
      }

      id = R.id.merchantNameTextView;
      TextView merchantNameTextView = ViewBindings.findChildViewById(rootView, id);
      if (merchantNameTextView == null) {
        break missingId;
      }

      id = R.id.nestedScrollView;
      NestedScrollView nestedScrollView = ViewBindings.findChildViewById(rootView, id);
      if (nestedScrollView == null) {
        break missingId;
      }

      id = R.id.order_token_edit_text;
      TextInputEditText orderTokenEditText = ViewBindings.findChildViewById(rootView, id);
      if (orderTokenEditText == null) {
        break missingId;
      }

      id = R.id.order_token_input_layout;
      TextInputLayout orderTokenInputLayout = ViewBindings.findChildViewById(rootView, id);
      if (orderTokenInputLayout == null) {
        break missingId;
      }

      id = R.id.payUpfront;
      View payUpfront = ViewBindings.findChildViewById(rootView, id);
      if (payUpfront == null) {
        break missingId;
      }
      GdIncludePayUpfrontBinding binding_payUpfront = GdIncludePayUpfrontBinding.bind(payUpfront);

      id = R.id.shahryIdTextView;
      TextView shahryIdTextView = ViewBindings.findChildViewById(rootView, id);
      if (shahryIdTextView == null) {
        break missingId;
      }

      id = R.id.termsTextView;
      TextView termsTextView = ViewBindings.findChildViewById(rootView, id);
      if (termsTextView == null) {
        break missingId;
      }

      id = R.id.totalAmountTextView;
      TextView totalAmountTextView = ViewBindings.findChildViewById(rootView, id);
      if (totalAmountTextView == null) {
        break missingId;
      }

      return new GdFragmentShahryConfirmBinding((CoordinatorLayout) rootView,
          binding_appBarWithStepper, confirmButton, confirmButtonProgress, coordinatorLayout,
          binding_downPaymentOptions, hintTextView, merchantNameTextView, nestedScrollView,
          orderTokenEditText, orderTokenInputLayout, binding_payUpfront, shahryIdTextView,
          termsTextView, totalAmountTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
