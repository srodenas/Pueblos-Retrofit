package com.pmdm.virgen.pueblosconnavigationdrawer2.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pmdm.virgen.pueblosconnavigationdrawer2.R;
import com.pmdm.virgen.pueblosconnavigationdrawer2.listener.OnPuebloInteractionListener;
import com.pmdm.virgen.pueblosconnavigationdrawer2.modelos_api.PuebloApi;


import java.util.List;

/*
Para la versión de Realm.
- Hay que hacer un método que actualize un RealmResult.
 */
public class MyPuebloRecyclerViewAdapter extends RecyclerView.Adapter<MyPuebloRecyclerViewAdapter.ViewPueblo> {

    private final List<PuebloApi> mValues;
    private OnPuebloInteractionListener listenerPueblo;
    private Context contexto;
    public MyPuebloRecyclerViewAdapter(List<PuebloApi> items, OnPuebloInteractionListener listener, Context contexto) {
        mValues = items;
        listenerPueblo = listener;
        this.contexto = contexto;

    }


    @Override
    public ViewPueblo onCreateViewHolder(ViewGroup parent, int viewType) {
        View vistaElementoPueblo = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.elemento_pueblo, parent, false);

        return new ViewPueblo(vistaElementoPueblo);

    }

    @Override
    public void onBindViewHolder(final ViewPueblo holder, int position) {
        holder.mItem = mValues.get(position);
        holder.textViewNombre.setText(holder.mItem.getNombre());
        holder.textViewDescripcion.setText(holder.mItem.getDescripcion());
        holder.textViewHabitantes.setText(String.valueOf(holder.mItem.getHabitantes()));

        if (holder.mItem.getImagen()!=null) {
            RequestOptions options = new RequestOptions();
            options.centerCrop();

            String imagenUrl = holder.mItem.getImagen();
            Glide.with(contexto).load(imagenUrl)
                    // .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .error(R.drawable.ic_baseline_camera_24)
                    .apply(options)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.imageViewPueblo);
        }
        /*
        Falta mostrar la imagen de la foto. Recordar que Glide, necesita el contexto del
        Activity.
         */
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /*
    Esta clase, es la encargada de recibir la vista inflada del elemento gráfico, asociarle
    elementos de la vista con objetos java para poder setear datos en sus campos.
     */
    public class ViewPueblo extends RecyclerView.ViewHolder {
        public final TextView textViewNombre;
        public final TextView textViewDescripcion;
        public final TextView textViewHabitantes;
        public final ImageView imageViewPueblo;
        public final ImageView imageViewEditarPueblo;
        public final ImageView imageViewBorrarPueblo;
        public PuebloApi mItem;  //es el objeto pueblo que representa el objeto
        public View vista;  //será el constrain layout del layout_pueblos_item



        public ViewPueblo(View view) {
            super(view.getRootView()); //le pasamos la vista a la que tiene que asociar elementos.
            vista = view;

            /**
             * Debo enlazar cada uno de los elementos de nuestro layout_pueblos_item
             * con cada propiedad de nuestra objeto interno.
             */
            textViewNombre = vista.findViewById(R.id.textViewNombre);
            textViewDescripcion = vista.findViewById(R.id.textViewDescripcion);
            textViewHabitantes = vista.findViewById(R.id.textViewHabitantes);
            imageViewPueblo = vista.findViewById(R.id.imageViewPueblo);
            imageViewEditarPueblo = vista.findViewById(R.id.image_view_editar_pueblo);
            imageViewBorrarPueblo = vista.findViewById(R.id.imageView_borrar_pueblo);

            imageViewEditarPueblo.setOnClickListener(
                    (evento)->{
                        listenerPueblo.onPuebloEditarClick(mItem);
                    }
            );

            imageViewBorrarPueblo.setOnClickListener(
                    (evento)->{
                        listenerPueblo.onPuebloBorrarClick(mItem);
                    }
            );


           vista.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   listenerPueblo.onPuebloClick(mItem);
               }
           });
        }



    }
}