package com.ecommerce.studentmarket.order.services;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import com.ecommerce.studentmarket.order.domains.OrderDomain;
import com.ecommerce.studentmarket.order.domains.OrderItemDomain;
import com.ecommerce.studentmarket.order.domains.OrderStateDomain;
import com.ecommerce.studentmarket.order.domains.SubOrderDomain;
import com.ecommerce.studentmarket.order.dtos.request.SubOrderRequestDto;
import com.ecommerce.studentmarket.order.dtos.response.OrderItemResponseDto;
import com.ecommerce.studentmarket.order.dtos.response.OrderStateResponseDto;
import com.ecommerce.studentmarket.order.dtos.response.SubOrderResponseDto;
import com.ecommerce.studentmarket.order.exceptions.suborder.*;
import com.ecommerce.studentmarket.order.repositories.OrderRepository;
import com.ecommerce.studentmarket.order.repositories.OrderStateRepository;
import com.ecommerce.studentmarket.order.repositories.SubOrderRepository;
import com.ecommerce.studentmarket.product.item.services.ProductService;
import com.ecommerce.studentmarket.student.ewallet.dtos.TransactionRequestDto;
import com.ecommerce.studentmarket.student.ewallet.enums.LoaiGiaoDich;
import com.ecommerce.studentmarket.student.ewallet.enums.TrangThaiGiaoDich;
import com.ecommerce.studentmarket.student.ewallet.services.PaymentService;
import com.ecommerce.studentmarket.student.store.domains.StoreDomain;
import com.ecommerce.studentmarket.student.store.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public Page<SubOrderResponseDto> getAllSubOrders(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SubOrderDomain> subOrderDomains = subOrderRepository.findAll(pageable);

        return subOrderDomains.map(this::convertToSubOrderResponseDto);
    }

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

    @Transactional(rollbackFor = Exception.class)
    public ApiResponse changeOrderStateToXacNhan(Long maDHC){

        SubOrderDomain subOrderDomain = subOrderRepository.findByIdWithItems(maDHC)
                .orElseThrow(() -> new SubOrderNotFoundException(maDHC));


        OrderStateDomain newOrderState = orderStateRepository.findBySubOrder_MaDHC(maDHC);

        newOrderState.setXacNhanTTDH(LocalDateTime.now());

        orderStateRepository.save(newOrderState);
        return new ApiResponse("Cập nhật trạng thái xác nhận thành công.", true, ApiResponseType.SUCCESS);
    }

    public ApiResponse changeOrderStateToDangGiao(Long maDHC){
        OrderStateDomain newOrderState = orderStateRepository.findBySubOrder_MaDHC(maDHC);

        newOrderState.setDangGiaoTTDH(LocalDateTime.now());
        orderStateRepository.save(newOrderState);

        return new ApiResponse("Cập nhật trạng thái đang giao thành công.", true, ApiResponseType.SUCCESS);
    }

    public ApiResponse changeOrderStateToDaGiao(Long maDHC){
        OrderStateDomain newOrderState = orderStateRepository.findBySubOrder_MaDHC(maDHC);

        newOrderState.setDaGiaoTTDH(LocalDateTime.now());
        orderStateRepository.save(newOrderState);

        return new ApiResponse("Cập nhật trạng thái đã giao thành công.", true, ApiResponseType.SUCCESS);
    }

//    Chuyển trạng thái thành đã nhận và lập tức chi trả tiền cho người bán

    public ApiResponse changeOrderStateToDaNhan(Long maDHC){
        OrderStateDomain newOrderState = orderStateRepository.findBySubOrder_MaDHC(maDHC);

        SubOrderDomain subOrder = subOrderRepository.findById(maDHC).orElseThrow(
                () -> new SubOrderNotFoundException(maDHC)
        );

        newOrderState.setDaNhanTTDH(LocalDateTime.now());

        StoreDomain store = storeRepository.findById(subOrder.getMaGianHangDHC()).orElseThrow(
                () -> new SubOrderStoreNotFoundException(subOrder.getMaGianHangDHC())
        );

        TransactionRequestDto transactionRequest = convertToTransactionRequestDto(subOrder, newOrderState.getDaNhanTTDH());

        paymentService.receivePaymentForSeller(store.getStudent().getMssv(), transactionRequest);
        orderStateRepository.save(newOrderState);

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


    public Page<SubOrderResponseDto> getSubOrdersByMaGianHang(Long maGianHangDHC, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<SubOrderDomain> subOrders = subOrderRepository.findByMaGianHangDHC(maGianHangDHC, pageable);

        return subOrders.map(this::convertToSubOrderResponseDto);
    }

    public ApiResponse changeOrderStateToDaHuy(Long maDHC) {
        OrderStateDomain newOrderState = orderStateRepository.findBySubOrder_MaDHC(maDHC);

        if (newOrderState == null) {
            throw new SubOrderNotPendingApprovalException(maDHC);
        }

        if (newOrderState.getChoDuyetTTDH() == null) {
            throw new SubOrderAlreadyProcessedException(maDHC);
        }

        if (newOrderState.getXacNhanTTDH() != null ||
                newOrderState.getDangGiaoTTDH() != null ||
                newOrderState.getDaGiaoTTDH() != null ||
                newOrderState.getDaNhanTTDH() != null ||
                newOrderState.getDaHoanTienTTDH() != null) {
            throw new SubOrderAlreadyProcessedException(maDHC);
        }

        newOrderState.setDaHuyTTDH(LocalDateTime.now());
        orderStateRepository.save(newOrderState);

        return new ApiResponse("Cập nhật trạng thái 'Đã hủy' thành công.", true, ApiResponseType.SUCCESS);
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiResponse changeOrderStateToDaHoanTien(Long maDHC){

        SubOrderDomain subOrderDomain = subOrderRepository.findByIdWithItems(maDHC)
                .orElseThrow(() -> new SubOrderNotFoundException(maDHC));


        OrderStateDomain newOrderState = orderStateRepository.findBySubOrder_MaDHC(maDHC);

        newOrderState.setDaHoanTienTTDH(LocalDateTime.now());

        for (OrderItemDomain item : subOrderDomain.getItems()) {
            productService.increaseNumberOfProduct(item.getMaCTDH().getMaSP(), item.getSoLuong());
        }

        OrderDomain orderDomain = subOrderDomain.getOrderDomain();

        paymentService.refundToBuyer(orderDomain);

        orderStateRepository.save(newOrderState);

        return new ApiResponse("Cập nhật trạng thái đã hoàn tiền thành công.", true, ApiResponseType.SUCCESS);
    }

    public Page<SubOrderResponseDto> getSubOrdersByMaGianHangAndDaNhan(Long maGianHangDHC, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<SubOrderDomain> subOrders = subOrderRepository.findChoDuyetByMaGianHangDHC(maGianHangDHC,pageable);

        return subOrders.map(this::convertToSubOrderResponseDto);
    }

    public Page<SubOrderResponseDto> getSubOrdersByTrangThai(String trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return switch (trangThai) {
            case "CHO_DUYET" -> {
                Page<SubOrderDomain> subOrderDomains = subOrderRepository.findAllChoDuyet(pageable);
                yield subOrderDomains.map(this::convertToSubOrderResponseDto);
            }
            case "XAC_NHAN" -> {
                Page<SubOrderDomain> subOrderDomains = subOrderRepository.findAllXacNhan(pageable);
                yield subOrderDomains.map(this::convertToSubOrderResponseDto);
            }
            case "DANG_GIAO" -> {
                Page<SubOrderDomain> subOrderDomains = subOrderRepository.findAllDangGiao(pageable);
                yield subOrderDomains.map(this::convertToSubOrderResponseDto);
            }
            case "DA_GIAO" -> {
                Page<SubOrderDomain> subOrderDomains = subOrderRepository.findAllDaGiao(pageable);
                yield subOrderDomains.map(this::convertToSubOrderResponseDto);
            }
            case "DA_NHAN" -> {
                Page<SubOrderDomain> subOrderDomains = subOrderRepository.findAllDaNhan(pageable);
                yield subOrderDomains.map(this::convertToSubOrderResponseDto);
            }
            case "DA_HUY" -> {
                Page<SubOrderDomain> subOrderDomains = subOrderRepository.findAllDaHuy(pageable);
                yield subOrderDomains.map(this::convertToSubOrderResponseDto);
            }
            case "DA_HOAN_TIEN" -> {
                Page<SubOrderDomain> subOrderDomains = subOrderRepository.findAllDaHoanTien(pageable);
                yield subOrderDomains.map(this::convertToSubOrderResponseDto);
            }
            default -> throw new SubOrderInvalidStateException(trangThai);
        };
    }

    public SubOrderDomain convertToSubOrderDomain(SubOrderRequestDto subDto, Long maDH) {

        SubOrderDomain subOrderDomain = new SubOrderDomain();

        Optional.ofNullable(subDto.getMaGianHangDHC()).ifPresent(subOrderDomain::setMaGianHangDHC);

        List<OrderItemDomain> orderItemDomains = subDto.getOrderItem().stream().map(itemDto -> {
                OrderItemDomain orderItem = orderItemService.convertToOrderItemDomain(itemDto, maDH);
                orderItem.setSubOrder(subOrderDomain);
                return orderItem;
            }).toList();
        subOrderDomain.setItems(orderItemDomains);

        OrderStateDomain orderState = new OrderStateDomain();

        orderState.setChoDuyetTTDH(LocalDateTime.now());
        orderState.setSubOrder(subOrderDomain);

        subOrderDomain.setOrderState(orderState);

        for (OrderItemDomain item : subOrderDomain.getItems()) {
            productService.decreaseNumberOfProduct(item.getMaCTDH().getMaSP(), item.getSoLuong());
        }

        return subOrderDomain;
    }
}
