
package edu.upc.dsa.services;

import models.Drone;

public class DroneDTO {

    private String id;
    private String nombre;
    private String fabricante;
    private String modelo;
    private double horasVuelo;

    public DroneDTO() {
    }

    public DroneDTO(Drone drone) {
        this.id = drone.getId();
        this.nombre = drone.getNombre();
        this.fabricante = drone.getFabricante();
        this.modelo = drone.getModelo();
        this.horasVuelo = drone.getHorasVuelo();
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

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getHorasVuelo() {
        return horasVuelo;
    }
}
