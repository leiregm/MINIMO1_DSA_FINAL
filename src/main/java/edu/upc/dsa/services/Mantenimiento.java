
package edu.upc.dsa.services;

import edu.upc.dsa.DronManager;
import edu.upc.dsa.DronManagerImpl;
import models.Dron;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/mantenimiento", description = "Mantenimiento de drones")
@Path("/mantenimiento")
public class Mantenimiento {

    private final DronManager manager = DronManagerImpl.getInstance();

    @POST
    @Path("/drones/{idDrone}")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Dron en mantenimiento")
    public Response guardarDrone(@PathParam("idDrone") String idDrone)
    {

        try
        {
            manager.guardarDrone(idDrone);

            return Response.ok("Dron en mantenimiento").build();

        }
        catch (IllegalArgumentException e)
        {
            return Response.status(404).entity("Dron no encontrado").build();

        }
        catch (IllegalStateException e) {
            return Response.status(409).entity("Dron ya en mantenimiento").build();
        }
    }

    @POST
    @Path("/repair")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Reparar siguiente dron")
    public Response repararDrone()
    {
        try
        {
            Dron dron = manager.repararDrone();

            return Response.ok(new DronDatos(dron)).build();

        }
        catch (IllegalStateException e)
        {
            return Response.status(404)
                    .type(MediaType.TEXT_PLAIN).entity("Almacen vacio").build();
        }
    }
}
