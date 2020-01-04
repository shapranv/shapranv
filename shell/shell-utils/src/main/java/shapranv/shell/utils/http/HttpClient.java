package shapranv.shell.utils.http;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.FutureRequestExecutionService;
import org.apache.http.impl.client.HttpClients;
import shapranv.shell.utils.executors.NamedThreadFactory;

import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

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

    public void asyncCall(long requestId, String uri, BiConsumer<Long, String> consumer) {
        HttpGet request = new HttpGet(uri);

        FutureCallback<String> future = new FutureCallback<String>() {
            @Override
            public void completed(String result) {
                log.info("Http request completed: {}", uri);
                log.info("Http response received: {}", result);
                consumer.accept(requestId, result);
            }

            @Override
            public void failed(Exception e) {
                log.error("Can't execute http request: {}", uri, e);
                consumer.accept(requestId, StringUtils.EMPTY);
            }

            @Override
            public void cancelled() {
                log.error("Http request cancelled: {}", uri);
                consumer.accept(requestId, StringUtils.EMPTY);
            }
        };

        executionService.execute(request, HttpClientContext.create(), new BasicResponseHandler(), future);
    }
}
