// Generated by view binder compiler. Do not edit!
package net.geidea.paymentsdk.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import java.lang.NullPointerException;
import java.lang.Override;
import net.geidea.paymentsdk.R;
import net.geidea.paymentsdk.internal.ui.widget.IconSnackbarLayout;

public final class GdLayoutIconSnackbarBinding implements ViewBinding {
  @NonNull
  private final IconSnackbarLayout rootView;

  private GdLayoutIconSnackbarBinding(@NonNull IconSnackbarLayout rootView) {
    this.rootView = rootView;
  }

  @Override
  @NonNull
  public IconSnackbarLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static GdLayoutIconSnackbarBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static GdLayoutIconSnackbarBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.gd_layout_icon_snackbar, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static GdLayoutIconSnackbarBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    return new GdLayoutIconSnackbarBinding((IconSnackbarLayout) rootView);
  }
}
