
package edu.upc.dsa.services;

import edu.upc.dsa.DronManager;
import edu.upc.dsa.DronManagerImpl;
import models.Piloto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Api(value = "/pilotos", description = "Gestion de pilotos")
@Path("/pilotos")
public class GestionPilotos
{
    private final DronManager manager = DronManagerImpl.getInstance();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Añadir un piloto")
    public Response addPiloto(PilotoDatos datos)
    {
        if (datos == null || datos.getId() == null || datos.getNombre() == null || datos.getApellidos() == null)
        {
            return Response.status(400).build();
        }

        if (manager.getPiloto(datos.getId()) != null)
        {
            return Response.status(409).build();
        }

        try
        {
            manager.addPiloto(datos.getId(), datos.getNombre(), datos.getApellidos());
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
    @ApiOperation(value = "Listar pilotos por horas de vuelo")
    public Response getPilotosByHours()
    {

        List<PilotoDatos> pilotosordenados = new ArrayList<>();

        for (Piloto piloto : manager.getPilotosByHours())
        {
            pilotosordenados.add(new PilotoDatos(piloto));
        }

        return Response.ok(pilotosordenados).build();
    }
}
