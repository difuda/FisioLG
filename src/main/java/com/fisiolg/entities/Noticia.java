package com.fisiolg.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "noticias")
public class Noticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Le damos una longitud de 500 caracteres por si Lucía escribe un aviso largo
    @Column(nullable = false, length = 500)
    private String texto;

    // Constructor vacío (obligatorio para Spring Boot / JPA)
    public Noticia() {
    }

    // Constructor para cuando queramos crear una noticia rápida
    public Noticia(String texto) {
        this.texto = texto;
    }

    // --- GETTERS Y SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}