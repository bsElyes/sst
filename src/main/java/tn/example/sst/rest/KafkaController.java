package tn.example.sst.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import tn.example.sst.services.broker.KafkaHandler;

import java.security.Principal;

public class KafkaController {
    private static final String PRODUCER_BINDING_NAME = "binding-out-0";

    private final Logger log = LoggerFactory.getLogger(KafkaController.class);
    private final KafkaHandler kafkaHandler;
    private final StreamBridge streamBridge;

    public KafkaController(KafkaHandler kafkaHandler, StreamBridge streamBridge) {
        this.kafkaHandler = kafkaHandler;
        this.streamBridge = streamBridge;
    }

    @PostMapping("/publish")
    public void publish(@RequestBody String message) {
        log.debug("REST request the message : {} to send to Kafka topic ", message);
        streamBridge.send(PRODUCER_BINDING_NAME, message);
    }

    @GetMapping("/register")
    public ResponseBodyEmitter register(Principal principal) {
        return kafkaHandler.register(principal.getName());
    }

    @GetMapping("/unregister")
    public void unregister(Principal principal) {
        kafkaHandler.unregister(principal.getName());
    }
}
