package tn.example.sst.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.example.sst.domain.Statistic;

import java.util.Date;
import java.util.List;

/**
 * Spring Data JPA repository for the Statistic entity.
 */
@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    Statistic findByDate(Date date);

    List<Statistic> findByDateBetween(Date start, Date end);
}
