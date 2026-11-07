
package edu.upc.dsa.services;
import models.PlanVuelo;
import edu.upc.dsa.DroneManager;
import edu.upc.dsa.DroneManagerImpl;

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

@Api(value = "/planes", description = "Planes de vuelo")
@Path("/planes")
public class PlanVueloService {

    private DroneManager pm =
            DroneManagerImpl.getInstance();

    // CREAR UN PLAN DE VUELO
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Crear un plan de vuelo")
    public Response crearPlanVuelo(PlanVueloRequest request) {

        // COMPROBAR DATOS OBLIGATORIOS
        if (request == null ||
                request.getIdDrone() == null ||
                request.getIdPiloto() == null ||
                request.getFechaInicio() == null ||
                request.getDuracionHoras() <= 0 ||
                !Double.isFinite(request.getDuracionHoras()) ||
                request.getPosiciones() == null ||
                request.getPosiciones().isEmpty()) {

            return Response.status(400)
                    .entity("Datos incorrectos")
                    .build();
        }

        // COMPROBAR QUE EXISTEN DRON Y PILOTO
        if (pm.getDrone(request.getIdDrone()) == null ||
                pm.getPiloto(request.getIdPiloto()) == null) {

            return Response.status(404)
                    .entity("Dron o piloto no encontrado")
                    .build();
        }

        try {

            // CONVERTIR LA FECHA
            LocalDateTime fechaInicio =
                    LocalDateTime.parse(request.getFechaInicio());

            // CONVERTIR POSICIONES DTO A MODELOS
            List<Posicion> posiciones = new ArrayList<>();

            for (PosicionDTO p : request.getPosiciones()) {

                if (p == null ||
                        !Double.isFinite(p.getLatitud()) ||
                        !Double.isFinite(p.getLongitud())) {

                    return Response.status(400)
                            .entity("Posicion incorrecta")
                            .build();
                }

                posiciones.add(
                        new Posicion(
                                p.getLatitud(),
                                p.getLongitud()
                        )
                );
            }

            // LLAMAR AL GESTOR DE LA PARTE II
            pm.crearPlanVuelo(
                    request.getIdDrone(),
                    request.getIdPiloto(),
                    fechaInicio,
                    request.getDuracionHoras(),
                    posiciones
            );

            return Response.status(201)
                    .entity("Plan de vuelo creado correctamente")
                    .build();

        } catch (DateTimeParseException e) {

            return Response.status(400)
                    .entity("Formato de fecha incorrecto")
                    .build();

        } catch (IllegalArgumentException e) {

            return Response.status(400)
                    .entity(e.getMessage())
                    .build();

        } catch (IllegalStateException e) {

            return Response.status(409)
                    .entity(e.getMessage())
                    .build();
        }
    }

    // CONSULTAR PLANES DE UN PILOTO
    @GET
    @Path("/pilotos/{idPiloto}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Consultar planes de un piloto")
    public Response getPlanesByPiloto(
            @PathParam("idPiloto") String idPiloto) {

        if (pm.getPiloto(idPiloto) == null) {
            return Response.status(404)
                    .entity("Piloto no encontrado")
                    .build();
        }

        List<PlanVueloDTO> resultado = new ArrayList<>();

        for (PlanVuelo plan : pm.getPlanesByPiloto(idPiloto)) {
            resultado.add(new PlanVueloDTO(plan));
        }

        return Response.ok(resultado).build();
    }

    // CONSULTAR PLANES DE UN DRON
    @GET
    @Path("/drones/{idDrone}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Consultar planes de un dron")
    public Response getPlanesByDrone(
            @PathParam("idDrone") String idDrone) {

        if (pm.getDrone(idDrone) == null) {
            return Response.status(404)
                    .entity("Dron no encontrado")
                    .build();
        }

        List<PlanVueloDTO> resultado = new ArrayList<>();

        for (PlanVuelo plan : pm.getPlanesByDrone(idDrone)) {
            resultado.add(new PlanVueloDTO(plan));
        }

        return Response.ok(resultado).build();
    }

}
