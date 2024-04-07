package org.vaadin.example.broadcaster;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Broadcaster {
    static ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static final List<Consumer<String>> listeners = new CopyOnWriteArrayList<>();

    public static synchronized void register(Consumer<String> listener) {
        listeners.add(listener);
    }

    public static synchronized void unregister(Consumer<String> listener) {
        listeners.remove(listener);
    }

    public static synchronized void broadcast(final String message) {
        for (Consumer<String> listener : listeners) {
            executorService.execute(() -> listener.accept(message));
        }
    }
}

