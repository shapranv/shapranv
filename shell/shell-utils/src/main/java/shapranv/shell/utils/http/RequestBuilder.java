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
    private String requestSuffix;

    private Map<Integer, String> integerToStringCache = CollectionUtils.concurrentMap();
    private Map<Boolean, String> booleanToStringCache = CollectionUtils.concurrentMap();

    @Setter
    private Map<String, String> params = CollectionUtils.concurrentMap();

    public static RequestBuilder of(String host, String function) {
        return new RequestBuilder(host, function);
    }

    public RequestBuilder requestSuffix(String requestSuffix) {
        this.requestSuffix = requestSuffix;
        return this;
    }

    public RequestBuilder addParam(RequestParameter param, String value) {
        return addParam(param.getName(), value);
    }

    public RequestBuilder addParam(RequestParameter param, int value) {
        return addParam(param.getName(), value);
    }

    public RequestBuilder addParam(RequestParameter param, boolean value) {
        return addParam(param.getName(), value);
    }

    public RequestBuilder addParam(String name, String value) {
        params.put(name, StringUtils.defaultString(value));
        return this;
    }

    public RequestBuilder addParam(String name, int value) {
        String stringValue = value > -100 && value < 100
                ? integerToStringCache.computeIfAbsent(value, String::valueOf)
                : String.valueOf(value);
        params.put(name, stringValue);
        return this;
    }

    public RequestBuilder addParam(String name, boolean value) {
        params.put(name, booleanToStringCache.computeIfAbsent(value, String::valueOf));
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

        if (StringUtils.isNotEmpty(requestSuffix)) {
            builder.append(requestSuffix);
        }

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
