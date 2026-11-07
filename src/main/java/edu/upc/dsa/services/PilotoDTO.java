
package edu.upc.dsa.services;

import models.Piloto;

public class PilotoDTO {

    private String id;
    private String nombre;
    private String apellidos;
    private double horasVuelo;

    public PilotoDTO() {
    }

    public PilotoDTO(Piloto piloto) {
        this.id = piloto.getId();
        this.nombre = piloto.getNombre();
        this.apellidos = piloto.getApellidos();
        this.horasVuelo = piloto.getHorasVuelo();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public double getHorasVuelo() {
        return horasVuelo;
    }
}
