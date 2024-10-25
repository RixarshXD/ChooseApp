package com.example.chooseapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrganizacionAdapter extends RecyclerView.Adapter<OrganizacionAdapter.ViewHolder> {

    private List<OrganizacionItem> organizacionList;

    public OrganizacionAdapter(List<OrganizacionItem> organizacionList) {
        this.organizacionList = organizacionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_organizacion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrganizacionItem organizacion = organizacionList.get(position);
        holder.textViewNombre.setText(organizacion.getNombre());
        holder.textViewEmail.setText(organizacion.getEmail());
    }

    @Override
    public int getItemCount() {
        return organizacionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        TextView textViewEmail;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
        }
    }
}
