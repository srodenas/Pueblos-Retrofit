// Generated by view binder compiler. Do not edit!
package com.pmdm.virgen.pueblosconnavigationdrawer2.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.pmdm.virgen.pueblosconnavigationdrawer2.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnLogin;

  @NonNull
  public final Button btnRegistro;

  @NonNull
  public final ImageView imagenLogo;

  @NonNull
  public final EditText txtEmail;

  @NonNull
  public final EditText txtPassword;

  @NonNull
  public final TextView txtVersion;

  private ActivityLoginBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnLogin,
      @NonNull Button btnRegistro, @NonNull ImageView imagenLogo, @NonNull EditText txtEmail,
      @NonNull EditText txtPassword, @NonNull TextView txtVersion) {
    this.rootView = rootView;
    this.btnLogin = btnLogin;
    this.btnRegistro = btnRegistro;
    this.imagenLogo = imagenLogo;
    this.txtEmail = txtEmail;
    this.txtPassword = txtPassword;
    this.txtVersion = txtVersion;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_login;
      Button btnLogin = ViewBindings.findChildViewById(rootView, id);
      if (btnLogin == null) {
        break missingId;
      }

      id = R.id.btn_registro;
      Button btnRegistro = ViewBindings.findChildViewById(rootView, id);
      if (btnRegistro == null) {
        break missingId;
      }

      id = R.id.imagenLogo;
      ImageView imagenLogo = ViewBindings.findChildViewById(rootView, id);
      if (imagenLogo == null) {
        break missingId;
      }

      id = R.id.txt_email;
      EditText txtEmail = ViewBindings.findChildViewById(rootView, id);
      if (txtEmail == null) {
        break missingId;
      }

      id = R.id.txt_password;
      EditText txtPassword = ViewBindings.findChildViewById(rootView, id);
      if (txtPassword == null) {
        break missingId;
      }

      id = R.id.txtVersion;
      TextView txtVersion = ViewBindings.findChildViewById(rootView, id);
      if (txtVersion == null) {
        break missingId;
      }

      return new ActivityLoginBinding((ConstraintLayout) rootView, btnLogin, btnRegistro,
          imagenLogo, txtEmail, txtPassword, txtVersion);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
