package shapranv.shell.utils.http;

import lombok.extern.log4j.Log4j2;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.FutureRequestExecutionService;
import org.apache.http.impl.client.HttpClients;
import shapranv.shell.utils.executors.NamedThreadFactory;

import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Log4j2
public class HttpClient {

    private final CloseableHttpClient httpClient;
    private final FutureRequestExecutionService executionService;

    public HttpClient() {
        this.httpClient = HttpClients.createMinimal();
        this.executionService = new FutureRequestExecutionService(
                this.httpClient,
                Executors.newFixedThreadPool(1, new NamedThreadFactory("HttpClient"))
        );
    }

    public void asyncCall(String uri, Consumer<String> consumer) {
        HttpGet request = new HttpGet(uri);

        FutureCallback<String> future = new FutureCallback<String>() {
            @Override
            public void completed(String result) {
                log.info("Http request response: {}", result);
                consumer.accept(result);
            }

            @Override
            public void failed(Exception e) {
                log.error("Can't execute http request: {}", uri, e);
            }

            @Override
            public void cancelled() {
                log.error("Http request cancelled: {}", uri);
            }
        };

        executionService.execute(request, HttpClientContext.create(), new BasicResponseHandler(), future);
    }
}
