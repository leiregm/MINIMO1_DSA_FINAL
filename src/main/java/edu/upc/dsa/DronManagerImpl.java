
package edu.upc.dsa;

import models.Dron;
import models.Piloto;
import models.PlanVuelo;
import models.Posicion;

import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.*;

public class DronManagerImpl implements DronManager
{

    private static DronManagerImpl instance;
    private static final Logger logger = Logger.getLogger(DronManagerImpl.class);

    private final Map<String, Dron> drones;
    private final Map<String, Piloto> pilotos;
    private final Queue<Dron> almacen;

    private DronManagerImpl()
    {
        logger.info("Inicio constructor");

        drones = new HashMap<>();
        pilotos = new HashMap<>();
        almacen = new LinkedList<>();

        logger.info("Fin constructor");
    }

    public static DronManagerImpl getInstance()
    {
        logger.info("Inicio getInstance");

        if (instance == null)
        {
            instance = new DronManagerImpl();
        }

        logger.info("Fin getInstance");
        return instance;
    }

    @Override
    public void addDrone(String id, String nombre, String fabricante, String modelo)
    {
        logger.info("Inicio addDrone: " + id + ", " + nombre + ", " + fabricante + ", " + modelo);

        try
        {
            if (id == null || id.trim().isEmpty() || nombre == null || nombre.trim().isEmpty() || fabricante == null || fabricante.trim().isEmpty() || modelo == null || modelo.trim().isEmpty())
            {
                logger.error("Datos incorrectos al añadir el dron");
                throw new IllegalArgumentException("Datos del dron incorrectos");
            }

            if (drones.containsKey(id))
            {
                logger.error("El dron " + id + " ya existe");
                throw new IllegalArgumentException("El dron ya existe");
            }

            Dron dron = new Dron(id, nombre, fabricante, modelo);
            drones.put(id, dron);

        }
        finally
        {
            logger.info("Fin addDrone: " + id + ", " + nombre + ", " + fabricante + ", " + modelo);
        }
    }

    @Override
    public void addPiloto(String id, String nombre, String apellidos)
    {
        logger.info("Inicio addPiloto: " + id + ", " + nombre + ", " + apellidos);

        try
        {
            if (id == null || id.trim().isEmpty() || nombre == null || nombre.trim().isEmpty() || apellidos == null || apellidos.trim().isEmpty())
            {
                logger.error("Datos incorrectos al añadir el piloto");
                throw new IllegalArgumentException("Datos del piloto incorrectos");
            }

            if (pilotos.containsKey(id))
            {
                logger.error("El piloto " + id + " ya existe");
                throw new IllegalArgumentException("El piloto ya existe");
            }

            Piloto piloto = new Piloto(id, nombre, apellidos);
            pilotos.put(id, piloto);

        }
        finally
        {
            logger.info("Fin addPiloto: " + id + ", " + nombre + ", " + apellidos);
        }
    }

    @Override
    public List<Dron> getDronesByHours()
    {
        logger.info("Inicio getDronesByHours");

        try
        {
            List<Dron> dronesordenados = new ArrayList<>(drones.values());
            dronesordenados.sort(Comparator.comparingDouble(Dron::getHorasVuelo).reversed());
            return dronesordenados;

        }
        finally
        {
            logger.info("Fin getDronesByHours");
        }
    }

    @Override
    public List<Piloto> getPilotosByHours()
    {
        logger.info("Inicio getPilotosByHours");

        try
        {
            List<Piloto> pilotosOrdenados = new ArrayList<>(pilotos.values());
            pilotosOrdenados.sort(Comparator.comparingDouble(Piloto::getHorasVuelo).reversed());
            return pilotosOrdenados;
        }
        finally
        {
            logger.info("Fin getPilotosByHours");
        }
    }

    @Override
    public void guardarDrone(String idDrone)
    {
        logger.info("Inicio guardarDrone: " + idDrone);

        try
        {
            Dron dron = drones.get(idDrone);

            if (dron == null)
            {
                logger.error("Dron no encontrado: " + idDrone);
                throw new IllegalArgumentException("Dron no encontrado");
            }

            if (dron.isEnMantenimiento())
            {
                logger.error("Dron ya en mantenimiento: " + idDrone);
                throw new IllegalStateException("Dron ya en mantenimiento");
            }

            //El ultimo dron en entrar se coloca al final de la cola, es decir, FIFO
            dron.setEnMantenimiento(true);
            almacen.add(dron);

        }
        finally
        {
            logger.info("Fin guardarDrone: " + idDrone);
        }
    }

    @Override
    public Dron repararDrone()
    {
        logger.info("Inicio repararDrone");

        Dron dron = null;

        try
        {
            dron = almacen.poll();

            if (dron == null)
            {
                logger.error("Almacen vacio");
                throw new IllegalStateException("No hay drones en el almacen");
            }

            dron.setEnMantenimiento(false);
            return dron;

        }
        finally
        {
            logger.info("Fin repararDrone: " +
                    (dron == null ? "ninguno" : dron.getId()));
        }
    }

    @Override
    public PlanVuelo crearPlanVuelo(String idDrone, String idPiloto, LocalDateTime fechaInicio, double duracionHoras, List<Posicion> posiciones) {

        String parametros = "Dron: " + idDrone + ", Piloto: " + idPiloto +
                ", Fecha: " + fechaInicio + ", Duracion: " + duracionHoras +
                ", Posiciones: " + posiciones;

        logger.info("Inicio crearPlanVuelo: " + parametros);

        try
        {
            Dron dron = drones.get(idDrone);
            Piloto piloto = pilotos.get(idPiloto);

            if (dron == null || piloto == null)
            {
                logger.error("Dron o piloto no encontrado");
                throw new IllegalArgumentException("Dron o piloto no encontrado");
            }

            if (fechaInicio == null || !Double.isFinite(duracionHoras) || duracionHoras <= 0 || posiciones == null || posiciones.isEmpty())
            {
                logger.error("Datos del plan incorrectos");
                throw new IllegalArgumentException("Datos del plan incorrectos");
            }

            if (dron.isEnMantenimiento())
            {
                logger.error("Dron en mantenimiento: " + idDrone);
                throw new IllegalStateException("Dron en mantenimiento");
            }

            LocalDateTime fechaFin = fechaInicio.plusSeconds(
                    Math.round(duracionHoras * 3600)
            );

            for (PlanVuelo plan : dron.getPlanesDelDron())
            {
                if (plan.seSolapa(fechaInicio, fechaFin))
                {
                    logger.error("Conflicto de horario del dron");
                    throw new IllegalStateException("Dron ocupado en ese horario");
                }
            }

            for (PlanVuelo plan : piloto.getPlanesDelPiloto())
            {
                if (plan.seSolapa(fechaInicio, fechaFin))
                {
                    logger.error("Conflicto de horario del piloto");
                    throw new IllegalStateException("Piloto ocupado en ese horario");
                }
            }

            PlanVuelo nuevoPlan = new PlanVuelo(
                    dron, piloto, fechaInicio, duracionHoras, posiciones
            );

            dron.addPlanVuelo(nuevoPlan);
            piloto.addPlanVuelo(nuevoPlan);

            dron.addHorasVuelo(duracionHoras);
            piloto.addHorasVuelo(duracionHoras);

            return nuevoPlan;

        }
        finally
        {
            logger.info("Fin crearPlanVuelo: " + parametros);
        }
    }

    @Override
    public List<PlanVuelo> getPlanesByPiloto(String idPiloto)
    {
        logger.info("Inicio getPlanesByPiloto: " + idPiloto);

        try
        {
            Piloto piloto = pilotos.get(idPiloto);

            if (piloto == null)
            {
                logger.error("Piloto no encontrado: " + idPiloto);
                throw new IllegalArgumentException("Piloto no encontrado");
            }

            return new ArrayList<>(piloto.getPlanesDelPiloto());

        }
        finally
        {
            logger.info("Fin getPlanesByPiloto: " + idPiloto);
        }
    }

    @Override
    public List<PlanVuelo> getPlanesByDrone(String idDrone)
    {
        logger.info("Inicio getPlanesByDrone: " + idDrone);

        try
        {
            Dron dron = drones.get(idDrone);

            if (dron == null)
            {
                logger.error("Dron no encontrado: " + idDrone);
                throw new IllegalArgumentException("Dron no encontrado");
            }

            return new ArrayList<>(dron.getPlanesDelDron());

        }
        finally
        {
            logger.info("Fin getPlanesByDrone: " + idDrone);
        }
    }

    @Override
    public Dron getDrone(String idDrone)
    {
        logger.info("Inicio getDrone: " + idDrone);

        try
        {
            return drones.get(idDrone);
        }
        finally
        {
            logger.info("Fin getDrone: " + idDrone);
        }
    }

    @Override
    public Piloto getPiloto(String idPiloto)
    {
        logger.info("Inicio getPiloto: " + idPiloto);

        try
        {
            return pilotos.get(idPiloto);
        }
        finally
        {
            logger.info("Fin getPiloto: " + idPiloto);
        }
    }

    public void clear()
    {
        logger.info("Inicio clear");

        try
        {
            drones.clear();
            pilotos.clear();
            almacen.clear();
        }
        finally
        {
            logger.info("Fin clear");
        }
    }
}
