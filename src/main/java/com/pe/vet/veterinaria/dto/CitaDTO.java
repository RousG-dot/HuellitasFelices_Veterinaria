package com.pe.vet.veterinaria.dto;

public class CitaDTO {
    private int id;
    private Integer clienteId;
    private Integer mascotaId;
    private String cliente;
    private String mascota;
    private String fecha;
    private String hora;
    private String motivo;

    public CitaDTO() {
    }

    public CitaDTO(int id, Integer clienteId, Integer mascotaId, String cliente, String mascota, String fecha, String hora, String motivo) {
        this.id = id;
        this.clienteId = clienteId;
        this.mascotaId = mascotaId;
        this.cliente = cliente;
        this.mascota = mascota;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(Integer mascotaId) {
        this.mascotaId = mascotaId;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getMascota() {
        return mascota;
    }

    public void setMascota(String mascota) {
        this.mascota = mascota;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
