
package edu.upc.dsa;

import models.Dron;
import models.Piloto;
import models.PlanVuelo;
import models.Posicion;

import java.time.LocalDateTime;
import java.util.List;

public interface DronManager {

    void addDrone(String id, String nombre, String fabricante, String modelo);

    void addPiloto(String id, String nombre, String apellidos);

    List<Dron> getDronesByHours();

    List<Piloto> getPilotosByHours();

    void guardarDrone(String idDrone);

    Dron repararDrone();

    PlanVuelo crearPlanVuelo(String idDrone, String idPiloto, LocalDateTime fechaInicio, double duracionHoras, List<Posicion> posiciones);

    List<PlanVuelo> getPlanesByPiloto(String idPiloto);

    List<PlanVuelo> getPlanesByDrone(String idDrone);

    Dron getDrone(String idDrone);

    Piloto getPiloto(String idPiloto);
}
