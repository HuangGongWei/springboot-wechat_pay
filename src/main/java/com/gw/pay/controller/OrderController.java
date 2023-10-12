package com.gw.pay.controller;

import com.gw.pay.dto.IdDTO;
import com.gw.pay.service.OrderService;
import com.gw.pay.vo.OrderPayVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description: 订单管理
 *
 * @author LinHuiBa-YanAn
 * @date 2023/10/11 21:33
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 创建订单
     * @return
     */
    @PostMapping(value = "/create")
    public ResponseEntity<Long> createOrder() {
        return ResponseEntity.ok(orderService.createOrder());
    }

    /**
     * 订单支付
     * @return
     */
    @PostMapping(value = "/pay")
    public ResponseEntity<OrderPayVO> orderPay(@RequestBody IdDTO idDTO) {
        return ResponseEntity.ok(orderService.orderPay(idDTO.getId()));
    }

}
