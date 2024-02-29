package tn.example.sst.rest;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.example.sst.rest.vm.OrderRequestVM;
import tn.example.sst.rest.vm.OrderResultVM;
import tn.example.sst.rest.exceptions.BadRequestAlertException;
import tn.example.sst.services.impl.IngredientServiceImpl;
import tn.example.sst.services.impl.OrderServiceImpl;
import tn.example.sst.utils.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;


@RestController
@RequestMapping("api")
public class OrderController {
    final Logger log = org.slf4j.LoggerFactory.getLogger(OrderController.class);
    private final OrderServiceImpl orderServiceImpl;
    @Value("${spring.application.name}")
    private String applicationName;

    public OrderController(IngredientServiceImpl ingredientServiceImpl, OrderServiceImpl orderServiceImpl) {
        this.orderServiceImpl = orderServiceImpl;
    }

    @PostMapping("/order")
    public ResponseEntity<OrderResultVM> makeOrder(@RequestBody OrderRequestVM request) throws URISyntaxException {
        log.debug("REST request to make order : {}", request);
        if (request.getOrder() == null || request.getOrder().isEmpty()) {
            throw new BadRequestAlertException("Order failed", "ORDER", "order_failed");
        }
        Optional<OrderResultVM> orderResult = orderServiceImpl.makeOrder(request);
        if (orderResult.isPresent()) {
            return ResponseEntity
                    .created(new URI("/api/order/" + orderResult.get().getOrderId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, "ORDER", orderResult.get().getOrderId().toString()))
                    .body(orderResult.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
