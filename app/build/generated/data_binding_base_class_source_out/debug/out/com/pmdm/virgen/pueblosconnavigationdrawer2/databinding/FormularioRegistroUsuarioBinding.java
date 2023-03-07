// Generated by view binder compiler. Do not edit!
package com.pmdm.virgen.pueblosconnavigationdrawer2.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.pmdm.virgen.pueblosconnavigationdrawer2.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FormularioRegistroUsuarioBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final EditText editEmail;

  @NonNull
  public final EditText editNombre;

  @NonNull
  public final EditText editPassword;

  @NonNull
  public final ImageView imagViewCapFoto2;

  @NonNull
  public final ImageView textViewImagenFoto2;

  private FormularioRegistroUsuarioBinding(@NonNull LinearLayout rootView,
      @NonNull EditText editEmail, @NonNull EditText editNombre, @NonNull EditText editPassword,
      @NonNull ImageView imagViewCapFoto2, @NonNull ImageView textViewImagenFoto2) {
    this.rootView = rootView;
    this.editEmail = editEmail;
    this.editNombre = editNombre;
    this.editPassword = editPassword;
    this.imagViewCapFoto2 = imagViewCapFoto2;
    this.textViewImagenFoto2 = textViewImagenFoto2;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FormularioRegistroUsuarioBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FormularioRegistroUsuarioBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.formulario_registro_usuario, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FormularioRegistroUsuarioBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.edit_email;
      EditText editEmail = ViewBindings.findChildViewById(rootView, id);
      if (editEmail == null) {
        break missingId;
      }

      id = R.id.edit_nombre;
      EditText editNombre = ViewBindings.findChildViewById(rootView, id);
      if (editNombre == null) {
        break missingId;
      }

      id = R.id.edit_password;
      EditText editPassword = ViewBindings.findChildViewById(rootView, id);
      if (editPassword == null) {
        break missingId;
      }

      id = R.id.imag_view_cap_foto2;
      ImageView imagViewCapFoto2 = ViewBindings.findChildViewById(rootView, id);
      if (imagViewCapFoto2 == null) {
        break missingId;
      }

      id = R.id.text_view_imagen_foto2;
      ImageView textViewImagenFoto2 = ViewBindings.findChildViewById(rootView, id);
      if (textViewImagenFoto2 == null) {
        break missingId;
      }

      return new FormularioRegistroUsuarioBinding((LinearLayout) rootView, editEmail, editNombre,
          editPassword, imagViewCapFoto2, textViewImagenFoto2);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
