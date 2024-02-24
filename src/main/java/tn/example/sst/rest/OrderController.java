package tn.example.sst.rest;


import tn.example.sst.repository.IngredientRepository;
import tn.example.sst.services.IngredientService;
import tn.example.sst.services.OrderService;

public class OrderController {
    private final OrderService orderService;

    public OrderController(IngredientService ingredientService, OrderService orderService) {
        this.orderService = orderService;
    }
}
