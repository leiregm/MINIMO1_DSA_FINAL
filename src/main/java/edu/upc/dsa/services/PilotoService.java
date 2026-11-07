
package edu.upc.dsa.services;

import edu.upc.dsa.DroneManager;
import edu.upc.dsa.DroneManagerImpl;

import models.Piloto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Api(value = "/pilotos", description = "Piloto Service")
@Path("/pilotos")
public class PilotoService {

    private DroneManager pm =
            DroneManagerImpl.getInstance();

    // AÑADIR UN PILOTO
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add a new pilot")
    public Response addPiloto(PilotoDTO request) {

        if (request == null ||
                request.getId() == null ||
                request.getNombre() == null ||
                request.getApellidos() == null) {

            return Response.status(400).build();
        }

        // Comprobar si ya existe
        if (pm.getPiloto(request.getId()) != null) {
            return Response.status(409).build();
        }

        try {
            pm.addPiloto(
                    request.getId(),
                    request.getNombre(),
                    request.getApellidos()
            );

            return Response.status(201).build();

        } catch (IllegalArgumentException e) {
            return Response.status(400).build();
        }
    }

    // PILOTOS ORDENADOS POR HORAS DE VUELO
    @GET
    @Path("/hours")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Get pilots ordered by flight hours"
    )
    public Response getPilotosByHours() {

        List<PilotoDTO> resultado = new ArrayList<>();

        for (Piloto piloto : pm.getPilotosByHours()) {
            resultado.add(new PilotoDTO(piloto));
        }

        return Response.ok(resultado).build();
    }
}
