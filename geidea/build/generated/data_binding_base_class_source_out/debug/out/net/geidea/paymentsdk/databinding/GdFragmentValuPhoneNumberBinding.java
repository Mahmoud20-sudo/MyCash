// Generated by view binder compiler. Do not edit!
package net.geidea.paymentsdk.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import net.geidea.paymentsdk.R;
import net.geidea.paymentsdk.ui.widget.phone.PhoneNumberEditText;

public final class GdFragmentValuPhoneNumberBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final GdIncludeAppbarWithStepperBinding appBarWithStepper;

  @NonNull
  public final Button cancelButton;

  @NonNull
  public final CoordinatorLayout coordinatorLayout;

  @NonNull
  public final NestedScrollView nestedScrollView;

  @NonNull
  public final Button nextButton;

  @NonNull
  public final CircularProgressIndicator nextButtonProgress;

  @NonNull
  public final PhoneNumberEditText phoneNumberEditText;

  @NonNull
  public final TextInputLayout phoneNumberInputLayout;

  private GdFragmentValuPhoneNumberBinding(@NonNull CoordinatorLayout rootView,
      @NonNull GdIncludeAppbarWithStepperBinding appBarWithStepper, @NonNull Button cancelButton,
      @NonNull CoordinatorLayout coordinatorLayout, @NonNull NestedScrollView nestedScrollView,
      @NonNull Button nextButton, @NonNull CircularProgressIndicator nextButtonProgress,
      @NonNull PhoneNumberEditText phoneNumberEditText,
      @NonNull TextInputLayout phoneNumberInputLayout) {
    this.rootView = rootView;
    this.appBarWithStepper = appBarWithStepper;
    this.cancelButton = cancelButton;
    this.coordinatorLayout = coordinatorLayout;
    this.nestedScrollView = nestedScrollView;
    this.nextButton = nextButton;
    this.nextButtonProgress = nextButtonProgress;
    this.phoneNumberEditText = phoneNumberEditText;
    this.phoneNumberInputLayout = phoneNumberInputLayout;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static GdFragmentValuPhoneNumberBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static GdFragmentValuPhoneNumberBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.gd_fragment_valu_phone_number, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static GdFragmentValuPhoneNumberBinding bind(@NonNull View rootView) {
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

      id = R.id.cancelButton;
      Button cancelButton = ViewBindings.findChildViewById(rootView, id);
      if (cancelButton == null) {
        break missingId;
      }

      CoordinatorLayout coordinatorLayout = (CoordinatorLayout) rootView;

      id = R.id.nestedScrollView;
      NestedScrollView nestedScrollView = ViewBindings.findChildViewById(rootView, id);
      if (nestedScrollView == null) {
        break missingId;
      }

      id = R.id.nextButton;
      Button nextButton = ViewBindings.findChildViewById(rootView, id);
      if (nextButton == null) {
        break missingId;
      }

      id = R.id.nextButtonProgress;
      CircularProgressIndicator nextButtonProgress = ViewBindings.findChildViewById(rootView, id);
      if (nextButtonProgress == null) {
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

      return new GdFragmentValuPhoneNumberBinding((CoordinatorLayout) rootView,
          binding_appBarWithStepper, cancelButton, coordinatorLayout, nestedScrollView, nextButton,
          nextButtonProgress, phoneNumberEditText, phoneNumberInputLayout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
