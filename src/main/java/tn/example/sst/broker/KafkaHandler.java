package tn.example.sst.broker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tn.example.sst.rest.dto.OrderRequest;
import tn.example.sst.rest.dto.OrderResult;
import tn.example.sst.rest.dto.SandwichDTO;
import tn.example.sst.services.impl.OrderServiceImpl;
import tn.example.sst.services.impl.StatisticServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class KafkaHandler {

    private final Logger log = LoggerFactory.getLogger(KafkaHandler.class);
    private final OrderServiceImpl orderServiceImpl;
    private final StatisticServiceImpl statisticServiceImpl;
    private final Map<String, SseEmitter> emitters = new HashMap<>();

    public KafkaHandler(OrderServiceImpl orderServiceImpl, StatisticServiceImpl statisticServiceImpl) {
        this.orderServiceImpl = orderServiceImpl;
        this.statisticServiceImpl = statisticServiceImpl;
    }

    @KafkaListener(topics = "sandwich_orders", groupId = "sandwich_orders_group")
    public void listenOrdersResult(OrderResult message) {
        System.out.println("Finished Order : " + message);
        statisticServiceImpl.updateStatistics(message);
    }

//    @KafkaListener(topics = "sandwich_request", groupId = "sandwich_orders_group")
//    public void listenOrdersRequest(OrderRequest message) {
//        System.out.println("Received Order : " + message);
//        orderServiceImpl.makeOrder(message);
//    }

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
