
package edu.upc.dsa.services;

import edu.upc.dsa.DroneManager;
import edu.upc.dsa.DroneManagerImpl;
import models.Drone;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/mantenimiento",
        description = "Servicio de mantenimiento")
@Path("/mantenimiento")
public class Mantenimiento {

    private DroneManager pm =
            DroneManagerImpl.getInstance();

    @POST
    @Path("/drones/{idDrone}")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Guardar dron en mantenimiento")
    public Response guardarDrone(
            @PathParam("idDrone") String idDrone) {

        try {
            pm.guardarDrone(idDrone);

            return Response.ok(
                    "Dron guardado en mantenimiento"
            ).build();

        } catch (IllegalArgumentException e) {
            return Response.status(404)
                    .entity("Dron no encontrado")
                    .build();

        } catch (IllegalStateException e) {
            return Response.status(409)
                    .entity("Dron ya en mantenimiento")
                    .build();
        }
    }

    @POST
    @Path("/repair")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Reparar siguiente dron")
    public Response repararDrone() {

        try {
            Drone drone = pm.repararDrone();

            return Response.ok(
                    new DroneDTO(drone)
            ).build();

        } catch (IllegalStateException e) {
            return Response.status(404)
                    .entity("Almacen vacio")
                    .build();
        }
    }
}
