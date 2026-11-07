import edu.upc.dsa.DroneManager;
import edu.upc.dsa.DroneManagerImpl;
import models.Drone;
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

    private DroneManagerImpl pm;

    @Before
    public void setUp() {

        pm = DroneManagerImpl.getInstance();
        pm.clear();

        // Drones de prueba
        pm.addDrone("D1", "Drone 1", "DJI", "Mini 4");
        pm.addDrone("D2", "Drone 2", "DJI", "Air 3");

        // Pilotos de prueba
        pm.addPiloto("P1", "Juan", "Garcia");
        pm.addPiloto("P2", "Ana", "Lopez");
    }

    @After
    public void tearDown() {
        pm.clear();
    }

    // TEST 1: AÑADIR DRONES Y PILOTOS
    @Test
    public void testAddDroneYPiloto() {

        assertNotNull(pm.getDrone("D1"));
        assertNotNull(pm.getDrone("D2"));

        assertNotNull(pm.getPiloto("P1"));
        assertNotNull(pm.getPiloto("P2"));

        assertEquals("DJI",
                pm.getDrone("D1").getFabricante());

        assertEquals("Juan",
                pm.getPiloto("P1").getNombre());
    }

    // TEST 2: ORDENAR POR HORAS DESCENDENTES
    @Test
    public void testOrdenarPorHoras() {

        LocalDateTime fecha =
                LocalDateTime.of(2026, 11, 6, 10, 0);

        List<Posicion> posiciones = Arrays.asList(
                new Posicion(41.27, 1.98),
                new Posicion(41.30, 2.00)
        );

        // D1 y P1 tienen 4 horas
        pm.crearPlanVuelo(
                "D1", "P1", fecha, 4, posiciones
        );

        // D2 y P2 tienen 2 horas
        pm.crearPlanVuelo(
                "D2", "P2", fecha, 2, posiciones
        );

        assertEquals("D1",
                pm.getDronesByHours().get(0).getId());

        assertEquals("D2",
                pm.getDronesByHours().get(1).getId());

        assertEquals("P1",
                pm.getPilotosByHours().get(0).getId());

        assertEquals("P2",
                pm.getPilotosByHours().get(1).getId());
    }

    // TEST 3: ALMACEN Y MANTENIMIENTO FIFO
    @Test
    public void testAlmacenFIFO() {

        pm.guardarDrone("D1");
        pm.guardarDrone("D2");

        assertTrue(pm.getDrone("D1").isEnMantenimiento());
        assertTrue(pm.getDrone("D2").isEnMantenimiento());

        // Se repara primero D1
        Drone primero = pm.repararDrone();

        assertEquals("D1", primero.getId());
        assertFalse(primero.isEnMantenimiento());

        // Después se repara D2
        Drone segundo = pm.repararDrone();

        assertEquals("D2", segundo.getId());
        assertFalse(segundo.isEnMantenimiento());
    }

    // TEST 4: PLANES DE VUELO Y CONFLICTOS
    @Test
    public void testPlanVueloYConflictos() {

        LocalDateTime fecha =
                LocalDateTime.of(2026, 11, 6, 10, 0);

        List<Posicion> posiciones = Arrays.asList(
                new Posicion(41.27, 1.98),
                new Posicion(41.30, 2.00)
        );

        // Crear plan de vuelo correctamente
        PlanVuelo plan = pm.crearPlanVuelo(
                "D1", "P1", fecha, 2, posiciones
        );

        assertNotNull(plan);

        // El plan aparece en ambos historiales
        assertEquals(1,
                pm.getPlanesByDrone("D1").size());

        assertEquals(1,
                pm.getPlanesByPiloto("P1").size());

        // Error: el dron D1 ya está ocupado
        assertThrows(IllegalStateException.class, () ->
                pm.crearPlanVuelo(
                        "D1", "P2",
                        fecha.plusHours(1),
                        1, posiciones
                )
        );

        // Error: el piloto P1 ya está ocupado
        assertThrows(IllegalStateException.class, () ->
                pm.crearPlanVuelo(
                        "D2", "P1",
                        fecha.plusHours(1),
                        1, posiciones
                )
        );

        // Error: D2 está en mantenimiento
        pm.guardarDrone("D2");

        assertThrows(IllegalStateException.class, () ->
                pm.crearPlanVuelo(
                        "D2", "P2",
                        fecha.plusDays(1),
                        1, posiciones
                )
        );
    }
}
