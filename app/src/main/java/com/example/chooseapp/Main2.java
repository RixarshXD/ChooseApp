package com.example.chooseapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Main2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Referencias a los botones
        ImageButton buttonCasa = findViewById(R.id.buttonCasa);
        ImageButton buttonPublicacion = findViewById(R.id.Publicacion);
        ImageButton buttonPerfil = findViewById(R.id.Perfil);

        // Obtener el tipo de usuario
        boolean isOrganization = getIntent().getBooleanExtra("isOrganization", false);

        // Si es un usuario, ocultar el botón de publicación
        if (!isOrganization) {
            buttonPublicacion.setVisibility(View.GONE);
        }

        // Asignar OnClickListener para cada botón
        buttonCasa.setOnClickListener(v -> replaceFragment(new Menu()));
        buttonPublicacion.setOnClickListener(v -> replaceFragment(new Publicacion()));
        buttonPerfil.setOnClickListener(v -> replaceFragment(new Perfil()));

        // Cargar el fragmento de Inicio al inicio
        if (savedInstanceState == null) {
            replaceFragment(new Inicio());
        }
    }


    // Método para reemplazar el fragmento
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null); // Opcional si deseas poder volver atrás
        transaction.commit();
    }
}
