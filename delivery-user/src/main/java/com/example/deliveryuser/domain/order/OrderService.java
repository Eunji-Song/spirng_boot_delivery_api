package com.example.deliveryuser.domain.order;

import com.example.deliverycore.entity.Cart;
import com.example.deliverycore.entity.CartItem;
import com.example.deliverycore.entity.Order;
import com.example.deliverycore.entity.OrderItem;
import com.example.deliverycore.enums.OrderStatus;
import com.example.deliveryuser.common.exception.NotFoundException;
import com.example.deliveryuser.common.response.ApiResponse;
import com.example.deliveryuser.common.util.SecurityUtil;
import com.example.deliveryuser.domain.cart.repository.CartRepository;
import com.example.deliveryuser.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CartRepository cartRepository;

    /**
     * 주문 생성
     * 장바구니 값을 받은 후 Order Table에 데이터 복사
     */
    @Transactional
    public Long saveOrder(OrderDto.RequestOrder requestOrder) {
        // cartId 데이터가 존재하는지 확인하기
        Long cartId = requestOrder.getCartId();
        Cart cart = cartRepository.findCartById(cartId);
        if (cart == null) {
            throw new NotFoundException("장바구니가 존재하지 않습니다.");
        }
        List<CartItem> cartItems = cart.getItems();

        Order order = new Order(cart, requestOrder.getAddress());

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(order, cartItem);
            orderItems.add(orderItem);

            order.getItems().add(orderItem);
        }

        orderRepository.save(order);

        Long orderId = order.getId();
        if (orderId != null && orderId > 0) {
            Long memberId = SecurityUtil.getCurrentMemberId();
            // 장바구니 삭제
            cartRepository.delete(cartId, memberId);
        }


        return orderId;
    }

    /**
     * 주문 취소
     */

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findPendingOrderById(orderId);
        if (order == null) {
            throw new NotFoundException("주문 상태를 찾을 수 없습니다.");
        }

        order.setOrderStatus(OrderStatus.CANCELED);
    }

    /**
     * 주문 상세 조회
     */
    public OrderDto.OrderDetail getOrder(Long orderId) {
        OrderDto.OrderDetail orderDetail = orderRepository.findOrderById(orderId);
        if (orderDetail == null) {
            throw new NotFoundException(orderId + " 주문을 찾을 수 없습니다.");
        }

        return orderDetail;
    }


}
