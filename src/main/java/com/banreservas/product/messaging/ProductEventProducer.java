package com.banreservas.product.messaging;


import io.smallrye.reactive.messaging.annotations.Broadcast;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class ProductEventProducer {

    @Channel("product-events")
    @Broadcast
    Emitter<String> productEventEmitter;

    public void sendProductEvent(String event) {
        productEventEmitter.send(event);
    }
}

