package shapranv.shell.utils.http;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RequestParameter {
    PHRASE("phrase"),
    ARRIVAL_PHRASE("arrivalPhrase"),
    DEPARTURE_PHRASE("departurePhrase"),
    MARKET("market");

    @Getter
    private final String name;
}
