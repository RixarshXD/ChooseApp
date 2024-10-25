package com.example.chooseapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button loginButton;
    private DatabaseReference mDatabaseUsuarios;
    private DatabaseReference mDatabaseOrganizaciones;

    // Nombre del archivo de SharedPreferences
    private static final String PREFS_NAME = "MyPrefs";
    private static final String USER_ID_KEY = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar Firebase Database
        mDatabaseUsuarios = FirebaseDatabase.getInstance().getReference().child("usuarios");
        mDatabaseOrganizaciones = FirebaseDatabase.getInstance().getReference().child("organizaciones");

        // Inicializar los campos de entrada
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);

        // Botón de inicio de sesión
        loginButton.setOnClickListener(v -> iniciarSesion());

        // Botón de "Crear cuenta" que lleva a la actividad de registro
        findViewById(R.id.register_link).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrarCuenta.class);
            startActivity(intent);
        });

        // Botón de "Crear cuenta organización"
        findViewById(R.id.register_organizacion).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrarOrganizacion.class);
            startActivity(intent);
        });
    }

    private void iniciarSesion() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validación básica de campos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Por favor, ingresa correo y contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Consultar usuarios
        mDatabaseUsuarios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean userFound = false;

                // Recorrer todos los usuarios en la base de datos
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String dbEmail = snapshot.child("email").getValue(String.class);
                    String dbPassword = snapshot.child("contrasenia").getValue(String.class);

                    // Verificar si el correo y la contraseña coinciden
                    if (dbEmail != null && dbEmail.equals(email) && dbPassword != null && dbPassword.equals(password)) {
                        // Obtener el UID del usuario
                        String userId = snapshot.getKey();
                        Log.d("Login", "Usuario autenticado. UID: " + userId);
                        userFound = true;

                        // Guardar el userId en SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(USER_ID_KEY, userId);
                        editor.apply();

                        // Redirigir al usuario a otra actividad
                        Intent intent = new Intent(MainActivity.this, Main2.class);
                        intent.putExtra("userId", userId);
                        intent.putExtra("isOrganization", false); // Indica que es un usuario normal
                        startActivity(intent);
                        finish();
                        break;
                    }
                }

                // Si no se encuentra el usuario, buscar en organizaciones
                if (!userFound) {
                    buscarOrganizacion(email, password);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Login", "Error al leer la base de datos: " + databaseError.getMessage());
            }
        });
    }

    private void buscarOrganizacion(String email, String password) {
        // Consultar organizaciones
        mDatabaseOrganizaciones.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean orgFound = false;

                // Recorrer todas las organizaciones en la base de datos
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String dbEmail = snapshot.child("email").getValue(String.class);
                    String dbPassword = snapshot.child("contrasenia").getValue(String.class);

                    // Verificar si el correo y la contraseña coinciden
                    if (dbEmail != null && dbEmail.equals(email) && dbPassword != null && dbPassword.equals(password)) {
                        // Obtener el UID de la organización
                        String orgId = snapshot.getKey();
                        Log.d("Login", "Organización autenticada. UID: " + orgId);
                        orgFound = true;

                        // Guardar el orgId en SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(USER_ID_KEY, orgId);
                        editor.apply();

                        // Redirigir a la actividad correspondiente
                        Intent intent = new Intent(MainActivity.this, Main2.class);
                        intent.putExtra("userId", orgId);
                        intent.putExtra("isOrganization", true); // Indica que es una organización
                        startActivity(intent);
                        finish();
                        break;
                    }
                }

                if (!orgFound) {
                    Toast.makeText(MainActivity.this, "Correo o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Login", "Error al leer la base de datos: " + databaseError.getMessage());
            }
        });
    }
}
