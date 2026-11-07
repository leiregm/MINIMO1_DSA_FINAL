
package edu.upc.dsa;

import models.Drone;
import models.Piloto;
import models.PlanVuelo;
import models.Posicion;

import java.time.LocalDateTime;
import java.util.List;

public interface DroneManager {

    // Añadir un nuevo dron
    void addDrone(String id, String nombre,
                  String fabricante, String modelo);

    // Añadir un nuevo piloto
    void addPiloto(String id, String nombre,
                   String apellidos);

    // Drones ordenados descendentemente
    // por horas de vuelo
    List<Drone> getDronesByHours();

    // Pilotos ordenados descendentemente
    // por horas de vuelo
    List<Piloto> getPilotosByHours();

    // Guardar un dron para mantenimiento
    void guardarDrone(String idDrone);

    // Reparar el dron más cercano a la puerta
    Drone repararDrone();

    // Crear un plan de vuelo
    PlanVuelo crearPlanVuelo(
            String idDrone,
            String idPiloto,
            LocalDateTime fechaInicio,
            double duracionHoras,
            List<Posicion> posiciones
    );

    // Consultar los planes de un piloto
    List<PlanVuelo> getPlanesByPiloto(String idPiloto);
    // Consultar los planes de un dron
    List<PlanVuelo> getPlanesByDrone(String idDrone);

    // Métodos auxiliares para consultas y tests
    Drone getDrone(String idDrone);

    Piloto getPiloto(String idPiloto);
}
