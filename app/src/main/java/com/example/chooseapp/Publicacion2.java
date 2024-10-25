package com.example.chooseapp;

public class Publicacion2 {
    private String titulo;        // Title of the publication
    private String fecha;         // Publication date
    private String descripcion;   // Description of the publication
    private String imagenUrl;     // URL for the uploaded image
    private String usuarioId;     // ID of the user or organization who created the publication

    // Default constructor required for calls to DataSnapshot.getValue(Publicacion2.class)
    public Publicacion2() {
        // Required empty constructor
    }

    // Constructor to initialize fields
    public Publicacion2(String titulo, String fecha, String descripcion, String imagenUrl, String usuarioId) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.usuarioId = usuarioId;
    }

    // Getters and Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}
