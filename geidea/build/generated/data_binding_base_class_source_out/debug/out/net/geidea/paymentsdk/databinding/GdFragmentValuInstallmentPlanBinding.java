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
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import net.geidea.paymentsdk.R;
import net.geidea.paymentsdk.ui.widget.bnpl.valu.ValuInstallmentPlanView;

public final class GdFragmentValuInstallmentPlanBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final GdIncludeAppbarWithStepperBinding appBarWithStepper;

  @NonNull
  public final Button cancelButton;

  @NonNull
  public final GdIncludeDownPaymentOptionsBinding downPaymentOptions;

  @NonNull
  public final ValuInstallmentPlanView installmentPlanView;

  @NonNull
  public final NestedScrollView nestedScrollView;

  @NonNull
  public final Button nextButton;

  private GdFragmentValuInstallmentPlanBinding(@NonNull CoordinatorLayout rootView,
      @NonNull GdIncludeAppbarWithStepperBinding appBarWithStepper, @NonNull Button cancelButton,
      @NonNull GdIncludeDownPaymentOptionsBinding downPaymentOptions,
      @NonNull ValuInstallmentPlanView installmentPlanView,
      @NonNull NestedScrollView nestedScrollView, @NonNull Button nextButton) {
    this.rootView = rootView;
    this.appBarWithStepper = appBarWithStepper;
    this.cancelButton = cancelButton;
    this.downPaymentOptions = downPaymentOptions;
    this.installmentPlanView = installmentPlanView;
    this.nestedScrollView = nestedScrollView;
    this.nextButton = nextButton;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static GdFragmentValuInstallmentPlanBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static GdFragmentValuInstallmentPlanBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.gd_fragment_valu_installment_plan, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static GdFragmentValuInstallmentPlanBinding bind(@NonNull View rootView) {
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

      id = R.id.downPaymentOptions;
      View downPaymentOptions = ViewBindings.findChildViewById(rootView, id);
      if (downPaymentOptions == null) {
        break missingId;
      }
      GdIncludeDownPaymentOptionsBinding binding_downPaymentOptions = GdIncludeDownPaymentOptionsBinding.bind(downPaymentOptions);

      id = R.id.installmentPlanView;
      ValuInstallmentPlanView installmentPlanView = ViewBindings.findChildViewById(rootView, id);
      if (installmentPlanView == null) {
        break missingId;
      }

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

      return new GdFragmentValuInstallmentPlanBinding((CoordinatorLayout) rootView,
          binding_appBarWithStepper, cancelButton, binding_downPaymentOptions, installmentPlanView,
          nestedScrollView, nextButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
