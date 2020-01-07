package shapranv.shell.utils.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import shapranv.shell.utils.Ticker;
import shapranv.shell.utils.application.config.ConfigService;
import shapranv.shell.utils.application.console.ConsoleCommand;
import shapranv.shell.utils.application.console.ConsoleListener;
import shapranv.shell.utils.http.HttpClient;
import shapranv.shell.utils.http.RequestBuilder;

import java.io.BufferedReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static shapranv.shell.utils.application.console.ConsoleUtils.printCommandInfo;

@Log4j2
public abstract class HttpDataLoader implements Service, ConsoleListener {
    private final String configKey;
    private final HttpClient httpClient;
    private final Ticker ticker;

    private final AtomicBoolean isActive = new AtomicBoolean(false);
    private final AtomicReference<LocalDateTime> lastUpdateTime = new AtomicReference<>(null);

    protected final RequestBuilder requestBuilder;
    protected final ObjectMapper objectMapper;

    private final AtomicLong requestId = new AtomicLong(0);

    public HttpDataLoader(String configKey) {
        ConfigService config = ConfigService.getInstance();
        this.configKey = configKey;
        this.httpClient = new HttpClient();
        this.ticker = new Ticker(this::getTickerPeriod, this::loadData);

        String host = config.get("service." + configKey + ".http.host", config.get("service.default.http.host"));
        String function = config.get("service." + configKey + ".http.function");
        this.requestBuilder = RequestBuilder.of(host, function);

        this.objectMapper = new ObjectMapper();
        this.objectMapper.writerWithDefaultPrettyPrinter();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private Duration getTickerPeriod() {
        ConfigService config = ConfigService.getInstance();
        String refreshPeriod = config.get("service." + configKey + ".refreshPeriod");
        return Duration.parse(refreshPeriod);
    }

    protected void loadData() {
        try {
            long requestId = this.requestId.incrementAndGet();
            String request = getHttpRequest(requestId);

            if (StringUtils.isBlank(request)) {
                log.info("Request is empty. Skipping...");
                return;
            }

            httpClient.asyncCall(requestId, request, (id, response) -> {
                refreshCache(id, response);
                lastUpdateTime.set(LocalDateTime.now());
            });
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }

    //TODO: Upgrade String->Object
    protected abstract String getHttpRequest(long requestId);

    protected abstract void refreshCache(long requestId, String httpResponse);

    protected abstract int getCacheSize();

    @Override
    public void start() {
        if (!isActive.getAndSet(true)) {
            ticker.start();
            log.info("{} started", getName());
        }
    }

    @Override
    public void stop() {
        if (isActive.getAndSet(false)) {
            ticker.close();
            log.info("{} stopped", getName());
        }
    }

    @Override
    public void printStatus(Consumer<String> printer) {
        printer.accept(
                "Service status: active [" + isActive.get() + "], " +
                        "cache size [" + getCacheSize() + "], " +
                        "last updated [" + (lastUpdateTime.get() == null ? "" : lastUpdateTime.get()) + "]"
        );
    }

    @Override
    public void printHelp(Consumer<String> printer) {
        ConsoleListener.super.printHelp(printer);
        printStatus(printer);
    }

    @Override
    public void printMenu(Consumer<String> printer) {
        printCommandInfo(ConsoleCommand.STATUS, printer);
        printCommandInfo(ConsoleCommand.START_SERVICE, printer);
        printCommandInfo(ConsoleCommand.STOP_SERVICE, printer);
        printCommandInfo(ConsoleCommand.SET_PROPERTY, printer);
        printCommandInfo(ConsoleCommand.EXIT, "Quit " + getName(), printer);
    }

    @Override
    public boolean processCommand(BufferedReader console, String code, Consumer<String> printer) throws Exception {
        ConsoleCommand command = ConsoleCommand.findByCode(code);

        switch (command) {
            case STATUS:
                printStatus(printer);
                return true;
            case START_SERVICE:
                start();
                printStatus(printer);
                return true;
            case STOP_SERVICE:
                stop();
                printStatus(printer);
                return true;
            case UNDEF:
                printer.accept("Unknown command...");
                printHelp(printer);
                return true;
        }

        return ConsoleListener.super.processCommand(console, code, printer);
    }
}
