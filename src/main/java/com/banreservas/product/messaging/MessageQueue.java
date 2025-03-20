// src/main/java/com/banreservas/product/messaging/MessageQueue.java
package com.banreservas.product.messaging;

import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class MessageQueue {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageQueue.class);

    @Inject
    @Channel("product-events")
    Emitter<String> emitter;

    public void sendMessage(String message) {
        emitter.send(message);
    }

//    public void publish(String message) {
//        LOGGER.info("Enviando mensaje a Kafka: {}", message);
//        emitter.send(message)
//                .the(result -> { // result is RecordMetadata, not a boolean
//                    if (result != null) {
//                        LOGGER.info("Mensaje enviado correctamente a Kafka.");
//                    } else {
//                        LOGGER.warn("Error al enviar mensaje a Kafka: Resultado es nulo");
//                    }
//                })
//                .exceptionally(throwable -> {
//                    LOGGER.error("Error al enviar mensaje a Kafka");
//                    return null; // Importante manejar la excepción
//                });
//
//    }
}