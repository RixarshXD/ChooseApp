package com.example.chooseapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegistrarOrganizacion extends AppCompatActivity {

    private EditText etNombre, etEmail, etContrasenia, etNumeroTelefonico, etDescripcion;
    private ImageView imgFotoPerfil;
    private Button btnSeleccionarFoto;
    private Uri fotoPerfilUri;

    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_organizacion);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar Firebase Database y Storage
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        // Inicializar los campos de entrada
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etContrasenia = findViewById(R.id.etContrasenia);
        etNumeroTelefonico = findViewById(R.id.etNumeroTelefonico);
        etDescripcion = findViewById(R.id.etCuentanosSobreTi);
        imgFotoPerfil = findViewById(R.id.imgFotoPerfil);
        btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);

        // Botón para seleccionar una foto
        btnSeleccionarFoto.setOnClickListener(v -> seleccionarFoto());

        // Botón para registrar la organización
        findViewById(R.id.button2).setOnClickListener(v -> registrarOrganizacion());
    }

    private void seleccionarFoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar foto"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fotoPerfilUri = data.getData();
            imgFotoPerfil.setImageURI(fotoPerfilUri);
        }
    }

    private void registrarOrganizacion() {
        // Obtener los valores ingresados
        String nombre = etNombre.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String contrasenia = etContrasenia.getText().toString().trim();
        String numeroTelefonico = etNumeroTelefonico.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        // Validar los campos
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(contrasenia) || TextUtils.isEmpty(numeroTelefonico)) {
            Toast.makeText(this, "Por favor, completa todos los campos obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un nuevo objeto Organizacion
        Organizacion1 organizacion = new Organizacion1(nombre, email, contrasenia, numeroTelefonico, descripcion, null);

        // Obtener un ID único para cada organización
        String orgId = mDatabase.push().getKey();

        // Guardar la foto de perfil en Firebase Storage
        if (fotoPerfilUri != null) {
            StorageReference fotoPerfilRef = mStorage.child("fotos_perfil/" + orgId + ".jpg");
            fotoPerfilRef.putFile(fotoPerfilUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fotoPerfilRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Guardar la URL de la imagen en el objeto de organización
                            organizacion.fotoPerfil = uri.toString();

                            // Guardar los datos en Firebase usando el ID único
                            assert orgId != null;
                            mDatabase.child("organizaciones").child(orgId).setValue(organizacion)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegistrarOrganizacion.this, "Organización registrada exitosamente.", Toast.LENGTH_SHORT).show();
                                            // Redirigir a la actividad principal
                                            Intent intent = new Intent(RegistrarOrganizacion.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(RegistrarOrganizacion.this, "Error al registrar la organización.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }).addOnFailureListener(e -> {
                            Toast.makeText(RegistrarOrganizacion.this, "Error al obtener la URL de la imagen.", Toast.LENGTH_SHORT).show();
                        });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(RegistrarOrganizacion.this, "Error al subir la imagen.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Si no hay foto, solo guardar los demás datos
            assert orgId != null;
            mDatabase.child("organizaciones").child(orgId).setValue(organizacion)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrarOrganizacion.this, "Organización registrada exitosamente sin foto.", Toast.LENGTH_SHORT).show();
                            // Redirigir a la actividad principal
                            Intent intent = new Intent(RegistrarOrganizacion.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegistrarOrganizacion.this, "Error al registrar la organización.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
