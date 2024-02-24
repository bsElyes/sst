package tn.example.sst.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tn.example.sst.repository.IngredientRepository;
import tn.example.sst.repository.OrderRepository;
import tn.example.sst.rest.dto.OrderDTO;
import tn.example.sst.services.OrderService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

@Component
public class KafkaConsumer implements Consumer<String> {

    private final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    private Map<String, SseEmitter> emitters = new HashMap<>();
    private final OrderService orderService;
    ObjectMapper   objectMapper = new ObjectMapper();

    public KafkaConsumer(OrderService orderService) {
        this.orderService = orderService;
    }


    public SseEmitter register(String key) {
        log.debug("Registering sse client for {}", key);
        SseEmitter emitter = new SseEmitter();
        emitter.onCompletion(() -> emitters.remove(key));
        emitters.put(key, emitter);
        return emitter;
    }

    public void unregister(String key) {
        log.debug("Unregistering sse emitter for: {}", key);
        Optional.ofNullable(emitters.get(key)).ifPresent(SseEmitter::complete);
    }

    @Override
    public void accept(String input) {
        log.debug("Got order from kafka stream: {}", input);
        emitters
            .values()
            .forEach((SseEmitter emitter) -> {
                try {
                    emitter.send(event().data(
                            orderService.makeOrder(objectMapper.convertValue(input,OrderDTO.class)),
                            MediaType.TEXT_PLAIN));
                } catch (IOException e) {
                    log.debug("error during making order message, {}", e.getMessage());
                }
            });
    }
}
