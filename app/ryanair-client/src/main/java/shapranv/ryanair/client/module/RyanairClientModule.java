package shapranv.ryanair.client.module;

import lombok.extern.log4j.Log4j2;
import shapranv.ryanair.client.module.service.AirportService;
import shapranv.ryanair.client.module.service.FlightService;
import shapranv.ryanair.client.module.service.RouteService;
import shapranv.shell.utils.application.Environment;
import shapranv.shell.utils.application.console.ServiceRegistry;
import shapranv.shell.utils.application.module.Module;

@Log4j2
public class RyanairClientModule implements Module {
    private AirportService airportService;
    private RouteService routeService;
    private FlightService flightService;
    //private AvailabilityService availabilityService;

    @Override
    public void start(Environment env) {
        ServiceRegistry serviceRegistry = env.ensureService(ServiceRegistry.class);

        this.airportService = new AirportService();
        serviceRegistry.register(airportService);

        this.routeService = new RouteService(airportService);
        serviceRegistry.register(routeService);
        airportService.addUpdateListener(routeService::onAirportsUpdated);

        /*this.availabilityService = new AvailabilityService(airportService, routeService);
        serviceRegistry.register(availabilityService);
        routeService.addUpdateListener(availabilityService::onRoutesUpdated);
        */

        this.flightService = new FlightService(airportService, routeService);
        serviceRegistry.register(flightService);
        routeService.addUpdateListener(flightService::onRoutesUpdated);

        String request =
        "/api/locate/v1/autocomplete/airports?phrase=&market=en-ie";
        //"/api/locate/v1/autocomplete/routes?arrivalPhrase=&departurePhrase=KRK&market=en-ie";
        //"/api/booking/v4/en-ie/availability?ADT=1&CHD=0&DateIn=2020-01-31&DateOut=2020-01-27&Destination=LIS&INF=0&Origin=KRK&RoundTrip=true&TEEN=0&FlexDaysIn=2&FlexDaysBeforeIn=2&FlexDaysOut=2&FlexDaysBeforeOut=2&ToUs=AGREED&promoCode=&IncludeConnectingFlights=false";
        ///api/booking/v5/en-ie/FareOptions?OutboundFlightKey=FR~1117~%20~~KRK~01/22/2020%2012:25~BFS~01/22/2020%2014:25~~&OutboundFareKey=JGINGYIUZRP4THLFIXMLTFSVCNGBIIAEUYJ2N3LDNDLJ7R246XX7PQAN57NU4IQ6D4KYVWFUHB4KAFCPBPQTUT6TYBBDHC3JDCET3S

    }

    @Override
    public void ready() {
        if (airportService != null) {
            airportService.start();
        }
    }

    @Override
    public void stop() {
        if (flightService != null) {
            //TODO: Move to ServiceRegistry?
            airportService.stop();
            routeService.stop();
            flightService.stop();
            //availabilityService.stop();
        }
    }
}
