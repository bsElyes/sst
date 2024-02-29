package tn.example.sst.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.example.sst.config.KafkaConfiguration;
import tn.example.sst.domain.Ingredient;
import tn.example.sst.domain.Order;
import tn.example.sst.domain.OrderItem;
import tn.example.sst.repository.IngredientRepository;
import tn.example.sst.repository.OrderItemRepository;
import tn.example.sst.repository.OrderRepository;
import tn.example.sst.repository.UserRepository;
import tn.example.sst.services.dto.IngredientDTO;
import tn.example.sst.rest.vm.OrderRequestVM;
import tn.example.sst.rest.vm.OrderResultVM;
import tn.example.sst.services.dto.SandwichDTO;
import tn.example.sst.security.SecurityUtils;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link tn.example.sst.domain.Order}.
 */
@Service
@Transactional
public class OrderServiceImpl {

    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, OrderResultVM> kafkaTemplate;

    public OrderServiceImpl(
            OrderItemRepository orderItemRepository,
            OrderRepository orderRepository,
            IngredientRepository ingredientRepository,
            UserRepository userRepository,
            KafkaTemplate<String, OrderResultVM> kafkaTemplate
    ) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.ingredientRepository = ingredientRepository;
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Save an order.
     *
     * @param order the entity to save.
     * @return the persisted entity.
     */
    public Order save(Order order) {
        log.debug("Request to save Order : {}", order);
        Order result = orderRepository.save(order);
        return result;
    }

    /**
     * Update a order.
     *
     * @param order the entity to save.
     * @return the persisted entity.
     */
    public Order update(Order order) {
        log.debug("Request to update Order : {}", order);
        Order result = orderRepository.save(order);
        return result;
    }

    /**
     * Partially update a order.
     *
     * @param order the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Order> partialUpdate(Order order) {
        log.debug("Request to partially update Order : {}", order);

        return orderRepository
                .findById(order.getOrderId())
                .map(existingOrder -> {
                    if (order.getOrderDate() != null) {
                        existingOrder.setOrderDate(order.getOrderDate());
                    }
                    if (order.getTotalCost() != null) {
                        existingOrder.setTotalCost(order.getTotalCost());
                    }
                    return existingOrder;
                })
                .map(orderRepository::save);
    }

    /**
     * Get all the orders.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        log.debug("Request to get all Orders");
        return orderRepository.findAll();
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Order> findOne(Long id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id);
    }

    /**
     * Delete the order by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }


    /**
     * Make an order.
     * This should be a transaction once one of the ingredients is out of stock,
     * the operations should be canceled.
     *
     * @param request the required ingredient to make the order.
     */
    public Optional<OrderResultVM> makeOrder(OrderRequestVM request) {
        return SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findOneByUsername)
                .flatMap(user -> {
                    Order newOrder = new Order(user);
                    newOrder = orderRepository.save(newOrder);
                    OrderResultVM orderResultVM = new OrderResultVM(request.getOrder());
                    orderResultVM.setOrderId(newOrder.getOrderId());
                    for (SandwichDTO sandwichDTO : request.getOrder()) {
                        for (IngredientDTO ingredient : sandwichDTO.getIngredients()) {
                            Ingredient ing = ingredientRepository.findById(ingredient.getId())
                                    .orElseThrow(() -> new RuntimeException("Ingredient not found"));
                            for (int i = 0; i <= sandwichDTO.getQuantity(); i++) {
                                if (ing.getAvailableQuantity() - ingredient.getQuantity() < 0) {
                                    throw new RuntimeException("Ingredient out of stock.");
                                }
                                ing.setAvailableQuantity(ing.getAvailableQuantity() - ingredient.getQuantity());
                            }
                            newOrder.addOrderItem(orderItemRepository.save(
                                            new OrderItem(
                                                    newOrder,
                                                    ing,
                                                    ingredient.getQuantity() * sandwichDTO.getQuantity()
                                            )
                                    )
                            );
                            ingredientRepository.save(ing);
                        }
                    }
                    newOrder.setTotalCost();
                    orderRepository.save(newOrder);
                    orderResultVM.setStatus("READY");
                    kafkaTemplate.send(KafkaConfiguration.ORDER_TOPIC_NAME_CONFIG, orderResultVM);
                    return Optional.of(orderResultVM);
                });
    }
}
