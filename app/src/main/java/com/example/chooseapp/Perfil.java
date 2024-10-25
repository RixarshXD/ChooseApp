package com.example.chooseapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso; // Librería para cargar imágenes

public class Perfil extends Fragment {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String USER_ID_KEY = "userId";

    private TextView nombreUsuario;
    private TextView apellidoUsuario;
    private TextView emailUsuario;
    private TextView telefonoUsuario;
    private TextView sobreTiUsuario;
    private ImageView fotoPerfil; // Campo para la foto de perfil

    public Perfil() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        inicializarTextViews(view);
        fotoPerfil = view.findViewById(R.id.imgFotoPerfil); // Inicializa el ImageView para la foto
        cargarInformacionUsuario();
        return view;
    }

    private void inicializarTextViews(View view) {
        nombreUsuario = view.findViewById(R.id.nombreUsuario);
        apellidoUsuario = view.findViewById(R.id.apellidoUsuario);
        emailUsuario = view.findViewById(R.id.emailUsuario);
        telefonoUsuario = view.findViewById(R.id.telefonoUsuario);
        sobreTiUsuario = view.findViewById(R.id.sobreTiUsuario);
    }

    private void cargarInformacionUsuario() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        String userId = sharedPreferences.getString(USER_ID_KEY, null);

        if (userId != null) {
            verificarTipoUsuario(userId);
        } else {
            Log.e("Perfil", "User ID no encontrado.");
        }
    }

    private void verificarTipoUsuario(String userId) {
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(userId);
        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    mostrarInformacionUsuario(usuario, true); // true indica que es un usuario
                } else {
                    verificarOrganizacion(userId); // Verificar si es una organización
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Perfil", "Error al cargar datos: " + databaseError.getMessage());
            }
        });
    }

    private void verificarOrganizacion(String userId) {
        DatabaseReference organizacionesRef = FirebaseDatabase.getInstance().getReference().child("organizaciones").child(userId);
        organizacionesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Organizacion1 organizacion = dataSnapshot.getValue(Organizacion1.class); // Cambiar a Organizacion1
                    mostrarInformacionUsuario(organizacion, false); // false indica que es una organización
                } else {
                    Log.e("Perfil", "No se encontró el usuario ni la organización en la base de datos.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Perfil", "Error al cargar datos: " + databaseError.getMessage());
            }
        });
    }

    private void mostrarInformacionUsuario(Object perfil, boolean esUsuario) {
        if (perfil == null) return; // Check if perfil is null

        if (esUsuario && perfil instanceof Usuario) {
            Usuario usuario = (Usuario) perfil;
            nombreUsuario.setText(usuario.nombre != null ? usuario.nombre : "No disponible");
            apellidoUsuario.setText(usuario.apellido != null ? usuario.apellido : "No disponible");
            emailUsuario.setText(usuario.email != null ? usuario.email : "No disponible");
            telefonoUsuario.setText(usuario.numeroTelefonico != null ? usuario.numeroTelefonico : "No disponible");
            sobreTiUsuario.setText(usuario.cuentanosSobreTi != null ? usuario.cuentanosSobreTi : "No disponible");

            // Log the URL for debugging
            Log.d("Perfil", "Cargando imagen de usuario: " + usuario.fotoPerfil);
            // Load profile picture from Firebase Storage
            cargarImagen(fotoPerfil, usuario.fotoPerfil);
        } else if (!esUsuario && perfil instanceof Organizacion1) {
            Organizacion1 organizacion = (Organizacion1) perfil;
            nombreUsuario.setText(organizacion.nombre != null ? organizacion.nombre : "No disponible");
            apellidoUsuario.setText("Organización"); // Set to "Organización" as there's no surname
            emailUsuario.setText(organizacion.email != null ? organizacion.email : "No disponible");
            telefonoUsuario.setText(organizacion.numeroTelefonico != null ? organizacion.numeroTelefonico : "No disponible");
            sobreTiUsuario.setText(organizacion.descripcion != null ? organizacion.descripcion : "No disponible");

            // Log the URL for debugging
            Log.d("Perfil", "Cargando imagen de organización: " + organizacion.fotoPerfil);
            // Load profile picture from Firebase Storage
            cargarImagen(fotoPerfil, organizacion.fotoPerfil);
        }
    }


    private void cargarImagen(ImageView imageView, String url) {
        if (url != null) {
            Picasso.get()
                    .load(url) // URL de la imagen
                    .placeholder(R.mipmap.usuario) // Imagen de carga
                    .error(R.mipmap.usuario) // Imagen de error en caso de fallo
                    .into(imageView); // Cargar la imagen
        } else {
            imageView.setImageResource(R.mipmap.usuario); // Establecer imagen por defecto si no hay URL
        }
    }
}
