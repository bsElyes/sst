package tn.example.sst.broker;

import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class KafkaProducer implements Supplier<String> {

    @Override
    public String get() {
        return "sandwich_orders";
    }
}
