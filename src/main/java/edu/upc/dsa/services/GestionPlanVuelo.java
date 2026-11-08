
package edu.upc.dsa.services;

import edu.upc.dsa.DronManager;
import edu.upc.dsa.DronManagerImpl;
import models.PlanVuelo;
import models.Posicion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Api(value = "/planes", description = "Gestion de planes de vuelo")
@Path("/planes")
public class GestionPlanVuelo
{
    private final DronManager manager = DronManagerImpl.getInstance();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Crear un plan de vuelo")
    public Response crearPlanVuelo(CrearPlanVuelo datos)
    {
        if (datos == null || datos.getIdDrone() == null || datos.getIdPiloto() == null || datos.getFechaInicio() == null || datos.getDuracionHoras() <= 0 || !Double.isFinite(datos.getDuracionHoras()) || datos.getPosiciones() == null || datos.getPosiciones().isEmpty())
        {
            return Response.status(400).entity("Datos incorrectos").build();
        }
        if (manager.getDrone(datos.getIdDrone()) == null || manager.getPiloto(datos.getIdPiloto()) == null)
        {
            return Response.status(404).entity("Dron o piloto no encontrados").build();
        }

        try
        {
            LocalDateTime fechaInicio = LocalDateTime.parse(datos.getFechaInicio());

            List<Posicion> posiciones = new ArrayList<>();

            for (PosicionDatos posicion : datos.getPosiciones())
            {

                if (posicion == null || !Double.isFinite(posicion.getLatitud()) || !Double.isFinite(posicion.getLongitud()))
                {
                    return Response.status(400).entity("Posicion incorrecta").build();
                }

                posiciones.add(new Posicion(posicion.getLatitud(), posicion.getLongitud()));
            }

            manager.crearPlanVuelo(datos.getIdDrone(), datos.getIdPiloto(), fechaInicio, datos.getDuracionHoras(), posiciones);

            return Response.status(201).entity("Plan de vuelo creado correctamente").build();

        }
        catch (DateTimeParseException e)
        {
            return Response.status(400).entity("Formato incorrecto").build();

        }
        catch (IllegalArgumentException e)
        {
            return Response.status(400).entity(e.getMessage()).build();

        }
    }

    @GET
    @Path("/pilotos/{idPiloto}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Consultar planes de un piloto")
    public Response getPlanesByPiloto(@PathParam("idPiloto") String idPiloto)
    {

        if (manager.getPiloto(idPiloto) == null)
        {
            return Response.status(404).entity("Piloto no encontrado").build();
        }

        List<PlanVueloDatos> planes = new ArrayList<>();

        for (PlanVuelo plan : manager.getPlanesByPiloto(idPiloto))
        {
            planes.add(new PlanVueloDatos(plan));
        }

        return Response.ok(planes).build();
    }

    @GET
    @Path("/drones/{idDrone}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Consultar planes de un dron")
    public Response getPlanesByDrone(@PathParam("idDrone") String idDrone)
    {
        if (manager.getDrone(idDrone) == null)
        {
            return Response.status(404).entity("Dron no encontrado").build();
        }

        List<PlanVueloDatos> planes = new ArrayList<>();

        for (PlanVuelo plan : manager.getPlanesByDrone(idDrone))
        {
            planes.add(new PlanVueloDatos(plan));
        }
        return Response.ok(planes).build();
    }
}
