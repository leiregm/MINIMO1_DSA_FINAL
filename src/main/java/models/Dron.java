package models;

import java.util.LinkedList;
import java.util.List;

public class Dron {

    private String id;
    private String nombre;
    private String fabricante;
    private String modelo;
    private double horasVuelo;
    private boolean enMantenimiento;

    private List<PlanVuelo> planesDelDron =
            new LinkedList<>();

    public Dron(String id, String nombre, String fabricante, String modelo)
    {
        this.id = id;
        this.nombre = nombre;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.horasVuelo = 0;
        this.enMantenimiento = false;
    }

    public String getId()
    {
        return id;
    }

    public String getNombre()
    {
        return nombre;
    }

    public String getFabricante()
    {
        return fabricante;
    }

    public String getModelo()
    {
        return modelo;
    }

    public double getHorasVuelo()
    {
        return horasVuelo;
    }

    public boolean isEnMantenimiento()
    {
        return enMantenimiento;
    }

    public void setEnMantenimiento(boolean valor)
    {
        this.enMantenimiento = valor;
    }

    public List<PlanVuelo> getPlanesDelDron()
    {
        return planesDelDron;
    }

    public void addPlanVuelo(PlanVuelo plan)
    {
        planesDelDron.add(plan);
    }

    public void addHorasVuelo(double horas)
    {
        this.horasVuelo += horas;
    }
}

