package io.github.junhuhdev.dracarys.jobrunr.dashboard.sse;

import com.sun.net.httpserver.HttpExchange;
import io.github.junhuhdev.dracarys.jobrunr.storage.JobStats;
import io.github.junhuhdev.dracarys.jobrunr.storage.StorageProvider;
import io.github.junhuhdev.dracarys.jobrunr.storage.listeners.JobStatsChangeListener;
import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.JsonMapper;

import java.io.IOException;

public class JobStatsSseExchange extends AbstractObjectSseExchange implements JobStatsChangeListener {

    private static String lastMessage;

    private final StorageProvider storageProvider;

    public JobStatsSseExchange(HttpExchange httpExchange, StorageProvider storageProvider, JsonMapper jsonMapper) throws IOException {
        super(httpExchange, jsonMapper);
        this.storageProvider = storageProvider;
        storageProvider.addJobStorageOnChangeListener(this);
        sendMessage(lastMessage);
    }

    @Override
    public void onChange(JobStats jobStats) {
        lastMessage = sendObject(jobStats);
    }

    @Override
    public void close() {
        storageProvider.removeJobStorageOnChangeListener(this);
        super.close();
    }
}
