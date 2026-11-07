
package edu.upc.dsa.services;

import models.PlanVuelo;
import models.Posicion;

import java.util.ArrayList;
import java.util.List;

public class PlanVueloDTO {

    private String idDrone;
    private String idPiloto;
    private String fechaInicio;
    private String fechaFin;
    private double duracionHoras;
    private List<PosicionDTO> posiciones;

    public PlanVueloDTO() {
    }

    public PlanVueloDTO(PlanVuelo plan) {

        this.idDrone = plan.getDrone().getId();
        this.idPiloto = plan.getPiloto().getId();

        this.fechaInicio =
                plan.getFechaInicio().toString();

        this.fechaFin =
                plan.getFechaFin().toString();

        this.duracionHoras = plan.getDuracionHoras();

        this.posiciones = new ArrayList<>();

        for (Posicion p : plan.getPosiciones()) {

            PosicionDTO dto = new PosicionDTO();

            dto.setLatitud(p.getLatitud());
            dto.setLongitud(p.getLongitud());

            this.posiciones.add(dto);
        }
    }

    public String getIdDrone() {
        return idDrone;
    }

    public String getIdPiloto() {
        return idPiloto;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public double getDuracionHoras() {
        return duracionHoras;
    }

    public List<PosicionDTO> getPosiciones() {
        return posiciones;
    }
}
