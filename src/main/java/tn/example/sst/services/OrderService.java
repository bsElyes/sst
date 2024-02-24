package tn.example.sst.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.example.sst.domain.Order;
import tn.example.sst.domain.OrderItem;
import tn.example.sst.repository.IngredientRepository;
import tn.example.sst.repository.OrderRepository;
import tn.example.sst.repository.UserRepository;
import tn.example.sst.rest.dto.OrderDTO;
import tn.example.sst.security.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link tn.example.sst.domain.Order}.
 */
@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, IngredientRepository ingredientRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.ingredientRepository = ingredientRepository;
        this.userRepository = userRepository;
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
     * this should be a transaction once one of the ingredients is out of stock,
     * the operations should be canceled.
     *
     * @param input the required ingredient to make the order.
     */
    @Transactional
    public Optional<OrderDTO> makeOrder(OrderDTO input) {
        return SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findOneByUsername)
                .flatMap(user -> {
                    List<OrderItem> orderItems = new ArrayList<>();
                    Order newOrder = new Order(user);
                    newOrder = orderRepository.save(newOrder);
                    Order finalNewOrder = newOrder;
                    input.getIngredients()
                            .forEach(ingredient -> ingredientRepository.findById(ingredient.getId())
                                    .ifPresentOrElse(ing -> {
                                        if (ing.getAvailableQuantity() - ingredient.getQuantity() < 0) {
                                            throw new RuntimeException("Ingredient out of stock.");
                                        }
                                        ing.setAvailableQuantity(ing.getAvailableQuantity() - ingredient.getQuantity());
                                        OrderItem orderItem = new OrderItem(finalNewOrder, ing,ingredient.getQuantity());
                                        orderItems.add(orderItem);
                                        ingredientRepository.save(ing);
                                    }, () -> {
                                        throw new RuntimeException("Ingredient not found");
                                    }));
                    newOrder.setOrderItems(orderItems);
                    newOrder.setTotalCost();
                    newOrder= orderRepository.save(newOrder);
                    input.setStatus("READY");
                    return Optional.of(input);
                });
    }
}
