
import edu.upc.dsa.DronManagerImpl;
import models.Dron;
import models.PlanVuelo;
import models.Posicion;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DroneManagerTest {

    private DronManagerImpl manager;

    @Before
    public void setUp() {
        manager = DronManagerImpl.getInstance();
        manager.clear();

        manager.addDrone("D1", "Drone 1", "DJI", "Mini 4");
        manager.addDrone("D2", "Drone 2", "DJI", "Air 3");

        manager.addPiloto("P1", "Juan", "Garcia");
        manager.addPiloto("P2", "Ana", "Lopez");
    }

    @After
    public void tearDown() {
        manager.clear();
    }

    @Test
    public void testAddDroneYPiloto() {
        assertNotNull(manager.getDrone("D1"));
        assertNotNull(manager.getDrone("D2"));
        assertNotNull(manager.getPiloto("P1"));
        assertNotNull(manager.getPiloto("P2"));

        assertEquals("DJI", manager.getDrone("D1").getFabricante());
        assertEquals("Juan", manager.getPiloto("P1").getNombre());
    }

    @Test
    public void testOrdenarPorHoras() {
        LocalDateTime fecha = LocalDateTime.of(2026, 11, 6, 10, 0);

        List<Posicion> posiciones = Arrays.asList(
                new Posicion(41.27, 1.98),
                new Posicion(41.30, 2.00)
        );

        manager.crearPlanVuelo("D1", "P1", fecha, 4, posiciones);
        manager.crearPlanVuelo("D2", "P2", fecha, 2, posiciones);

        assertEquals("D1", manager.getDronesByHours().get(0).getId());
        assertEquals("D2", manager.getDronesByHours().get(1).getId());

        assertEquals("P1", manager.getPilotosByHours().get(0).getId());
        assertEquals("P2", manager.getPilotosByHours().get(1).getId());
    }

    @Test
    public void testAlmacenFIFO() {
        manager.guardarDrone("D1");
        manager.guardarDrone("D2");

        assertTrue(manager.getDrone("D1").isEnMantenimiento());
        assertTrue(manager.getDrone("D2").isEnMantenimiento());

        Dron primero = manager.repararDrone();

        assertEquals("D1", primero.getId());
        assertFalse(primero.isEnMantenimiento());

        Dron segundo = manager.repararDrone();

        assertEquals("D2", segundo.getId());
        assertFalse(segundo.isEnMantenimiento());
    }

    @Test
    public void testPlanVueloYConflictos() {
        LocalDateTime fecha = LocalDateTime.of(2026, 11, 6, 10, 0);

        List<Posicion> posiciones = Arrays.asList(
                new Posicion(41.27, 1.98),
                new Posicion(41.30, 2.00)
        );

        PlanVuelo plan = manager.crearPlanVuelo(
                "D1", "P1", fecha, 2, posiciones
        );

        assertNotNull(plan);
        assertEquals(1, manager.getPlanesByDrone("D1").size());
        assertEquals(1, manager.getPlanesByPiloto("P1").size());

        // El dron ya tiene una reserva en ese horario
        assertThrows(IllegalStateException.class, () ->
                manager.crearPlanVuelo(
                        "D1", "P2", fecha.plusHours(1), 1, posiciones
                )
        );

        // El piloto ya tiene una reserva en ese horario
        assertThrows(IllegalStateException.class, () ->
                manager.crearPlanVuelo(
                        "D2", "P1", fecha.plusHours(1), 1, posiciones
                )
        );

        // No se puede reservar un dron que esta en mantenimiento
        manager.guardarDrone("D2");

        assertThrows(IllegalStateException.class, () ->
                manager.crearPlanVuelo(
                        "D2", "P2", fecha.plusDays(1), 1, posiciones
                )
        );
    }
}
