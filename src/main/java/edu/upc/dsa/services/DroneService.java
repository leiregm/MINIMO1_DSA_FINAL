
package edu.upc.dsa.services;

import edu.upc.dsa.DroneManager;
import edu.upc.dsa.DroneManagerImpl;
import models.Drone;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Api(value = "/drones", description = "Drone Service")
@Path("/drones")
public class DroneService {

    private DroneManager pm =
            DroneManagerImpl.getInstance();

    // AÑADIR UN DRON
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Add a new drone")
    public Response addDrone(DroneDTO request) {

        if (request == null ||
                request.getId() == null ||
                request.getNombre() == null ||
                request.getFabricante() == null ||
                request.getModelo() == null) {

            return Response.status(400).build();
        }

        if (pm.getDrone(request.getId()) != null) {
            return Response.status(409).build();
        }

        try {
            pm.addDrone(
                    request.getId(),
                    request.getNombre(),
                    request.getFabricante(),
                    request.getModelo()
            );

            return Response.status(201).build();

        } catch (IllegalArgumentException e) {
            return Response.status(400).build();
        }
    }

    // DRONES ORDENADOS POR HORAS DE VUELO
    @GET
    @Path("/hours")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Get drones ordered by flight hours"
    )
    public Response getDronesByHours() {

        List<DroneDTO> resultado = new ArrayList<>();

        for (Drone drone : pm.getDronesByHours()) {
            resultado.add(new DroneDTO(drone));
        }

        return Response.ok(resultado).build();
    }
}
