package shapranv.shell.utils.http;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RequestParameter {
    PHRASE("phrase"),
    ARRIVAL_PHRASE("arrivalPhrase"),
    DEPARTURE_PHRASE("departurePhrase"),
    MARKET("market"),

    ADT("ADT"),
    CHD("CHD"),
    INF("INF"),
    TEEN("TEEN"),
    DATE_IN("DateIn"),
    DATE_OUT("DateOut"),
    ORIGIN("Origin"),
    DESTINATION("Destination"),
    ROUND_TRIP("RoundTrip"),
    FLEX_DAYS_IN("FlexDaysIn"),
    FLEX_DAYS_BEFORE_IN("FlexDaysBeforeIn"),
    FLEX_DAYS_OUT("FlexDaysOut"),
    FLEX_DAYS_BEFORE_OUT("FlexDaysBeforeOut"),
    TO_US("ToUs"),
    PROMO_CODE("promoCode"),
    INCLUDE_CONNECTING_FLIGHTS("IncludeConnectingFlights"),

    INBOUND_MONTH_OF_DATE("inboundMonthOfDate"),
    OUTBOUND_MONTH_OF_DATE("outboundMonthOfDate");

    @Getter
    private final String name;
}
