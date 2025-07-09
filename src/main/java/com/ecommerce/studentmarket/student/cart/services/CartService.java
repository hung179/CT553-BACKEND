package com.ecommerce.studentmarket.student.cart.services;

import com.ecommerce.studentmarket.product.dtos.ProductDtoRespone;
import com.ecommerce.studentmarket.product.services.ProductService;
import com.ecommerce.studentmarket.student.cart.domains.CartDomain;
import com.ecommerce.studentmarket.student.cart.domains.CartItemDomain;
import com.ecommerce.studentmarket.student.cart.domains.CartItemIdDomain;
import com.ecommerce.studentmarket.student.cart.dtos.CartItemDto;
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
            ProductDtoRespone product = productService.getProductResponetById(item.getMaSP());
//          Lưu từng product vào CartItemDomain của danh sách Cart
            item.setSanPham(product);
        }
        return cart;
    }
//Tìm sản phẩm trong giỏ hàng theo id
    @Transactional(readOnly = true)
    public CartItemDomain findCartItemById(CartItemIdDomain idCartItem){
        return cartItemRepository.findById(idCartItem).orElseThrow(
                () -> {
                    throw new CartItemNotFoundException(idCartItem);
                }
        );
    }

@Transactional(rollbackFor = Exception.class)
public CartDomain addToCart(String mssv, CartItemDto cartItemDto) {
    CartDomain cart = getCartByUserId(mssv);

    ProductDtoRespone product = productService.getProductResponetById(cartItemDto.getMaSP());

    if (product.getDaAn()) {
        throw new CartItemUnavailableException(product.getMaSP());
    }

    CartItemIdDomain itemId = new CartItemIdDomain(cart.getIdGioHang(), cartItemDto.getMaSP());
    Optional<CartItemDomain> existingItemOpt = cartItemRepository.findById(itemId);

    if (existingItemOpt.isPresent()) {
        throw new CartItemAlreadyExistsException(product.getMaSP());
    }

    // Tạo CartItem mới
    CartItemDomain newItem = cartItemService.createNewCartItem(cart, cartItemDto);

    // Lưu vào database
    cartItemRepository.save(newItem);

    // Cập nhật thông tin sản phẩm cho item mới
    newItem.setSanPham(product);

    // Thêm vào collection trong memory
    cart.getItems().add(newItem);

    return cart;

    // Trả về giỏ hàng đã cập nhật
//    return getCartByUserId(mssv);
}

    // Hàm cập nhật sản phẩm đã có trong giỏ hàng
    @Transactional(rollbackFor = Exception.class)
    public CartDomain updateCartItem(String mssv, CartItemDto cartItemDto) {
        CartDomain cart = getCartByUserId(mssv);

        ProductDtoRespone product = productService.getProductResponetById(cartItemDto.getMaSP());

        if (product.getDaAn()) {
            throw new CartItemUnavailableException(product.getMaSP());
        }

        CartItemIdDomain itemId = new CartItemIdDomain(cart.getIdGioHang(), cartItemDto.getMaSP());
        Optional<CartItemDomain> existingItemOpt = cartItemRepository.findById(itemId);

        if (!existingItemOpt.isPresent()) {
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
                throw new CartItemProductOutOfStockException(product.getMaSP(), product.getSoLuong());
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

        return getCartByUserId(mssv);
    }

    // Xử lý cập nhật sản phẩm đã có trong giỏ hàng
    private void handleExistingItem(CartItemDomain existingItem, CartItemDto cartItemDto, ProductDtoRespone product) {
        Integer newQuantity = cartItemDto.getSoLuong();
        if (newQuantity == 0) {
            // Xóa sản phẩm khỏi giỏ hàng
            cartItemRepository.delete(existingItem);
        } else {
            // Kiểm tra tồn kho
            if (product.getSoLuong() < newQuantity) {
                throw new CartItemProductOutOfStockException(product.getMaSP(), product.getSoLuong());
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
    private void handleNewItem(CartDomain cart, CartItemDto cartItemDto, ProductDtoRespone product) {
        Integer quantity = cartItemDto.getSoLuong();

        // Kiểm tra tồn kho
        if (product.getSoLuong() < quantity) {
            throw new CartItemProductOutOfStockException(product.getMaSP(), product.getSoLuong());
        }

        // Tạo CartItem mới
        CartItemDomain newItem = cartItemService.createNewCartItem(cart, cartItemDto);

        // Lưu CartItem mới
        cartItemRepository.save(newItem);
    }
}
