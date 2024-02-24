package tn.example.sst.broker;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import tn.example.sst.rest.dto.OrderDTO;

import java.util.ArrayList;
import java.util.function.Supplier;

@Component
public class KafkaProducer implements Supplier<String> {
    Logger  logger = org.slf4j.LoggerFactory.getLogger(KafkaProducer.class);
    @Override
    public String get() {
        return "order_producer";
    }
}
