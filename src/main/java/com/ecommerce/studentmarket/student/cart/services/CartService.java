package com.ecommerce.studentmarket.student.cart.services;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import com.ecommerce.studentmarket.product.item.dtos.ProductResponseDto;
import com.ecommerce.studentmarket.product.item.services.ProductService;
import com.ecommerce.studentmarket.student.cart.domains.CartDomain;
import com.ecommerce.studentmarket.student.cart.domains.CartItemDomain;
import com.ecommerce.studentmarket.student.cart.domains.CartItemIdDomain;
import com.ecommerce.studentmarket.student.cart.dtos.CartItemDto;
import com.ecommerce.studentmarket.student.cart.dtos.CartItemIdDto;
import com.ecommerce.studentmarket.student.cart.exceptions.CartItemAlreadyExistsException;
import com.ecommerce.studentmarket.student.cart.exceptions.CartItemNotFoundException;
import com.ecommerce.studentmarket.student.cart.exceptions.CartItemProductOutOfStockException;
import com.ecommerce.studentmarket.student.cart.exceptions.CartItemUnavailableException;
import com.ecommerce.studentmarket.student.cart.repositories.CartItemRepository;
import com.ecommerce.studentmarket.student.cart.repositories.CartRepository;
import com.ecommerce.studentmarket.student.user.repositories.StudentRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private EntityManager entityManager;
//  Tìm giỏ hàng theo mã sinh viên
    @Transactional(readOnly = true)
    public CartDomain getCartByUserId(String mssv){
        CartDomain cart = cartRepository.findByStudent_Mssv(mssv);
//      Bởi vì sản phẩm và cart nằm ở 2 database khác nhau nên phải sử dụng @Transient tạm thời lưu vào memory
//      Do không được map vào database của Sinh viên nên việc mỗi lần lấy sản phẩm ra phải lấy thông tin từ Product mà trả về
        for (CartItemDomain item: cart.getItems()){
//          Lấy sản phẩm từ service
            ProductResponseDto product = productService.getProductResponetById(item.getMaSP());
//          Lưu từng product vào CartItemDomain của danh sách Cart
            item.setSanPham(product);
        }
        return cart;
    }

@Transactional(rollbackFor = Exception.class)
public ApiResponse addToCart(String mssv, CartItemDto cartItemDto) {
    CartDomain cart = getCartByUserId(mssv);

    ProductResponseDto product = productService.getProductResponetById(cartItemDto.getMaSP());

    if (product.getDaAn()) {
        throw new CartItemUnavailableException(product.getTenSP());
    }

    CartItemIdDomain itemId = new CartItemIdDomain(cart.getIdGioHang(), cartItemDto.getMaSP());
    Optional<CartItemDomain> existingItemOpt = cartItemRepository.findById(itemId);

    if (existingItemOpt.isPresent()) {
        throw new CartItemAlreadyExistsException(product.getTenSP());
    }

    // Tạo CartItem mới
    CartItemDomain newItem = cartItemService.createNewCartItem(cart, cartItemDto);

    // Lưu vào database
    cartItemRepository.save(newItem);

    // Cập nhật thông tin sản phẩm cho item mới
    newItem.setSanPham(product);

    // Thêm vào collection trong memory
    cart.getItems().add(newItem);

    return new ApiResponse( "Thêm sản phẩm vào giỏ hàng thành công", true, ApiResponseType.SUCCESS);
}

    // Hàm cập nhật sản phẩm đã có trong giỏ hàng
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse updateCartItem(String mssv, CartItemDto cartItemDto) {
        CartDomain cart = getCartByUserId(mssv);
        boolean cartItem = cartItemRepository.existsByMaSP(cartItemDto.getMaSP());

        if (!cartItem){
            return addToCart(mssv, cartItemDto);
        }

        ProductResponseDto product = productService.getProductResponetById(cartItemDto.getMaSP());

        if (product.getDaAn()) {
            throw new CartItemUnavailableException(product.getTenSP());
        }

        CartItemIdDomain itemId = new CartItemIdDomain(cart.getIdGioHang(), cartItemDto.getMaSP());
        Optional<CartItemDomain> existingItemOpt = cartItemRepository.findById(itemId);

        if (existingItemOpt.isEmpty()) {
            throw new CartItemNotFoundException(itemId);
        }

        // Cập nhật sản phẩm có trong giỏ hàng
        CartItemDomain existingItem = existingItemOpt.get();
        Integer newQuantity = cartItemDto.getSoLuong();
        if (newQuantity == 0) {
            cartItemRepository.delete(existingItem);
            // Xóa khỏi collection trong memory
            cart.getItems().removeIf(item -> item.getMaSP().equals(cartItemDto.getMaSP()));
        } else {
            if (product.getSoLuong() < newQuantity) {
                throw new CartItemProductOutOfStockException(product.getTenSP(), product.getSoLuong());
            }

            existingItem.setSoLuong(newQuantity);
            cartItemRepository.save(existingItem);

            // Cập nhật trong memory
            cart.getItems().stream()
                    .filter(item -> item.getMaSP().equals(cartItemDto.getMaSP()))
                    .findFirst()
                    .ifPresent(item -> {
                        item.setSoLuong(newQuantity);
                        item.setSanPham(product);
                    });
        }

        return new ApiResponse("Cập nhật thành công", true, ApiResponseType.SUCCESS);
    }

    // Xử lý cập nhật sản phẩm đã có trong giỏ hàng
    private void handleExistingItem(CartItemDomain existingItem, CartItemDto cartItemDto, ProductResponseDto product) {
        Integer newQuantity = cartItemDto.getSoLuong();
        if (newQuantity == 0) {
            // Xóa sản phẩm khỏi giỏ hàng
            cartItemRepository.delete(existingItem);
        } else {
            // Kiểm tra tồn kho
            if (product.getSoLuong() < newQuantity) {
                throw new CartItemProductOutOfStockException(product.getTenSP(), product.getSoLuong());
            }

            // Cập nhật số lượng
            existingItem.setSoLuong(newQuantity);
            cartItemRepository.save(existingItem);
//          Hàm này có nhiệm vụ xem xét số lượng sản phẩm cập nhật, nếu số lượng sản phẩm cập nhật là = 0 -> Xóa
//          Nếu số lượng sản phẩm cập nhật lớn hơn số lượng tồn kho còn lại của sản phẩm -> Báo lỗi
//          Nếu thỏa hết thì thay thế số lượng mới trong giỏ hàng
        }
    }
    // Xử lý thêm sản phẩm mới vào giỏ hàng
    private void handleNewItem(CartDomain cart, CartItemDto cartItemDto, ProductResponseDto product) {
        Integer quantity = cartItemDto.getSoLuong();

        // Kiểm tra tồn kho
        if (product.getSoLuong() < quantity) {
            throw new CartItemProductOutOfStockException(product.getTenSP(), product.getSoLuong());
        }

        // Tạo CartItem mới
        CartItemDomain newItem = cartItemService.createNewCartItem(cart, cartItemDto);

        // Lưu CartItem mới
        cartItemRepository.save(newItem);
    }

    public ApiResponse deleteCartItem(CartItemIdDto cartItemIdDto){
        CartItemIdDomain cartItemId = new CartItemIdDomain(cartItemIdDto.getIdGioHang(), cartItemIdDto.getIdSP());
        cartItemRepository.deleteById(cartItemId);
        return new ApiResponse("Xóa thành công", true, ApiResponseType.SUCCESS);
    }

    @Transactional(readOnly = true)
    public Object getCartById(String mssv, CartItemIdDto cartItemId) {

        CartItemIdDomain cartItemIdDomain = new CartItemIdDomain(cartItemId.getIdGioHang(), cartItemId.getIdSP());

        if(!cartItemRepository.existsById(cartItemIdDomain)){
            return false;
        };

        return
         cartItemRepository.findById(cartItemIdDomain).orElseThrow(
                () -> new CartItemNotFoundException(cartItemIdDomain)
        );
    }
}
