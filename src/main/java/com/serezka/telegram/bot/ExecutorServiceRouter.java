package com.serezka.telegram.bot;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class ExecutorServiceRouter {
    List<ExecutorService> services;
    int size;

    @NonFinal
    boolean shutdown = false;

    public boolean isShutdown() {
        return !(!shutdown || !(shutdown = services.stream().allMatch(ExecutorService::isShutdown)));
    }

    public ExecutorServiceRouter(int size) {
        services = new ArrayList<>(size);
        IntStream.range(0, size).mapToObj(i -> Executors.newSingleThreadExecutor()).forEach(services::add);

        this.size = size;

        log.info("created executor service router with {} services", this.size);
    }

    public void route(long id, Runnable runnable) {
        log.info("sent task#{} to executor#{}", id, (int) ((id) % size));
        services.get((int) ((id) % size)).execute(runnable);
    }

    public void shutdown() {
        log.info("shutting down...");
        shutdown = true;
        services.forEach(ExecutorService::shutdown);
        log.info("turned off successfully");
    }

}