
package edu.upc.dsa;

import models.Drone;
import models.Piloto;
import models.PlanVuelo;
import models.Posicion;

import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.*;

public class DroneManagerImpl implements DroneManager {

    // SINGLETON
    private static DroneManagerImpl instance;

    private static final Logger logger =
            Logger.getLogger(DroneManagerImpl.class);

    // ESTRUCTURAS DE DATOS
    private Map<String, Drone> drones;
    private Map<String, Piloto> pilotos;
    private Queue<Drone> almacen;

    // Constructor privado
    private DroneManagerImpl() {
        logger.info("Inicio constructor");

        drones = new HashMap<>();
        pilotos = new HashMap<>();
        almacen = new LinkedList<>();

        logger.info("Fin constructor");
    }

    // Obtener instancia Singleton
    public static DroneManagerImpl getInstance() {
        logger.info("Inicio getInstance");

        if (instance == null) {
            instance = new DroneManagerImpl();
        }

        logger.info("Fin getInstance");
        return instance;
    }

    // AÑADIR DRON
    @Override
    public void addDrone(String id, String nombre,
                         String fabricante, String modelo) {

        logger.info("Inicio addDrone: " + id + ", " +
                nombre + ", " + fabricante + ", " + modelo);

        try {
            if (id == null || id.trim().isEmpty() ||
                    nombre == null || fabricante == null ||
                    modelo == null) {
                logger.error("Datos de dron incorrectos");
                throw new IllegalArgumentException(
                        "Datos de dron incorrectos");
            }

            if (drones.containsKey(id)) {
                logger.error("Dron duplicado: " + id);
                throw new IllegalArgumentException(
                        "El dron ya existe");
            }

            Drone drone =
                    new Drone(id, nombre, fabricante, modelo);

            drones.put(id, drone);

        } finally {
            logger.info("Fin addDrone: " + id + ", " + nombre + ", " + fabricante + ", " + modelo);        }
    }

    // AÑADIR PILOTO
    @Override
    public void addPiloto(String id, String nombre,
                          String apellidos) {

        logger.info("Inicio addPiloto: " + id + ", " +
                nombre + ", " + apellidos);

        try {
            if (id == null || id.trim().isEmpty() ||
                    nombre == null || apellidos == null) {
                logger.error("Datos de piloto incorrectos");
                throw new IllegalArgumentException(
                        "Datos de piloto incorrectos");
            }

            if (pilotos.containsKey(id)) {
                logger.error("Piloto duplicado: " + id);
                throw new IllegalArgumentException(
                        "El piloto ya existe");
            }

            Piloto piloto = new Piloto(id, nombre, apellidos);

            pilotos.put(id, piloto);

        } finally {
            logger.info("Fin addPiloto: " + id + ", " + nombre + ", " + apellidos);
        }
    }

    // DRONES ORDENADOS DESCENDENTEMENTE POR HORAS
    @Override
    public List<Drone> getDronesByHours() {

        logger.info("Inicio getDronesByHours");

        try {
            List<Drone> lista =
                    new ArrayList<>(drones.values());

            lista.sort(
                    Comparator.comparingDouble(
                            Drone::getHorasVuelo
                    ).reversed()
            );

            return lista;

        } finally {
            logger.info("Fin getDronesByHours");
        }
    }

    // PILOTOS ORDENADOS DESCENDENTEMENTE POR HORAS
    @Override
    public List<Piloto> getPilotosByHours() {

        logger.info("Inicio getPilotosByHours");

        try {
            List<Piloto> lista =
                    new ArrayList<>(pilotos.values());

            lista.sort(
                    Comparator.comparingDouble(
                            Piloto::getHorasVuelo
                    ).reversed()
            );

            return lista;

        } finally {
            logger.info("Fin getPilotosByHours");
        }
    }

    // GUARDAR DRON EN EL ALMACEN
    @Override
    public void guardarDrone(String idDrone) {

        logger.info("Inicio guardarDrone: " + idDrone);

        try {
            Drone drone = drones.get(idDrone);

            if (drone == null) {
                logger.error("Dron no encontrado: " + idDrone);
                throw new IllegalArgumentException(
                        "Dron no encontrado");
            }

            if (drone.isEnMantenimiento()) {
                logger.error("Dron ya en mantenimiento: " +
                        idDrone);
                throw new IllegalStateException(
                        "Dron ya en mantenimiento");
            }

            drone.setEnMantenimiento(true);
            almacen.add(drone);

        } finally {
            logger.info("Fin guardarDrone: " + idDrone);
        }
    }

    // REPARAR EL PRIMER DRON DEL ALMACEN
    @Override
    public Drone repararDrone() {

        logger.info("Inicio repararDrone");

        Drone drone = null;

        try {
            drone = almacen.poll();

            if (drone == null) {
                logger.error("Almacen vacio");
                throw new IllegalStateException(
                        "No hay drones en el almacen");
            }

            drone.setEnMantenimiento(false);

            return drone;

        } finally {
            logger.info("Fin repararDrone. Dron: " +
                    (drone == null ? "ninguno" : drone.getId()));
        }
    }

    // CREAR PLAN DE VUELO
    @Override
    public PlanVuelo crearPlanVuelo(
            String idDrone,
            String idPiloto,
            LocalDateTime fechaInicio,
            double duracionHoras,
            List<Posicion> posiciones) {


        logger.info("Inicio crearPlanVuelo: " + "idDrone=" + idDrone + ", idPiloto=" + idPiloto + ", fechaInicio=" + fechaInicio + ", duracionHoras=" + duracionHoras + ", posiciones=" + posiciones);

        try {
            Drone drone = drones.get(idDrone);
            Piloto piloto = pilotos.get(idPiloto);

            if (drone == null || piloto == null) {
                logger.error("Dron o piloto no encontrado");
                throw new IllegalArgumentException(
                        "Dron o piloto no encontrado");
            }

            if (fechaInicio == null ||
                    !Double.isFinite(duracionHoras) ||
                    duracionHoras <= 0 ||
                    posiciones == null ||
                    posiciones.isEmpty()) {

                logger.error("Datos del plan incorrectos");
                throw new IllegalArgumentException(
                        "Datos del plan incorrectos");
            }

            // Comprobar mantenimiento
            if (drone.isEnMantenimiento()) {
                logger.error("Dron en mantenimiento: " +
                        idDrone);

                throw new IllegalStateException(
                        "Dron no operativo");
            }

            // Calcular la fecha de finalizacion
            LocalDateTime fechaFin =
                    fechaInicio.plusSeconds(
                            Math.round(duracionHoras * 3600)
                    );

            // Comprobar conflictos del dron
            for (PlanVuelo plan : drone.getPlanesDelDron()) {

                if (plan.seSolapa(fechaInicio, fechaFin)) {
                    logger.error("Conflicto de horario del dron");

                    throw new IllegalStateException(
                            "Dron ocupado en ese horario");
                }
            }

            // Comprobar conflictos del piloto
            for (PlanVuelo plan : piloto.getPlanesDelPiloto()) {

                if (plan.seSolapa(fechaInicio, fechaFin)) {
                    logger.error("Conflicto de horario del piloto");

                    throw new IllegalStateException(
                            "Piloto ocupado en ese horario");
                }
            }

            // Crear el nuevo plan
            PlanVuelo nuevoPlan = new PlanVuelo(
                    drone, piloto, fechaInicio,
                    duracionHoras, posiciones
            );

            // Guardarlo en ambas listas
            drone.addPlanVuelo(nuevoPlan);
            piloto.addPlanVuelo(nuevoPlan);

            // Contabilizar las horas planificadas
            drone.addHorasVuelo(duracionHoras);
            piloto.addHorasVuelo(duracionHoras);

            return nuevoPlan;

        } finally {
            logger.info("Fin crearPlanVuelo: " + "idDrone=" + idDrone + ", idPiloto=" + idPiloto + ", fechaInicio=" + fechaInicio + ", duracionHoras=" + duracionHoras + ", posiciones=" + posiciones);

        }
    }

    // PLANES DE VUELO DE UN PILOTO
    @Override
    public List<PlanVuelo> getPlanesByPiloto(String idPiloto) {

        logger.info("Inicio getPlanesByPiloto: " + idPiloto);

        try {
            Piloto piloto = pilotos.get(idPiloto);

            if (piloto == null) {
                logger.error("Piloto no encontrado: " + idPiloto);
                throw new IllegalArgumentException(
                        "Piloto no encontrado");
            }

            return new ArrayList<>(piloto.getPlanesDelPiloto());

        } finally {
            logger.info("Fin getPlanesByPiloto: " + idPiloto);
        }
    }

    // PLANES DE VUELO DE UN DRON
    @Override
    public List<PlanVuelo> getPlanesByDrone(String idDrone) {

        logger.info("Inicio getPlanesByDrone: " + idDrone);

        try {
            Drone drone = drones.get(idDrone);

            if (drone == null) {
                logger.error("Dron no encontrado: " + idDrone);
                throw new IllegalArgumentException(
                        "Dron no encontrado");
            }
            return new ArrayList<>(drone.getPlanesDelDron());

        } finally {
            logger.info("Fin getPlanesByDrone: " + idDrone);
        }
    }

    // CONSULTAR DRON
    @Override
    public Drone getDrone(String idDrone) {

        logger.info("Inicio getDrone: " + idDrone);

        try {
            return drones.get(idDrone);

        } finally {
            logger.info("Fin getDrone: " + idDrone);
        }
    }

    // CONSULTAR PILOTO
    @Override
    public Piloto getPiloto(String idPiloto) {

        logger.info("Inicio getPiloto: " + idPiloto);

        try {
            return pilotos.get(idPiloto);

        } finally {
            logger.info("Fin getPiloto: " + idPiloto);
        }
    }

    // LIMPIAR DATOS PARA LOS TESTS
    public void clear() {

        logger.info("Inicio clear");

        try {
            drones.clear();
            pilotos.clear();
            almacen.clear();

        } finally {
            logger.info("Fin clear");
        }
    }
}
