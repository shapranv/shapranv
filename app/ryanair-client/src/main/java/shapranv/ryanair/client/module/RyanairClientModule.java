package shapranv.ryanair.client.module;

import lombok.extern.log4j.Log4j2;
import shapranv.ryanair.client.module.http.HttpClient;
import shapranv.ryanair.client.module.service.AirportService;
import shapranv.shell.utils.Ticker;
import shapranv.shell.utils.application.Environment;
import shapranv.shell.utils.application.module.Module;

import java.time.Duration;

@Log4j2
public class RyanairClientModule implements Module {
    private Ticker ticker;

    @Override
    public void start(Environment env) {
        AirportService airportService = new AirportService();
        HttpClient httpClient = new HttpClient();
        String host = "https://www.ryanair.com/";
        String request = "/api/locate/v1/autocomplete/airports?phrase=&market=en-ie";
        //"/api/locate/v1/autocomplete/routes?arrivalPhrase=&departurePhrase=KRK&market=en-ie";
        //"/api/booking/v4/en-ie/availability?ADT=1&CHD=0&DateIn=2020-01-31&DateOut=2020-01-27&Destination=LIS&INF=0&Origin=KRK&RoundTrip=true&TEEN=0&FlexDaysIn=2&FlexDaysBeforeIn=2&FlexDaysOut=2&FlexDaysBeforeOut=2&ToUs=AGREED&promoCode=&IncludeConnectingFlights=false";
        ///api/booking/v5/en-ie/FareOptions?OutboundFlightKey=FR~1117~%20~~KRK~01/22/2020%2012:25~BFS~01/22/2020%2014:25~~&OutboundFareKey=JGINGYIUZRP4THLFIXMLTFSVCNGBIIAEUYJ2N3LDNDLJ7R246XX7PQAN57NU4IQ6D4KYVWFUHB4KAFCPBPQTUT6TYBBDHC3JDCET3S

        ticker = new Ticker(Duration.ofSeconds(10),
                () -> {
                    try {
                        httpClient.asyncCall(host + request, airportService::refreshCache);
                    } catch (Exception e) {
                        log.error("Error: ", e);
                    }
                },
                () -> log.info("Ryanair client was stopped."));
    }

    @Override
    public void ready() {
        if (ticker != null) {
            ticker.start();
        }
    }

    @Override
    public void stop() {
        if (ticker != null) {
            ticker.close();
        }
    }
}
