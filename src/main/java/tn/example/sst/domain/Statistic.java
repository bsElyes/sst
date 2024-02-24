package tn.example.sst.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "statistics")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statistic_id")
    private Long statisticId;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "best_selling_ingredient")
    private String bestSellingIngredient;

    @Column(name = "number_of_sandwiches_sold")
    private int numberOfSandwichesSold;

    @Column(name = "profit")
    private BigDecimal profit;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Statistic statistic = (Statistic) o;
        return getStatisticId() != null && Objects.equals(getStatisticId(), statistic.getStatisticId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
