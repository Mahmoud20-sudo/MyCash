// Generated by view binder compiler. Do not edit!
package net.geidea.paymentsdk.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.button.MaterialButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import net.geidea.paymentsdk.R;

public final class GdViewIconSnackbarBinding implements ViewBinding {
  @NonNull
  private final View rootView;

  @NonNull
  public final MaterialButton actionButton;

  @NonNull
  public final ImageView iconImageView;

  @NonNull
  public final TextView messageTextView;

  @NonNull
  public final TextView referenceTextView;

  @NonNull
  public final TextView titleTextView;

  private GdViewIconSnackbarBinding(@NonNull View rootView, @NonNull MaterialButton actionButton,
      @NonNull ImageView iconImageView, @NonNull TextView messageTextView,
      @NonNull TextView referenceTextView, @NonNull TextView titleTextView) {
    this.rootView = rootView;
    this.actionButton = actionButton;
    this.iconImageView = iconImageView;
    this.messageTextView = messageTextView;
    this.referenceTextView = referenceTextView;
    this.titleTextView = titleTextView;
  }

  @Override
  @NonNull
  public View getRoot() {
    return rootView;
  }

  @NonNull
  public static GdViewIconSnackbarBinding inflate(@NonNull LayoutInflater inflater,
      @NonNull ViewGroup parent) {
    if (parent == null) {
      throw new NullPointerException("parent");
    }
    inflater.inflate(R.layout.gd_view_icon_snackbar, parent);
    return bind(parent);
  }

  @NonNull
  public static GdViewIconSnackbarBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.actionButton;
      MaterialButton actionButton = ViewBindings.findChildViewById(rootView, id);
      if (actionButton == null) {
        break missingId;
      }

      id = R.id.iconImageView;
      ImageView iconImageView = ViewBindings.findChildViewById(rootView, id);
      if (iconImageView == null) {
        break missingId;
      }

      id = R.id.messageTextView;
      TextView messageTextView = ViewBindings.findChildViewById(rootView, id);
      if (messageTextView == null) {
        break missingId;
      }

      id = R.id.referenceTextView;
      TextView referenceTextView = ViewBindings.findChildViewById(rootView, id);
      if (referenceTextView == null) {
        break missingId;
      }

      id = R.id.titleTextView;
      TextView titleTextView = ViewBindings.findChildViewById(rootView, id);
      if (titleTextView == null) {
        break missingId;
      }

      return new GdViewIconSnackbarBinding(rootView, actionButton, iconImageView, messageTextView,
          referenceTextView, titleTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
