package tn.example.sst.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Document(indexName = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "order_date", nullable = false)
    private Date orderDate;

    @Column(name = "total_cost", nullable = false)
    private BigDecimal totalCost = BigDecimal.ZERO;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order(User user) {
        this.user = user;
        this.orderDate = new Date();
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(this);
        }
    }

    public void setTotalCost() {
        BigDecimal totalCost = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            //total cost = all ingredients costs + all ingredients cost * 0.30 (30%) profit margin.
            totalCost = totalCost.add(
                    orderItem.getTotalCost()
                            .add(orderItem.getTotalCost()
                                    .multiply(BigDecimal.valueOf(0.30)))
            );
        }
        this.totalCost = totalCost;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Order order = (Order) o;
        return getOrderId() != null && Objects.equals(getOrderId(), order.getOrderId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
