package com.pe.vet.veterinaria.model;

public class Mascota {

    private int id;
    private String nombre;
    private String especie;
    private String raza;
    private String dueno;
    private Integer clienteId;

    public Mascota() {
    }

    public Mascota(int id, String nombre, String especie, String raza, String dueno) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.dueno = dueno;
    }

    public Mascota(int id, String nombre, String especie, String raza, String dueno, Integer clienteId) {
        this(id, nombre, especie, raza, dueno);
        this.clienteId = clienteId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getDueno() {
        return dueno;
    }

    public void setDueno(String dueno) {
        this.dueno = dueno;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }
}
