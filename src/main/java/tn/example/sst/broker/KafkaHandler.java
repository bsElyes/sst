package tn.example.sst.broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tn.example.sst.rest.dto.OrderResult;
import tn.example.sst.services.OrderService;
import tn.example.sst.services.StatisticService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class KafkaHandler {

    private final Logger log = LoggerFactory.getLogger(KafkaHandler.class);
    private final OrderService orderService;
    private final StatisticService statisticService;
    private final Map<String, SseEmitter> emitters = new HashMap<>();

    public KafkaHandler(OrderService orderService, StatisticService statisticService) {
        this.orderService = orderService;
        this.statisticService = statisticService;
    }

    @KafkaListener(topics = "sandwich_orders", groupId = "sandwich_orders_group")
    public void listenOrders(OrderResult message) {
        System.out.println("Received Order : " + message);
        statisticService.updateStatistics(message);
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
}
