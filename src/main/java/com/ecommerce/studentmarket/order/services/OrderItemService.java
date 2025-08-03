package com.ecommerce.studentmarket.order.services;

import com.ecommerce.studentmarket.order.domains.OrderItemDomain;
import com.ecommerce.studentmarket.order.domains.OrderItemIdDomain;
import com.ecommerce.studentmarket.order.dtos.request.OrderItemIdRequestDto;
import com.ecommerce.studentmarket.order.dtos.request.OrderItemRequestDto;
import com.ecommerce.studentmarket.order.dtos.response.OrderItemIdResponseDto;
import com.ecommerce.studentmarket.order.dtos.response.OrderItemResponseDto;
import com.ecommerce.studentmarket.order.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;



    public OrderItemResponseDto convertToOrderItemResponseDto(OrderItemDomain orderItemDomain){

        OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();

        OrderItemIdResponseDto orderItemId = convertToOrderItemIdResponseDto(orderItemDomain.getMaCTDH());

        Optional.of(orderItemId).ifPresent(orderItemResponseDto::setOrderItemId);
        Optional.ofNullable(orderItemDomain.getGiaSP()).ifPresent(orderItemResponseDto::setGiaSP);

        return orderItemResponseDto;
    }

    private OrderItemIdResponseDto convertToOrderItemIdResponseDto(OrderItemIdDomain orderItemIdDomain){

        OrderItemIdResponseDto orderItemIdResponseDto = new OrderItemIdResponseDto();

        Optional.ofNullable(orderItemIdDomain.getMaDH()).ifPresent(orderItemIdResponseDto::setMaDH);
        Optional.ofNullable(orderItemIdDomain.getMaSP()).ifPresent(orderItemIdResponseDto::setMaSP);

        return orderItemIdResponseDto;
    }

    public OrderItemDomain convertToOrderItemDomain(OrderItemRequestDto itemDto) {

        OrderItemDomain orderItemDomain = new OrderItemDomain();

        Optional.ofNullable(itemDto.getGiaSP()).ifPresent(orderItemDomain::setGiaSP);

        if (itemDto.getOrderItemId() == null) {
            throw new IllegalArgumentException("OrderItemId không được null");
        }
        OrderItemIdDomain orderItemIdDomain = convertToOrderItemIdDomain(itemDto.getOrderItemId());
        orderItemDomain.setMaCTDH(orderItemIdDomain);

        return orderItemDomain;
    }

    public OrderItemIdDomain convertToOrderItemIdDomain(OrderItemIdRequestDto itemId){
        OrderItemIdDomain orderItemIdDomain = new OrderItemIdDomain();

        Optional.ofNullable(itemId.getMaDH()).ifPresent(orderItemIdDomain::setMaDH);
        Optional.ofNullable(itemId.getMaSP()).ifPresent(orderItemIdDomain::setMaSP);

        return orderItemIdDomain;
    }
}
