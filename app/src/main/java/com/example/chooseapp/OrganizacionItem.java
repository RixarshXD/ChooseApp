package com.example.chooseapp;

public class OrganizacionItem {
    private String nombre;
    private String email;

    public OrganizacionItem(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }
}
