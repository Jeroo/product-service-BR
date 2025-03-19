package com.banreservas.product.messaging;

import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MessageQueue {
    @Inject
    @Channel("product-events-q")
    Emitter<String> emitter;

    public void publish(String message) {
        emitter.send(message);
    }
}
