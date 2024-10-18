package com.example.deliverycore.entity;

import com.example.deliverycore.embeded.Address;
import com.example.deliverycore.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@ToString(exclude = "items")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    // 주문자
    @Column(nullable = false)
    private Long member_id;

    // 주문 매장
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    // 주문 처리 상태
    @Column(nullable = false)
    @ColumnDefault(value = "'PENDING'")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    // 주문 금액 총액
    private int totalPrice;

    // 주소
    @Embedded
    private Address address;

    public Order(Cart cart, Address address) {
        this.member_id = cart.getMember().getId();
        this.store = cart.getStore();
        this.orderStatus = OrderStatus.PENDING;
        this.totalPrice = cart.getTotalPrice();
        this.address = address;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
