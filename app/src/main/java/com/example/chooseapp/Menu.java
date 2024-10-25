package com.example.chooseapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Menu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Menu extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Menu() {
        // Required empty public constructor
    }

    public static Menu newInstance(String param1, String param2) {
        Menu fragment = new Menu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // Find TextViews and set click listeners to load fragments
        TextView menuInicio = view.findViewById(R.id.menu_inicio);
        TextView menuPerfil = view.findViewById(R.id.menu_perfil);
        TextView menuOrganizaciones = view.findViewById(R.id.menu_organizaciones);
        TextView menuInsignias = view.findViewById(R.id.menu_insignias);

        Button btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

        // Cargar fragmentos correspondientes al hacer clic
        menuInicio.setOnClickListener(v -> replaceFragment(new Inicio()));
        menuPerfil.setOnClickListener(v -> replaceFragment(new Perfil()));
        menuOrganizaciones.setOnClickListener(v -> replaceFragment(new Organizacion()));
        menuInsignias.setOnClickListener(v -> replaceFragment(new Insignias()));

        // Cerrar sesión al hacer clic en el botón
        btnCerrarSesion.setOnClickListener(v -> {
            // Redirigir a la actividad de Login
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar el historial
            startActivity(intent);
        });

        return view;
    }

    // Método para reemplazar fragmentos
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); // Reemplaza con el ID de tu contenedor
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
