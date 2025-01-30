// Generated by view binder compiler. Do not edit!
package net.geidea.paymentsdk.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import net.geidea.paymentsdk.R;

public final class GdFragmentCardAuthBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final GdIncludeAppbarWithStepperBinding appBarWithStepper;

  @NonNull
  public final CoordinatorLayout coordinatorLayout;

  @NonNull
  public final WebView webView;

  private GdFragmentCardAuthBinding(@NonNull CoordinatorLayout rootView,
      @NonNull GdIncludeAppbarWithStepperBinding appBarWithStepper,
      @NonNull CoordinatorLayout coordinatorLayout, @NonNull WebView webView) {
    this.rootView = rootView;
    this.appBarWithStepper = appBarWithStepper;
    this.coordinatorLayout = coordinatorLayout;
    this.webView = webView;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static GdFragmentCardAuthBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static GdFragmentCardAuthBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.gd_fragment_card_auth, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static GdFragmentCardAuthBinding bind(@NonNull View rootView) {
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

      CoordinatorLayout coordinatorLayout = (CoordinatorLayout) rootView;

      id = R.id.webView;
      WebView webView = ViewBindings.findChildViewById(rootView, id);
      if (webView == null) {
        break missingId;
      }

      return new GdFragmentCardAuthBinding((CoordinatorLayout) rootView, binding_appBarWithStepper,
          coordinatorLayout, webView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
