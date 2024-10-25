package com.example.chooseapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Publicacion extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etTitulo, etFecha, etDescripcion;
    private Button btnSelectImage, btnEnviarPublicacion;
    private Uri imageUri;
    private DatabaseReference mDatabasePublicaciones;
    private StorageReference mStorageRef;

    // Nombre del archivo de SharedPreferences
    private static final String PREFS_NAME = "MyPrefs";
    private static final String USER_ID_KEY = "userId";
    private String userId;

    public Publicacion() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publicacion, container, false);

        etTitulo = view.findViewById(R.id.et_titulo_publicacion);
        etFecha = view.findViewById(R.id.et_fecha_publicacion);
        etDescripcion = view.findViewById(R.id.et_descripcion_publicacion);
        btnSelectImage = view.findViewById(R.id.btn_select_imagen);
        btnEnviarPublicacion = view.findViewById(R.id.btn_enviar_publicacion);

        mDatabasePublicaciones = FirebaseDatabase.getInstance().getReference("publicaciones");
        mStorageRef = FirebaseStorage.getInstance().getReference("images");

        // Recuperar el User ID de SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID_KEY, null); // Recupera el User ID

        btnSelectImage.setOnClickListener(v -> openImageChooser());
        btnEnviarPublicacion.setOnClickListener(v -> uploadPublication());

        return view;
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
        }
    }

    private void uploadPublication() {
        String titulo = etTitulo.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (titulo.isEmpty() || fecha.isEmpty() || descripcion.isEmpty() || imageUri == null) {
            Toast.makeText(getActivity(), "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a unique ID for the publication
        String publicationId = mDatabasePublicaciones.push().getKey();
        StorageReference fileReference = mStorageRef.child(publicationId + ".jpg");

        fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                // Aquí se usa el userId recuperado de SharedPreferences
                Publicacion2 publicacion = new Publicacion2(titulo, fecha, descripcion, uri.toString(), userId);
                mDatabasePublicaciones.child(publicationId).setValue(publicacion).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Publicación enviada", Toast.LENGTH_SHORT).show();
                        clearInputs(); // Clear inputs after successful submission

                        // Cargar el fragmento de inicio
                        loadInicioFragment();
                    } else {
                        Toast.makeText(getActivity(), "Error al enviar la publicación", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(getActivity(), "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadInicioFragment() {
        // Crear una instancia del fragmento de inicio
        Inicio inicioFragment = new Inicio();

        // Reemplazar el fragmento actual por el de inicio
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, inicioFragment) // Asegúrate de que 'fragment_container' es el ID correcto
                .addToBackStack(null) // Añadir a la pila de retroceso, opcional
                .commit();
    }


    private void clearInputs() {
        etTitulo.setText("");
        etFecha.setText("");
        etDescripcion.setText("");
        imageUri = null; // Reset the image URI
    }
}
