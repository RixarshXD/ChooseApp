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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegistrarCuenta extends AppCompatActivity {

    private EditText etNombre, etApellido, etEmail, etContrasenia, etNumeroTelefonico, etCuentanosSobreTi, etQueTeAtrajoaSerVoluntario, etTienesExperienciaComoVoluntario;
    private ImageView imgFotoPerfil;
    private Button btnSeleccionarFoto;
    private Uri fotoPerfilUri;

    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_cuenta);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Principal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etEmail = findViewById(R.id.etEmail);
        etContrasenia = findViewById(R.id.etContrasenia);
        etNumeroTelefonico = findViewById(R.id.etNumeroTelefonico);
        etCuentanosSobreTi = findViewById(R.id.etCuentanosSobreTi);
        etQueTeAtrajoaSerVoluntario = findViewById(R.id.etQueTeAtrajoaSerVoluntario);
        etTienesExperienciaComoVoluntario = findViewById(R.id.etTienesExperienciaComoVoluntario);
        imgFotoPerfil = findViewById(R.id.imgFotoPerfil);
        btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);

        btnSeleccionarFoto.setOnClickListener(v -> seleccionarFoto());

        findViewById(R.id.button2).setOnClickListener(v -> registrarCuenta());
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

    private void registrarCuenta() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String contrasenia = etContrasenia.getText().toString().trim();
        String numeroTelefonico = etNumeroTelefonico.getText().toString().trim();
        String cuentanosSobreTi = etCuentanosSobreTi.getText().toString().trim();
        String queTeAtrajo = etQueTeAtrajoaSerVoluntario.getText().toString().trim();
        String experienciaVoluntario = etTienesExperienciaComoVoluntario.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(contrasenia) || TextUtils.isEmpty(numeroTelefonico)) {
            Toast.makeText(this, "Por favor, completa todos los campos obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fotoPerfilUri != null) {
            // Subir la foto a Firebase Storage
            StorageReference fileReference = mStorage.child("fotos_perfil").child(System.currentTimeMillis() + ".jpg");
            fileReference.putFile(fotoPerfilUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Obtener la URL de descarga
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String fotoUrl = uri.toString();
                            guardarUsuarioEnFirebase(nombre, apellido, email, contrasenia, numeroTelefonico, cuentanosSobreTi, queTeAtrajo, experienciaVoluntario, fotoUrl);
                        });
                    })
                    .addOnFailureListener(e -> Toast.makeText(RegistrarCuenta.this, "Error al subir la foto: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            guardarUsuarioEnFirebase(nombre, apellido, email, contrasenia, numeroTelefonico, cuentanosSobreTi, queTeAtrajo, experienciaVoluntario, null);
        }
    }

    private void guardarUsuarioEnFirebase(String nombre, String apellido, String email, String contrasenia, String numeroTelefonico, String cuentanosSobreTi, String queTeAtrajo, String experienciaVoluntario, String fotoUrl) {
        Usuario usuario = new Usuario(nombre, apellido, email, contrasenia, numeroTelefonico, cuentanosSobreTi, queTeAtrajo, experienciaVoluntario, fotoUrl);
        String userId = mDatabase.push().getKey();

        assert userId != null;
        mDatabase.child("usuarios").child(userId).setValue(usuario)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegistrarCuenta.this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrarCuenta.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegistrarCuenta.this, "Error al registrar usuario.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etApellido.setText("");
        etEmail.setText("");
        etContrasenia.setText("");
        etNumeroTelefonico.setText("");
        etCuentanosSobreTi.setText("");
        etQueTeAtrajoaSerVoluntario.setText("");
        etTienesExperienciaComoVoluntario.setText("");
        imgFotoPerfil.setImageURI(null);
    }
}
