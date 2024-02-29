package tn.example.sst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.example.sst.domain.OrderItem;

/**
 * Spring Data JPA repository for the Order entity.
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}
