package tn.example.sst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.example.sst.domain.Statistic;

/**
 * Spring Data JPA repository for the Statistic entity.
 */
@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {
}
