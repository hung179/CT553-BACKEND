package com.ecommerce.studentmarket.order.services;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import com.ecommerce.studentmarket.order.domains.OrderDomain;
import com.ecommerce.studentmarket.order.domains.SubOrderDomain;
import com.ecommerce.studentmarket.order.dtos.request.OrderRequestDto;
import com.ecommerce.studentmarket.order.dtos.response.OrderResponseDto;
import com.ecommerce.studentmarket.order.dtos.response.SubOrderResponseDto;
import com.ecommerce.studentmarket.order.exceptions.OrderNotFoundException;
import com.ecommerce.studentmarket.order.repositories.OrderRepository;
import com.ecommerce.studentmarket.student.ewallet.dtos.TransactionRequestDto;
import com.ecommerce.studentmarket.student.ewallet.enums.LoaiGiaoDich;
import com.ecommerce.studentmarket.student.ewallet.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SubOrderService subOrderService;

    @Autowired
    private PaymentService paymentService;


//    Lấy order theo mssv

    public Page<OrderResponseDto> getAllOrderByMssv(String mssv, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);

        Page<OrderDomain> orderDomainPage =  orderRepository.findByMssvDH(mssv, pageable);

        return orderDomainPage.map(this::convertToOrderDtoResponse);
    }

    public OrderResponseDto getOrderById(Long maDH){
        OrderDomain order = orderRepository.findById(maDH).orElseThrow(
                () -> new OrderNotFoundException(maDH)
        );
        return convertToOrderDtoResponse(order);
    }
//Tạo đơn hàng
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse createOrder(OrderRequestDto orderRequestDto){
        OrderDomain orderDomain = convertToOrderDomain(orderRequestDto);

        orderRepository.save(orderDomain);

        return new ApiResponse("Tạo đơn hàng thành công", true, ApiResponseType.SUCCESS);
    }

//   Thanh toán đơn hàng ngay lập tức
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse payTheOrder(OrderRequestDto orderRequestDto){

        OrderDomain orderDomain = convertToOrderDomain(orderRequestDto);

        OrderDomain newOrder = orderRepository.save(orderDomain);

        String chiTietGD = "Thanh toán cho đơn hàng " + newOrder.getMaDH();

        TransactionRequestDto transactionRequest = TransactionRequestDto.builder()
                .soTienGD(newOrder.getTongTienDH())
                .loaiGD(LoaiGiaoDich.THANHTOAN)
                .thoiGianGD(LocalDateTime.now())
                .idDonHangGD(newOrder.getMaDH())
                .chiTietGD(chiTietGD)
                .build();

        paymentService.handlePayTheOrder(newOrder.getMssvDH(), transactionRequest);

        return new ApiResponse("Tạo và thanh toán đơn hàng thành công", true, ApiResponseType.SUCCESS);
    }

    public OrderDomain convertToOrderDomain(OrderRequestDto orderRequestDto){
        OrderDomain orderDomain = new OrderDomain();

        Optional.ofNullable(orderRequestDto.getMssvDH()).ifPresent(orderDomain::setMssvDH);
        Optional.ofNullable(orderRequestDto.getTongTienDH()).ifPresent(orderDomain::setTongTienDH);
        Optional.ofNullable(orderRequestDto.getThanhToan()).ifPresent(orderDomain::setThanhToan);
        Optional.ofNullable(orderRequestDto.getThanhToan()).ifPresent(orderDomain::setThanhToan);

        List<SubOrderDomain> subOrderDomains = orderRequestDto.getSubOrder().stream().map(subDto -> {
            SubOrderDomain sub = subOrderService.convertToSubOrderDomain(subDto);

            sub.setOrderDomain(orderDomain);
            return sub;
        }).toList();

        orderDomain.setSubOrders(subOrderDomains);

        return orderDomain;
    }



    public OrderResponseDto convertToOrderDtoResponse(OrderDomain orderDomain){
        OrderResponseDto orderResponseDto = new OrderResponseDto();

        Optional.ofNullable(orderDomain.getMaDH()).ifPresent(orderResponseDto::setMaDH);
        Optional.ofNullable(orderDomain.getMssvDH()).ifPresent(orderResponseDto::setMssvDH);
        Optional.ofNullable(orderDomain.getThoiGianTaoDH()).ifPresent(orderResponseDto::setNgayTaoDH);
        Optional.ofNullable(orderDomain.getTongTienDH()).ifPresent(orderResponseDto::setTongTienDH);
        Optional.ofNullable(orderDomain.getThanhToan()).ifPresent(orderResponseDto::setThanhToan);
        Optional.ofNullable(orderDomain.getThoiGianThanhToanDH()).ifPresent(orderResponseDto::setNgayThanhToanDH);

        List<SubOrderResponseDto> subOrderResponseDtos = orderDomain.getSubOrders().stream()
                .map(subOrderService::convertToSubOrderResponseDto)
                .toList();

        Optional.of(subOrderResponseDtos).ifPresent(orderResponseDto::setSubOrder);

        return orderResponseDto;

    }

}
