package com.example.chooseapp;

public class Usuario {
    public String nombre;
    public String apellido;
    public String email;
    public String contrasenia;
    public String numeroTelefonico;
    public String cuentanosSobreTi;
    public String queTeAtrajo;
    public String experienciaVoluntario;
    public Integer insignias; // Para representar la cantidad de insignias
    public String fotoPerfil;  // Nuevo atributo para almacenar la foto de perfil

    public Usuario() {
        // Constructor vacío necesario para Firebase
    }

    public Usuario(String nombre, String apellido, String email, String contrasenia, String numeroTelefonico, String cuentanosSobreTi, String queTeAtrajo, String experienciaVoluntario, String fotoPerfil) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasenia = contrasenia;
        this.numeroTelefonico = numeroTelefonico;
        this.cuentanosSobreTi = cuentanosSobreTi;
        this.queTeAtrajo = queTeAtrajo;
        this.experienciaVoluntario = experienciaVoluntario;
        this.insignias = null; // Inicialmente nulo
        this.fotoPerfil = fotoPerfil; // Inicializar la foto de perfil
    }

    // Constructor que incluye insignias y foto de perfil
    public Usuario(String nombre, String apellido, String email, String contrasenia, String numeroTelefonico, String cuentanosSobreTi, String queTeAtrajo, String experienciaVoluntario, Integer insignias, String fotoPerfil) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasenia = contrasenia;
        this.numeroTelefonico = numeroTelefonico;
        this.cuentanosSobreTi = cuentanosSobreTi;
        this.queTeAtrajo = queTeAtrajo;
        this.experienciaVoluntario = experienciaVoluntario;
        this.insignias = insignias;
        this.fotoPerfil = fotoPerfil; // Inicializar la foto de perfil
    }
}
