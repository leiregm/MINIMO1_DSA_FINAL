package models;

import java.util.LinkedList;
import java.util.List;

public class Piloto {

    private String id;
    private String nombre;
    private String apellidos;
    private double horasVuelo;

    private List<PlanVuelo> planesDelPiloto =
            new LinkedList<>();

    public Piloto(String id, String nombre, String apellidos)
    {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.horasVuelo = 0;
    }

    public String getId()
    {
        return id;
    }

    public String getNombre()
    {
        return nombre;
    }

    public String getApellidos()
    {
        return apellidos;
    }

    public double getHorasVuelo()
    {
        return horasVuelo;
    }

    public List<PlanVuelo> getPlanesDelPiloto()
    {
        return planesDelPiloto;
    }

    public void addPlanVuelo(PlanVuelo plan)
    {
        planesDelPiloto.add(plan);
    }

    public void addHorasVuelo(double horas)
    {
        this.horasVuelo += horas;
    }
}
