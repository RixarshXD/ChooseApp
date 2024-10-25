package com.example.chooseapp;

public class Organizacion1 {
    public String nombre;
    public String email;
    public String contrasenia;
    public String numeroTelefonico;
    public String descripcion; // Para almacenar información sobre la organización
    public String fotoPerfil;   // Para almacenar la foto de perfil de la organización

    public Organizacion1() {
        // Constructor vacío necesario para Firebase
    }

    public Organizacion1(String nombre, String email, String contrasenia, String numeroTelefonico, String descripcion, String fotoPerfil) {
        this.nombre = nombre;
        this.email = email;
        this.contrasenia = contrasenia;
        this.numeroTelefonico = numeroTelefonico;
        this.descripcion = descripcion;
        this.fotoPerfil = fotoPerfil; // Inicializar la foto de perfil
    }
}
