package shapranv.shell.utils.http;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import shapranv.shell.utils.collections.CollectionUtils;
import shapranv.shell.utils.concurrent.CachedStringBuilder;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
public class RequestBuilder {
    private static final CachedStringBuilder cachedBuilder = CachedStringBuilder.of(256);

    private final String host;
    private final String function;

    @Setter
    private Map<String, String> params = CollectionUtils.concurrentMap();

    public static RequestBuilder of(String host, String function) {
        return new RequestBuilder(host, function);
    }

    public RequestBuilder addParam(RequestParameter param, String value) {
        return addParam(param.getName(), value);
    }

    public RequestBuilder addParam(String name, String value) {
        params.put(name, StringUtils.defaultString(value));
        return this;
    }

    public RequestBuilder clear() {
        params.clear();
        return this;
    }

    public String buildRequest() {
        StringBuilder builder = cachedBuilder.getBuilder()
                .append(host)
                .append(function);

        AtomicBoolean isFirst = new AtomicBoolean(true);
        params.keySet().forEach(param -> {
            if (isFirst.getAndSet(false)) {
                builder.append("?");
            } else {
                builder.append("&");
            }
            builder.append(param);
            builder.append("=");
            builder.append(params.get(param));
        });

        return builder.toString();
    }
}
