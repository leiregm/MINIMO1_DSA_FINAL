
package edu.upc.dsa.services;

import edu.upc.dsa.DronManager;
import edu.upc.dsa.DronManagerImpl;
import models.Dron;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Api(value = "/drones", description = "Gestion de drones")
@Path("/drones")
public class GestionDrones {

    private DronManager pm = DronManagerImpl.getInstance();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Añadir un dron")
    public Response addDrone(DronDatos datos) {

        if (datos == null || datos.getId() == null ||
                datos.getNombre() == null ||
                datos.getFabricante() == null ||
                datos.getModelo() == null) {

            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (pm.getDrone(datos.getId()) != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        try
        {
            pm.addDrone(datos.getId(), datos.getNombre(), datos.getFabricante(), datos.getModelo());
            return Response.status(201).build();
        }
        catch (IllegalArgumentException e)
        {
            return Response.status(400).build();
        }
    }

    @GET
    @Path("/hours")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Listar drones por horas de vuelo")
    public Response getDronesByHours()
    {
        List<DronDatos> resultado = new ArrayList<>();

        for (Dron drone : pm.getDronesByHours())
        {
            resultado.add(new DronDatos(drone));
        }

        return Response.ok(resultado).build();
    }
}
