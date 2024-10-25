package com.example.chooseapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Agregar esta línea si estás usando Glide para cargar imágenes.

import java.util.List;

public class PublicacionAdapter extends RecyclerView.Adapter<PublicacionAdapter.PublicacionViewHolder> {

    private List<Publicacion2> publicaciones;

    public PublicacionAdapter(List<Publicacion2> publicaciones) {
        this.publicaciones = publicaciones;
    }

    @NonNull
    @Override
    public PublicacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_publicacion, parent, false);
        return new PublicacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicacionViewHolder holder, int position) {
        Publicacion2 publicacion = publicaciones.get(position);
        holder.tituloPublicacion.setText(publicacion.getTitulo());
        holder.fechaPublicacion.setText(publicacion.getFecha());
        holder.descripcionPublicacion.setText(publicacion.getDescripcion());

        // Cargar la imagen de la publicación
        Glide.with(holder.itemView.getContext())
                .load(publicacion.getImagenUrl()) // Suponiendo que tienes un método getImagenUrl() en Publicacion2
                .into(holder.imagenPublicacion);
    }

    @Override
    public int getItemCount() {
        return publicaciones.size();
    }

    public static class PublicacionViewHolder extends RecyclerView.ViewHolder {
        TextView tituloPublicacion, fechaPublicacion, descripcionPublicacion;
        ImageView imagenPublicacion;

        public PublicacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloPublicacion = itemView.findViewById(R.id.titulo_publicacion);
            fechaPublicacion = itemView.findViewById(R.id.fecha_publicacion);
            descripcionPublicacion = itemView.findViewById(R.id.descripcion_publicacion);
            imagenPublicacion = itemView.findViewById(R.id.imagen_publicacion);
        }
    }
}
