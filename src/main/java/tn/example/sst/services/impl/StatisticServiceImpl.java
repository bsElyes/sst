package tn.example.sst.services.impl;


import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tn.example.sst.domain.Order;
import tn.example.sst.domain.Statistic;
import tn.example.sst.repository.OrderRepository;
import tn.example.sst.repository.StatisticRepository;
import tn.example.sst.rest.dto.IngredientDTO;
import tn.example.sst.rest.dto.OrderResult;
import tn.example.sst.rest.dto.SandwichDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Implementation for managing {@link tn.example.sst.domain.Statistic}.
 */
@Service
@Log4j2
public class StatisticServiceImpl {
    private final StatisticRepository statisticRepository;
    private final OrderRepository orderRepository;
    private final Map<LocalDate, Map<Long, Integer>> dailyIngredientReports = new HashMap<>();

    public StatisticServiceImpl(StatisticRepository statisticRepository, OrderRepository orderRepository) {
        this.statisticRepository = statisticRepository;
        this.orderRepository = orderRepository;
    }

    @PostConstruct
    public void warmUpCache() {
        Statistic statistics = getStatisticsForCurrentDay();
        LocalDate currentDate = LocalDate.now();
        LocalDateTime startOfDay = currentDate.atTime(LocalTime.MIN);
        LocalDateTime endOfDay = currentDate.atTime(LocalTime.MAX);
        List<Order> todayOrders = orderRepository.findByOrderDateBetween(
                new Date(startOfDay.toLocalDate().toEpochDay()),
                new Date(endOfDay.toLocalDate().toEpochDay())
        );
    }

    public Statistic getStatisticsForCurrentDay() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startOfDay = currentDate.atStartOfDay().toLocalDate();
        Statistic statistic = statisticRepository.findByDate(new Date(startOfDay.toEpochDay()));
        if (statistic == null) {
            statistic = new Statistic(new Date(startOfDay.toEpochDay()));
            return statisticRepository.save(statistic);
        } else {
            return statistic;
        }
    }

    public List<Statistic> getAllStatisticsBetween(Date start, Date end) {
        return statisticRepository.findByDateBetween(start, end);
    }

    public void generateIngredientReport(OrderResult orderRequest) {
        // Get the current date
        LocalDate now = LocalDate.now();
        LocalDate currentDate = now.atStartOfDay().toLocalDate();
        // Get or create a new daily ingredient report for the current date
        Map<Long, Integer> ingredientReport = dailyIngredientReports
                .computeIfAbsent(currentDate, k -> new HashMap<>());

        // Update the ingredient quantities based on the order
        for (SandwichDTO sandwich : orderRequest.getOrder()) {
            for (IngredientDTO ingredient : sandwich.getIngredients()) {
                Long ingredientId = ingredient.getId();
                Integer quantity = ingredient.getQuantity() * sandwich.getQuantity();
                ingredientReport.put(ingredientId,
                        ingredientReport.getOrDefault(ingredientId, 0) + quantity);
            }
        }
    }

    public Long getBestSellingIngredientForDay(LocalDate date) {
        Map<Long, Integer> ingredientReport = dailyIngredientReports.getOrDefault(date, new HashMap<>());
        if (ingredientReport.isEmpty()) {
            return null; // No data available for the specified date
        }
        Long bestSellingIngredientId = null;
        int maxQuantity = Integer.MIN_VALUE;
        for (Map.Entry<Long, Integer> entry : ingredientReport.entrySet()) {
            if (entry.getValue() > maxQuantity) {
                maxQuantity = entry.getValue();
                bestSellingIngredientId = entry.getKey();
            }
        }
        return bestSellingIngredientId;
    }

    public String getBestSellingIngredientsForDay(Date date) {
        Statistic statistic = statisticRepository.findByDate(date);
        if (statistic == null) {
            throw new IllegalArgumentException("No statistic found for the specified date: " + date);
        }
        return statistic.getBestSellingIngredient();
    }

    public int getNumberOfSandwichesSoldForDay(Date date) {
        Statistic statistic = statisticRepository.findByDate(date);
        if (statistic == null) {
            throw new IllegalArgumentException("No statistic found for the specified date: " + date);
        }
        return statistic.getNumberOfSandwichesSold();
    }

    public BigDecimal getProfitForDay(Date date) {
        Statistic statistic = statisticRepository.findByDate(date);
        if (statistic == null) {
            throw new IllegalArgumentException("No statistic found for the specified date: " + date);
        }
        return statistic.getProfit();
    }

    @Async("taskExecutor")
    @SuppressWarnings("unused")
    public void updateStatistics(OrderResult orderResult) {
        log.debug("Updating statistics for {}", orderResult);
        Statistic statistic = getStatisticsForCurrentDay();
        if (statistic == null) {
            statistic = new Statistic();
        }
        statistic.setDate(new Date());
        statistic.setNumberOfSandwichesSold(statistic.getNumberOfSandwichesSold() + 1);
    }
}
