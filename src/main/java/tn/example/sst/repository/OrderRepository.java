package tn.example.sst.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.example.sst.domain.Order;

import java.util.Date;
import java.util.List;

/**
 * Spring Data JPA repository for the Order entity.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderDateBetween(Date start, Date end);

    @NotNull
    @Override
    <S extends Order> List<S> saveAll(Iterable<S> entities);
}
