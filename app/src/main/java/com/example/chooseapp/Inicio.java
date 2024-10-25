package com.example.chooseapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Inicio extends Fragment {

    private RecyclerView recyclerView;
    private PublicacionAdapter adapter;
    private List<Publicacion2> publicaciones;

    public Inicio() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPublicaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        publicaciones = new ArrayList<>();
        adapter = new PublicacionAdapter(publicaciones);
        recyclerView.setAdapter(adapter);

        loadPublicaciones();

        return view;
    }

    private void loadPublicaciones() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("publicaciones");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                publicaciones.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Publicacion2 publicacion = snapshot.getValue(Publicacion2.class);
                    publicaciones.add(publicacion);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error si es necesario
            }
        });
    }
}
