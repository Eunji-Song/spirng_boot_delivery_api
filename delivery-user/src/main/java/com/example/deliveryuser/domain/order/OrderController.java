package com.example.deliveryuser.domain.order;

import com.example.deliverycore.embeded.Address;
import com.example.deliverycore.entity.Order;
import com.example.deliveryuser.common.response.ApiResponse;
import com.example.deliveryuser.common.response.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Order - 주문")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /**
     * 주문생성
     */
    @PostMapping("")
    // requestBody
    public ApiResult saveOrder(@Valid @RequestBody OrderDto.RequestOrder requestOrder) {
        log.info("dto : {}", requestOrder.toString());
        Long cartId = requestOrder.getCartId();
        if (cartId == null || cartId < 1) {
            return ApiResponse.error("장바구니 id를 입력해주세요.");
        }

        Long orderId = orderService.saveOrder(requestOrder);
        return ApiResponse.success(orderId);
    }

    /**
     * 주문 취소
     */
    @DeleteMapping("/{orderId}")
    public ApiResult deleteOrder(@PathVariable Long orderId) {
        if (orderId == null || orderId < 1) {
            return ApiResponse.error("올바른 주문 Id 값을 입력해주세요.");
        }

        orderService.deleteOrder(orderId);
        return ApiResponse.success();
    }

    /**
     * 주문 상세 조회
     */
    @GetMapping("/{orderId}")
    public ApiResult getOrder(@PathVariable Long orderId) {
        if (orderId == null || orderId < 1) {
            return ApiResponse.error("올바른 주문 Id 값을 입력해주세요.");
        }

        OrderDto.OrderDetail orderDetail = orderService.getOrder(orderId);
        return ApiResponse.success(orderDetail);
    }
}
