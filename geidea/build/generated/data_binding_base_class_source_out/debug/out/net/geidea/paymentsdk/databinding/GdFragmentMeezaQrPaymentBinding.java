// Generated by view binder compiler. Do not edit!
package net.geidea.paymentsdk.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Barrier;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import net.geidea.paymentsdk.R;

public final class GdFragmentMeezaQrPaymentBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final TextView amountFractionPartTextView;

  @NonNull
  public final TextView amountIntegerPartTextView;

  @NonNull
  public final GdIncludeAppbarWithStepperBinding appBarWithStepper;

  @NonNull
  public final Button cancelButton;

  @NonNull
  public final Barrier contentBottomBarrier;

  @NonNull
  public final TextView currencyTextView;

  @NonNull
  public final TextView dontRefreshTextView;

  @NonNull
  public final LinearLayout errorStateLinearLayout;

  @NonNull
  public final LinearLayout idleStateLinearLayout;

  @NonNull
  public final ImageView meezaLogoImageView;

  @NonNull
  public final TextView merchantNameLabelTextView;

  @NonNull
  public final TextView merchantNameTextView;

  @NonNull
  public final NestedScrollView nestedScrollView;

  @NonNull
  public final TextView noNotificationTextView;

  @NonNull
  public final CircularProgressIndicator progress;

  @NonNull
  public final ImageView qrCodeImageView;

  @NonNull
  public final Button requestToPayButton;

  @NonNull
  public final CoordinatorLayout root;

  @NonNull
  public final TextView sendPaymentNotificationTextView;

  @NonNull
  public final TextView titleTextView;

  @NonNull
  public final TextView waitingTextView;

  @NonNull
  public final TextView walletAppHintTextView;

  private GdFragmentMeezaQrPaymentBinding(@NonNull CoordinatorLayout rootView,
      @NonNull TextView amountFractionPartTextView, @NonNull TextView amountIntegerPartTextView,
      @NonNull GdIncludeAppbarWithStepperBinding appBarWithStepper, @NonNull Button cancelButton,
      @NonNull Barrier contentBottomBarrier, @NonNull TextView currencyTextView,
      @NonNull TextView dontRefreshTextView, @NonNull LinearLayout errorStateLinearLayout,
      @NonNull LinearLayout idleStateLinearLayout, @NonNull ImageView meezaLogoImageView,
      @NonNull TextView merchantNameLabelTextView, @NonNull TextView merchantNameTextView,
      @NonNull NestedScrollView nestedScrollView, @NonNull TextView noNotificationTextView,
      @NonNull CircularProgressIndicator progress, @NonNull ImageView qrCodeImageView,
      @NonNull Button requestToPayButton, @NonNull CoordinatorLayout root,
      @NonNull TextView sendPaymentNotificationTextView, @NonNull TextView titleTextView,
      @NonNull TextView waitingTextView, @NonNull TextView walletAppHintTextView) {
    this.rootView = rootView;
    this.amountFractionPartTextView = amountFractionPartTextView;
    this.amountIntegerPartTextView = amountIntegerPartTextView;
    this.appBarWithStepper = appBarWithStepper;
    this.cancelButton = cancelButton;
    this.contentBottomBarrier = contentBottomBarrier;
    this.currencyTextView = currencyTextView;
    this.dontRefreshTextView = dontRefreshTextView;
    this.errorStateLinearLayout = errorStateLinearLayout;
    this.idleStateLinearLayout = idleStateLinearLayout;
    this.meezaLogoImageView = meezaLogoImageView;
    this.merchantNameLabelTextView = merchantNameLabelTextView;
    this.merchantNameTextView = merchantNameTextView;
    this.nestedScrollView = nestedScrollView;
    this.noNotificationTextView = noNotificationTextView;
    this.progress = progress;
    this.qrCodeImageView = qrCodeImageView;
    this.requestToPayButton = requestToPayButton;
    this.root = root;
    this.sendPaymentNotificationTextView = sendPaymentNotificationTextView;
    this.titleTextView = titleTextView;
    this.waitingTextView = waitingTextView;
    this.walletAppHintTextView = walletAppHintTextView;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static GdFragmentMeezaQrPaymentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static GdFragmentMeezaQrPaymentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.gd_fragment_meeza_qr_payment, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static GdFragmentMeezaQrPaymentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.amountFractionPartTextView;
      TextView amountFractionPartTextView = ViewBindings.findChildViewById(rootView, id);
      if (amountFractionPartTextView == null) {
        break missingId;
      }

      id = R.id.amountIntegerPartTextView;
      TextView amountIntegerPartTextView = ViewBindings.findChildViewById(rootView, id);
      if (amountIntegerPartTextView == null) {
        break missingId;
      }

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

      id = R.id.contentBottomBarrier;
      Barrier contentBottomBarrier = ViewBindings.findChildViewById(rootView, id);
      if (contentBottomBarrier == null) {
        break missingId;
      }

      id = R.id.currencyTextView;
      TextView currencyTextView = ViewBindings.findChildViewById(rootView, id);
      if (currencyTextView == null) {
        break missingId;
      }

      id = R.id.dontRefreshTextView;
      TextView dontRefreshTextView = ViewBindings.findChildViewById(rootView, id);
      if (dontRefreshTextView == null) {
        break missingId;
      }

      id = R.id.errorStateLinearLayout;
      LinearLayout errorStateLinearLayout = ViewBindings.findChildViewById(rootView, id);
      if (errorStateLinearLayout == null) {
        break missingId;
      }

      id = R.id.idleStateLinearLayout;
      LinearLayout idleStateLinearLayout = ViewBindings.findChildViewById(rootView, id);
      if (idleStateLinearLayout == null) {
        break missingId;
      }

      id = R.id.meezaLogoImageView;
      ImageView meezaLogoImageView = ViewBindings.findChildViewById(rootView, id);
      if (meezaLogoImageView == null) {
        break missingId;
      }

      id = R.id.merchantNameLabelTextView;
      TextView merchantNameLabelTextView = ViewBindings.findChildViewById(rootView, id);
      if (merchantNameLabelTextView == null) {
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

      id = R.id.noNotificationTextView;
      TextView noNotificationTextView = ViewBindings.findChildViewById(rootView, id);
      if (noNotificationTextView == null) {
        break missingId;
      }

      id = R.id.progress;
      CircularProgressIndicator progress = ViewBindings.findChildViewById(rootView, id);
      if (progress == null) {
        break missingId;
      }

      id = R.id.qrCodeImageView;
      ImageView qrCodeImageView = ViewBindings.findChildViewById(rootView, id);
      if (qrCodeImageView == null) {
        break missingId;
      }

      id = R.id.requestToPayButton;
      Button requestToPayButton = ViewBindings.findChildViewById(rootView, id);
      if (requestToPayButton == null) {
        break missingId;
      }

      CoordinatorLayout root = (CoordinatorLayout) rootView;

      id = R.id.sendPaymentNotificationTextView;
      TextView sendPaymentNotificationTextView = ViewBindings.findChildViewById(rootView, id);
      if (sendPaymentNotificationTextView == null) {
        break missingId;
      }

      id = R.id.titleTextView;
      TextView titleTextView = ViewBindings.findChildViewById(rootView, id);
      if (titleTextView == null) {
        break missingId;
      }

      id = R.id.waitingTextView;
      TextView waitingTextView = ViewBindings.findChildViewById(rootView, id);
      if (waitingTextView == null) {
        break missingId;
      }

      id = R.id.walletAppHintTextView;
      TextView walletAppHintTextView = ViewBindings.findChildViewById(rootView, id);
      if (walletAppHintTextView == null) {
        break missingId;
      }

      return new GdFragmentMeezaQrPaymentBinding((CoordinatorLayout) rootView,
          amountFractionPartTextView, amountIntegerPartTextView, binding_appBarWithStepper,
          cancelButton, contentBottomBarrier, currencyTextView, dontRefreshTextView,
          errorStateLinearLayout, idleStateLinearLayout, meezaLogoImageView,
          merchantNameLabelTextView, merchantNameTextView, nestedScrollView, noNotificationTextView,
          progress, qrCodeImageView, requestToPayButton, root, sendPaymentNotificationTextView,
          titleTextView, waitingTextView, walletAppHintTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
