package io.github.junhuhdev.dracarys.jobrunr.dashboard.sse;

import com.sun.net.httpserver.HttpExchange;
import io.github.junhuhdev.dracarys.jobrunr.dashboard.server.sse.SseExchange;
import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.JsonMapper;

import java.io.IOException;

public class AbstractObjectSseExchange extends SseExchange {

    private final JsonMapper jsonMapper;

    public AbstractObjectSseExchange(HttpExchange httpExchange, JsonMapper jsonMapper) throws IOException {
        super(httpExchange);
        this.jsonMapper = jsonMapper;
    }

    public String sendObject(Object object) {
        final String message = jsonMapper.serialize(object);
        sendMessage(message);
        return message;
    }

}
