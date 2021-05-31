package com.ingenia.bank.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;   // para el login

    private String nombreCompleto;

    @JsonIgnore
    private String password;

    // relaciones

    @ManyToMany
    @JoinTable(
            name = "usuario_cuenta",
            joinColumns = {@JoinColumn(name="usuario_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="cuenta_id", referencedColumnName = "id")}
    )
    List<Cuenta> cuentas = new ArrayList<>();


    public Usuario() {
    }

    public Usuario(String username, String nombreCompleto, String password) {
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }
}
