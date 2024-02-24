package tn.example.sst.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import tn.example.sst.broker.KafkaConsumer;
import tn.example.sst.rest.dto.OrderDTO;

import java.security.Principal;

public class KafkaController {
    private static final String PRODUCER_BINDING_NAME = "binding-out-0";

    private final Logger log = LoggerFactory.getLogger(KafkaController.class);
    private final KafkaConsumer kafkaConsumer;
    private final StreamBridge streamBridge;

    public KafkaController(KafkaConsumer kafkaConsumer, StreamBridge streamBridge) {
        this.kafkaConsumer = kafkaConsumer;
        this.streamBridge = streamBridge;
    }

    @PostMapping("/publish")
    public void publish(@RequestBody String message) {
        log.debug("REST request the message : {} to send to Kafka topic ", message);
        streamBridge.send(PRODUCER_BINDING_NAME, message);
    }

    @GetMapping("/register")
    public ResponseBodyEmitter register(Principal principal) {
        return kafkaConsumer.register(principal.getName());
    }

    @GetMapping("/unregister")
    public void unregister(Principal principal) {
        kafkaConsumer.unregister(principal.getName());
    }
}
