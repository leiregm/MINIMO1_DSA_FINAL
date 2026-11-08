
package edu.upc.dsa.services;

import java.util.List;

public class CrearPlanVuelo {

    private String idDrone;
    private String idPiloto;
    private String fechaInicio;
    private double duracionHoras;
    private List<PosicionDatos> posiciones;

    public CrearPlanVuelo()
    {
    }

    public String getIdDrone()
    {
        return idDrone;
    }

    public void setIdDrone(String idDrone)
    {
        this.idDrone = idDrone;
    }

    public String getIdPiloto()
    {
        return idPiloto;
    }

    public void setIdPiloto(String idPiloto)
    {
        this.idPiloto = idPiloto;
    }

    public String getFechaInicio()
    {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio)
    {
        this.fechaInicio = fechaInicio;
    }

    public double getDuracionHoras()
    {
        return duracionHoras;
    }

    public void setDuracionHoras(double duracionHoras)
    {
        this.duracionHoras = duracionHoras;
    }

    public List<PosicionDatos> getPosiciones()
    {
        return posiciones;
    }

    public void setPosiciones(List<PosicionDatos> posiciones)
    {
        this.posiciones = posiciones;
    }
}
