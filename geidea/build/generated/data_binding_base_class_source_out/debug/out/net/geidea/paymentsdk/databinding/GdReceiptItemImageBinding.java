// Generated by view binder compiler. Do not edit!
package net.geidea.paymentsdk.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import java.lang.NullPointerException;
import java.lang.Override;
import net.geidea.paymentsdk.R;

public final class GdReceiptItemImageBinding implements ViewBinding {
  @NonNull
  private final ImageView rootView;

  @NonNull
  public final ImageView imageView;

  private GdReceiptItemImageBinding(@NonNull ImageView rootView, @NonNull ImageView imageView) {
    this.rootView = rootView;
    this.imageView = imageView;
  }

  @Override
  @NonNull
  public ImageView getRoot() {
    return rootView;
  }

  @NonNull
  public static GdReceiptItemImageBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static GdReceiptItemImageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.gd_receipt_item_image, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static GdReceiptItemImageBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    ImageView imageView = (ImageView) rootView;

    return new GdReceiptItemImageBinding((ImageView) rootView, imageView);
  }
}
