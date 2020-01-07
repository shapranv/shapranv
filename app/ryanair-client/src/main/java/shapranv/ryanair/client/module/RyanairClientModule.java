package shapranv.ryanair.client.module;

import lombok.extern.log4j.Log4j2;
import shapranv.ryanair.client.service.AirportService;
import shapranv.ryanair.client.service.FlightService;
import shapranv.ryanair.client.service.RouteService;
import shapranv.shell.utils.application.Environment;
import shapranv.shell.utils.application.console.ServiceRegistry;
import shapranv.shell.utils.application.module.Module;

@Log4j2
public class RyanairClientModule implements Module {
    private ServiceRegistry serviceRegistry;
    private AirportService airportService;

    @Override
    public void start(Environment env) {
        this.serviceRegistry = env.ensureService(ServiceRegistry.class);

        this.airportService = new AirportService();
        serviceRegistry.register(airportService);
        env.addService(airportService, AirportService.class);

        RouteService routeService = new RouteService(airportService);
        serviceRegistry.register(routeService);
        airportService.addUpdateListener(routeService::onAirportsUpdated);

        /*this.availabilityService = new AvailabilityService(airportService, routeService);
        serviceRegistry.register(availabilityService);
        routeService.addUpdateListener(availabilityService::onRoutesUpdated);
        */

        FlightService flightService = new FlightService(airportService, routeService);
        serviceRegistry.register(flightService);
        routeService.addUpdateListener(flightService::onRoutesUpdated);
    }

    @Override
    public void ready() {
        if (airportService != null) {
            airportService.start();
        }
    }

    @Override
    public void stop() {
        if (serviceRegistry != null) {
            serviceRegistry.stopAll();
        }
    }
}
