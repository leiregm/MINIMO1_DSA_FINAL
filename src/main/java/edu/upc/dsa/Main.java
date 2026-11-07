
package edu.upc.dsa;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import java.io.IOException;
import java.net.URI;

public class Main {

    private static final Logger logger =
            Logger.getLogger(Main.class);

    public static final String BASE_URI =
            "http://localhost:8080/dsaApp/";

    // INICIAR EL SERVIDOR REST
    public static HttpServer startServer() {

        ResourceConfig rc = new ResourceConfig()
                .packages("edu.upc.dsa.services");

        // JSON
        rc.register(JacksonFeature.class);

        // SWAGGER
        rc.register(ApiListingResource.class);
        rc.register(SwaggerSerializers.class);

        BeanConfig beanConfig = new BeanConfig();

        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/dsaApp");
        beanConfig.setTitle("DroneManager API");
        beanConfig.setDescription(
                "REST API for Drone Management"
        );
        beanConfig.setVersion("1.0.0");
        beanConfig.setResourcePackage(
                "edu.upc.dsa.services"
        );
        beanConfig.setScan(true);

        return GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_URI), rc
        );
    }

    public static void main(String[] args)
            throws IOException {

        logger.info("Inicio Main");

        // Obtener la instancia Singleton
        DroneManager pm =
                DroneManagerImpl.getInstance();

        // Iniciar servidor
        HttpServer server = startServer();

        // Mostrar Swagger UI desde public
        StaticHttpHandler staticHttpHandler =
                new StaticHttpHandler("./public/");

        server.getServerConfiguration()
                .addHttpHandler(staticHttpHandler, "/");

        logger.info("Servidor REST iniciado: " + BASE_URI);

        // Mantener el servidor en funcionamiento
        System.in.read();

        server.shutdownNow();

        logger.info("Fin Main");
    }
}

