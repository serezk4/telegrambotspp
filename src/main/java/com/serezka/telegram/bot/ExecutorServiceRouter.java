package com.serezka.telegram.bot;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExecutorServiceRouter {
    List<ExecutorService> services;
    int size;

    public ExecutorServiceRouter(int size) {
        services = new ArrayList<>(size);
        IntStream.range(0, size).mapToObj(i -> Executors.newSingleThreadExecutor()).forEach(services::add);

        this.size = size;
    }

    public void route(long id, Runnable r) {
        services.get((int) ((id) % size)).execute(r);
    }

    public void shutdown() {
        for (ExecutorService service : services)
            service.shutdown();
    }

}