
package edu.upc.dsa.services;

import java.util.List;

public class PlanVueloRequest {

    private String idDrone;
    private String idPiloto;
    private String fechaInicio;
    private double duracionHoras;
    private List<PosicionDTO> posiciones;

    public PlanVueloRequest() {
    }

    public String getIdDrone() {
        return idDrone;
    }

    public void setIdDrone(String idDrone) {
        this.idDrone = idDrone;
    }

    public String getIdPiloto() {
        return idPiloto;
    }

    public void setIdPiloto(String idPiloto) {
        this.idPiloto = idPiloto;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public double getDuracionHoras() {
        return duracionHoras;
    }

    public void setDuracionHoras(double duracionHoras) {
        this.duracionHoras = duracionHoras;
    }

    public List<PosicionDTO> getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(List<PosicionDTO> posiciones) {
        this.posiciones = posiciones;
    }
}
