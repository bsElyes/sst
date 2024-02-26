package tn.example.sst.rest;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.example.sst.rest.dto.OrderDTO;
import tn.example.sst.rest.dto.OrderRequest;
import tn.example.sst.rest.dto.OrderResult;
import tn.example.sst.rest.exceptions.BadRequestAlertException;
import tn.example.sst.services.IngredientService;
import tn.example.sst.services.OrderService;
import tn.example.sst.utils.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;


@RestController
@RequestMapping("api")
public class OrderController {
    final Logger log = org.slf4j.LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    @Value("${spring.application.name}")
    private String applicationName;

    public OrderController(IngredientService ingredientService, OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public ResponseEntity<OrderResult> makeOrder(@RequestBody OrderRequest request) throws URISyntaxException {
        log.debug("REST request to make order : {}", request);
        if (request.getOrder() == null || request.getOrder().isEmpty()) {
            throw new BadRequestAlertException("Order failed", "ORDER", "order_failed");
        }
        Optional<OrderResult> orderResult = orderService.makeOrder(request);
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
