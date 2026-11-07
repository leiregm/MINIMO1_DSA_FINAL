
package models;

import java.time.LocalDateTime;
import java.util.List;

public class PlanVuelo {

    private Drone drone;
    private Piloto piloto;
    private LocalDateTime fechaInicio;
    private double duracionHoras;
    private List<Posicion> posiciones;

    public PlanVuelo(Drone drone, Piloto piloto,
                     LocalDateTime fechaInicio,
                     double duracionHoras,
                     List<Posicion> posiciones) {

        this.drone = drone;
        this.piloto = piloto;
        this.fechaInicio = fechaInicio;
        this.duracionHoras = duracionHoras;
        this.posiciones = posiciones;
    }

    public Drone getDrone() {
        return drone;
    }

    public Piloto getPiloto() {
        return piloto;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public double getDuracionHoras() {
        return duracionHoras;
    }

    public List<Posicion> getPosiciones() {
        return posiciones;
    }

    public LocalDateTime getFechaFin() {
        return fechaInicio.plusSeconds(
                Math.round(duracionHoras * 3600)
        );
    }

    public boolean seSolapa(
            LocalDateTime inicio,
            LocalDateTime fin) {

        return inicio.isBefore(getFechaFin())
                && fechaInicio.isBefore(fin);
    }
}
