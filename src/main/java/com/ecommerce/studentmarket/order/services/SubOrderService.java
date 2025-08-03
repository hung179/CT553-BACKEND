package com.ecommerce.studentmarket.order.services;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import com.ecommerce.studentmarket.order.domains.OrderItemDomain;
import com.ecommerce.studentmarket.order.domains.OrderStateDomain;
import com.ecommerce.studentmarket.order.domains.SubOrderDomain;
import com.ecommerce.studentmarket.order.dtos.request.SubOrderRequestDto;
import com.ecommerce.studentmarket.order.dtos.response.OrderItemResponseDto;
import com.ecommerce.studentmarket.order.dtos.response.OrderStateResponseDto;
import com.ecommerce.studentmarket.order.dtos.response.SubOrderResponseDto;
import com.ecommerce.studentmarket.order.repositories.OrderStateRepository;
import com.ecommerce.studentmarket.order.repositories.SubOrderRepository;
import com.ecommerce.studentmarket.student.ewallet.dtos.TransactionRequestDto;
import com.ecommerce.studentmarket.student.ewallet.enums.LoaiGiaoDich;
import com.ecommerce.studentmarket.student.ewallet.enums.TrangThaiGiaoDich;
import com.ecommerce.studentmarket.student.ewallet.services.PaymentService;
import com.ecommerce.studentmarket.student.store.domains.StoreDomain;
import com.ecommerce.studentmarket.student.store.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubOrderService {

    @Autowired
    private SubOrderRepository subOrderRepository;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderStateRepository orderStateRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private PaymentService paymentService;

    public SubOrderResponseDto convertToSubOrderResponseDto(SubOrderDomain subOrderDomain){
        SubOrderResponseDto subOrderResponseDto = new SubOrderResponseDto();

        Optional.ofNullable(subOrderDomain.getMaDHC()).ifPresent(subOrderResponseDto::setMaDHC);
        Optional.ofNullable(subOrderDomain.getMaGianHangDHC()).ifPresent(subOrderResponseDto::setMaGianHangDHC);

        List<OrderItemResponseDto> orderItemResponseDtos = subOrderDomain.getItems().stream()
                .map(orderItemService::convertToOrderItemResponseDto)
                .toList();

        Optional.of(orderItemResponseDtos).ifPresent(subOrderResponseDto::setOrderItems);

        OrderStateResponseDto orderStateResponseDto = convertToOrderStateResponseDto(subOrderDomain.getOrderState());

        Optional.of(orderStateResponseDto).ifPresent(subOrderResponseDto::setOrderState);

        return subOrderResponseDto;
    }

    private OrderStateResponseDto convertToOrderStateResponseDto(OrderStateDomain orderStateDomain){

        OrderStateResponseDto orderStateResponseDto = new OrderStateResponseDto();

        Optional.ofNullable(orderStateDomain.getMaTTDH()).ifPresent(orderStateResponseDto::setMaTTDH);
        Optional.ofNullable(orderStateDomain.getChoDuyetTTDH()).ifPresent(orderStateResponseDto::setChoDuyetTTDH);
        Optional.ofNullable(orderStateDomain.getXacNhanTTDH()).ifPresent(orderStateResponseDto::setXacNhanTTDH);
        Optional.ofNullable(orderStateDomain.getDangGiaoTTDH()).ifPresent(orderStateResponseDto::setDangGiaoTTDH);
        Optional.ofNullable(orderStateDomain.getDaHuyTTDH()).ifPresent(orderStateResponseDto::setDaHuyTTDH);
        Optional.ofNullable(orderStateDomain.getDaHoanTienTTDH()).ifPresent(orderStateResponseDto::setDaHoanTienTTDH);
        Optional.ofNullable(orderStateDomain.getDaGiaoTTDH()).ifPresent(orderStateResponseDto::setDaGiaoTTDH);

        return orderStateResponseDto;

    }

    public ApiResponse changeOrderStateToXacNhan(Long maDHC){
        OrderStateDomain newOrderState = orderStateRepository.findBySubOrder_MaDHC(maDHC);

        newOrderState.setXacNhanTTDH(LocalDateTime.now());

        return new ApiResponse("Cập nhật trạng thái xác nhận thành công.", true, ApiResponseType.SUCCESS);
    }

    public ApiResponse changeOrderStateToDangGiao(Long maDHC){
        OrderStateDomain newOrderState = orderStateRepository.findBySubOrder_MaDHC(maDHC);

        newOrderState.setDangGiaoTTDH(LocalDateTime.now());

        return new ApiResponse("Cập nhật trạng thái đang giao thành công.", true, ApiResponseType.SUCCESS);
    }

    public ApiResponse changeOrderStateToDaGiao(Long maDHC){
        OrderStateDomain newOrderState = orderStateRepository.findBySubOrder_MaDHC(maDHC);

        newOrderState.setDaGiaoTTDH(LocalDateTime.now());

        return new ApiResponse("Cập nhật trạng thái đã giao thành công.", true, ApiResponseType.SUCCESS);
    }

//    Chuyển trạng thái thành đã nhận và lập tức chi trả tiền cho người bán

    public ApiResponse changeOrderStateToDaNhan(Long maDHC){
        OrderStateDomain newOrderState = orderStateRepository.findBySubOrder_MaDHC(maDHC);

        SubOrderDomain subOrder = subOrderRepository.findById(maDHC).orElseThrow(
                () -> new RuntimeException("Không tìm thấy đơn hàng con")
        );

        newOrderState.setDaNhanTTDH(LocalDateTime.now());

        StoreDomain store = storeRepository.findById(subOrder.getMaGianHangDHC()).orElseThrow(
                () -> new RuntimeException("Không tìm thấy gian hàng")
        );

        TransactionRequestDto transactionRequest = convertToTransactionRequestDto(subOrder, newOrderState.getDaNhanTTDH());

        paymentService.receivePaymentForSeller(store.getStudent().getMssv(), transactionRequest);

        return new ApiResponse("Cập nhật trạng thái đã hủy thành công.", true, ApiResponseType.SUCCESS);
    }

    public TransactionRequestDto convertToTransactionRequestDto(SubOrderDomain subOrder, LocalDateTime dateTime){

        TransactionRequestDto transactionRequest = new TransactionRequestDto();

        Optional.ofNullable(calculateTotalItemPrice(subOrder.getItems())).ifPresent(transactionRequest::setSoTienGD);

        Optional.of(LoaiGiaoDich.THANHTOAN).ifPresent(transactionRequest::setLoaiGD);
        Optional.of(dateTime).ifPresent(transactionRequest::setThoiGianGD);
        Optional.of(TrangThaiGiaoDich.DATHANHTOAN).ifPresent(transactionRequest::setTrangThaiGD);
        Optional.of(subOrder.getMaDHC()).ifPresent(transactionRequest::setIdDonHangGD);

        String chiTiet = "Thanh toán cho đơn hàng" + subOrder.getMaDHC();

        transactionRequest.setChiTietGD(chiTiet);

        return transactionRequest;
    }

//    Hàm lấy giá của các sản phẩm
public BigDecimal calculateTotalItemPrice(List<OrderItemDomain> items) {
    if (items == null || items.isEmpty()) return BigDecimal.ZERO;

    return items.stream()
            .map(item -> item.getGiaSP().multiply(BigDecimal.valueOf(item.getSoLuong())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
}

    public ApiResponse changeOrderStateToDaHuy(Long maDHC) {
        OrderStateDomain newOrderState = orderStateRepository.findBySubOrder_MaDHC(maDHC);

        if (newOrderState == null) {
            throw new RuntimeException("Không tìm thấy trạng thái đơn hàng với mã đơn hàng con: " + maDHC);
        }

        if (newOrderState.getChoDuyetTTDH() == null) {
            throw new RuntimeException("Đơn hàng không ở trạng thái 'Chờ duyệt', không thể hủy.");
        }

        if (newOrderState.getXacNhanTTDH() != null ||
                newOrderState.getDangGiaoTTDH() != null ||
                newOrderState.getDaGiaoTTDH() != null ||
                newOrderState.getDaNhanTTDH() != null ||
                newOrderState.getDaHoanTienTTDH() != null) {
            throw new RuntimeException("Đơn hàng đã được xử lý, không thể hủy.");
        }

        newOrderState.setDaHuyTTDH(LocalDateTime.now());
        orderStateRepository.save(newOrderState);

        return new ApiResponse("Cập nhật trạng thái 'Đã hủy' thành công.", true, ApiResponseType.SUCCESS);
    }

    public ApiResponse changeOrderStateToDaHoanTien(Long maDHC){
        OrderStateDomain newOrderState = orderStateRepository.findBySubOrder_MaDHC(maDHC);

        newOrderState.setDaHoanTienTTDH(LocalDateTime.now());

        return new ApiResponse("Cập nhật trạng thái đã hoàn tiền thành công.", true, ApiResponseType.SUCCESS);
    }

    public SubOrderDomain convertToSubOrderDomain(SubOrderRequestDto subDto) {

        SubOrderDomain subOrderDomain = new SubOrderDomain();

        Optional.ofNullable(subDto.getMaGianHangDHC()).ifPresent(subOrderDomain::setMaGianHangDHC);

        List<OrderItemDomain> orderItemDomains = subDto.getOrderItem().stream().map(itemDto -> {
                OrderItemDomain orderItem = orderItemService.convertToOrderItemDomain(itemDto);

                orderItem.setSubOrder(subOrderDomain);
                return orderItem;
            }).toList();
        subOrderDomain.setItems(orderItemDomains);

        OrderStateDomain orderState = new OrderStateDomain();

        orderState.setChoDuyetTTDH(LocalDateTime.now());

        subOrderDomain.setOrderState(orderState);

        return subOrderDomain;
    }
}
