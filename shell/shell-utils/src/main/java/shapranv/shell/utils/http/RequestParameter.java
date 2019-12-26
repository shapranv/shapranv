package shapranv.shell.utils.http;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RequestParameter {
    PHRASE("phrase"),
    MARKET("market");

    @Getter
    private final String name;
}
