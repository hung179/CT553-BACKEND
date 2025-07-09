package com.ecommerce.studentmarket.product.services;

import com.ecommerce.studentmarket.common.cloudinary.domains.ImageDomain;
import com.ecommerce.studentmarket.common.cloudinary.enums.ChuSoHuu;
import com.ecommerce.studentmarket.common.cloudinary.enums.LoaiAnh;
import com.ecommerce.studentmarket.common.cloudinary.services.ImageService;
import com.ecommerce.studentmarket.product.domains.ProductDomain;
import com.ecommerce.studentmarket.product.dtos.ProductDto;
import com.ecommerce.studentmarket.product.dtos.ProductDtoRespone;
import com.ecommerce.studentmarket.product.exceptions.ProductAlreadyDeletedException;
import com.ecommerce.studentmarket.product.exceptions.ProductNotFoundException;
import com.ecommerce.studentmarket.product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageService imageService;

    //Lấy toàn bộ sản phẩm theo trang và kích cỡ trang
    public Page<ProductDtoRespone> getAllProduct(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDomain> productDomainPage = productRepository.findByDaXoaFalseAndDaAnFalse(pageable);

        return productDomainPage.map(this::convertToProductDtoRespone);
    }

    //   Lấy sản phẩm theo từ khóa tìm kiếm
    public Page<ProductDtoRespone> searchProductByName(String tenSP, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDomain> productDomainPage = productRepository.findByTenSPContainingIgnoreCaseAndDaXoaFalseAndDaAnFalse(tenSP, pageable);
        return productDomainPage.map(this::convertToProductDtoRespone);
    }

    public ProductDomain getProductById(Long maSP) {
        ProductDomain productDomain = productRepository.findByMaSPAndDaXoaFalse(maSP);
//      Nếu có thể hãy thêm ngoại lệ để bắt việc product có thể không tìm thấy
        return productDomain;
    }

    //    Trả về sản phẩm theo mã sản phẩm
    public ProductDtoRespone getProductResponetById(Long maSP) {
        ProductDomain productDomain = productRepository.findById(maSP).orElseThrow(() -> new ProductNotFoundException(maSP));
        return convertToProductDtoRespone(productDomain);
    }

    //Thêm sản phẩm
    @Transactional(rollbackFor = Exception.class)
    public ProductDomain createProduct(ProductDto productDto, List<MultipartFile> files){

        ProductDomain savedProduct = productRepository.save(convertToProductDomain(productDto));

        if(files != null && !files.isEmpty() && productDto.getImages() != null){

            productDto.getImages().forEach(imageDto -> {
                imageDto.setIdChuSoHuu(savedProduct.getMaSP());
                imageDto.setChuSoHuu(ChuSoHuu.SANPHAM);
            });
            List<Long> newImageId = new ArrayList<Long>();
            List<ImageDomain> images = imageService.uploadMultipleImage(files, productDto.getImages());
            for (ImageDomain image: images){
                newImageId.add(image.getIdImg());
            }
            savedProduct.setImageIds(newImageId);
        }

        return savedProduct;
    }

    //    Cập nhật sản phẩm
    @Transactional(rollbackFor = Exception.class)
    public ProductDomain updateProduct(Long maSP, ProductDto productDto, List<MultipartFile> files){
        ProductDomain productDomain = getProductById(maSP);
        if (files != null && !files.isEmpty()){
//            Kiểm tra ảnh đại diện có bị thay thế

            boolean hasProductAvatarImage = productDto.getImages().stream().anyMatch(img -> img.getLoaiAnh() == LoaiAnh.DAIDIEN);
            boolean hasProductDetailImage = productDto.getImages().stream().anyMatch(img -> img.getLoaiAnh() == LoaiAnh.MOTA);

            if (hasProductAvatarImage){
                List<ImageDomain> oldProductAvatarImages = imageService.getImage(
                        maSP,
                        ChuSoHuu.SANPHAM,
                        LoaiAnh.DAIDIEN
                );
            for (ImageDomain oldImage: oldProductAvatarImages){
                imageService.hardDelete(oldImage.getIdImg());
            }
            }
            if (hasProductDetailImage) {
                List<ImageDomain> oldProductDetailImages = imageService.getImage(
                        maSP,
                        ChuSoHuu.SANPHAM,
                        LoaiAnh.MOTA
                );
                for (ImageDomain oldImage: oldProductDetailImages){
                    imageService.hardDelete(oldImage.getIdImg());
                }
            }

//            Upload ảnh mới
            if (productDto.getImages() != null && productDto.getImages().size() == files.size()){
                productDto.getImages().forEach(imageDto -> {
                    imageDto.setIdChuSoHuu(maSP);
                    imageDto.setChuSoHuu(ChuSoHuu.SANPHAM);
                });

                List<ImageDomain> newImage = imageService.uploadMultipleImage(files, productDto.getImages());
                List<Long> newImageId = new ArrayList<Long>();
                for (ImageDomain image: newImage){
                    newImageId.add(image.getIdImg());
                }
                productDomain.setImageIds(newImageId);
            }
        }
        patchProductFromDto(productDomain, productDto);
        return productRepository.save(productDomain);
    }

    //    Ẩn sản phẩm
    public String toggleProductVisibility(Long maSP) {
        ProductDomain productDomain = getProductById(maSP);
        productDomain.setDaAn(!productDomain.getDaAn());
        productRepository.save(productDomain);
        return productDomain.getDaAn() ? "Đã ẩn sản phẩm" : "Đã hiện sản phẩm";
    }

    //    Xóa sản phẩm
    public String deleteProduct(Long maSP){
        ProductDomain productDomain = getProductById(maSP);
        if (productDomain.getDaXoa()){
            throw new ProductAlreadyDeletedException(maSP);
        }

//        Xóa các ảnh liên quan

        List<ImageDomain> productAvatarImage = imageService.getImage(
                maSP,
                ChuSoHuu.SANPHAM,
                LoaiAnh.DAIDIEN
        );

        List<ImageDomain> productDetailImages = imageService.getImage(
                maSP,
                ChuSoHuu.SANPHAM,
                LoaiAnh.MOTA
        );
        productAvatarImage.forEach(image -> imageService.hardDelete(image.getIdImg()));
        productDetailImages.forEach(image -> imageService.hardDelete(image.getIdImg()));
        productDomain.setDaXoa(true);
        productRepository.save(productDomain);
        return "Xóa thành công";
    }

    //    Lấy sản phẩm theo người bán
    public Page<ProductDomain> getProductByStoreId(Long maGHSH, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByMaGHSHAndDaXoaFalse(maGHSH, pageable);
    }
//  Lấy sản phẩm theo người bán không bị ẩn
    public Page<ProductDomain> getProductByStoreIdNotHidden(Long maGHSH, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByMaGHSHAndDaXoaFalseAndDaAnFalse(maGHSH, pageable);
    }

    //Chuyển DTO sang Domain
    private ProductDomain convertToProductDomain(ProductDto productDto) {
        ProductDomain data = new ProductDomain();

        Optional.ofNullable(productDto.getMaGHSH()).ifPresent(data::setMaGHSH);
        Optional.ofNullable(productDto.getTenSP()).ifPresent(data::setTenSP);
        Optional.ofNullable(productDto.getGiaSP()).ifPresent(data::setGiaSP);
        Optional.ofNullable(productDto.getSoLuong()).ifPresent(data::setSoLuong);
        Optional.ofNullable(productDto.getKichThuoc()).ifPresent(data::setKichThuoc);
        Optional.ofNullable(productDto.getTrongLuong()).ifPresent(data::setTrongLuong);
        Optional.ofNullable(productDto.getDaAn()).ifPresent(data::setDaAn);

        return data;
    }

    public void patchProductFromDto(ProductDomain target, ProductDto dto) {
        Optional.ofNullable(dto.getTenSP()).ifPresent(target::setTenSP);
        Optional.ofNullable(dto.getGiaSP()).ifPresent(target::setGiaSP);
        Optional.ofNullable(dto.getSoLuong()).ifPresent(target::setSoLuong);
        Optional.ofNullable(dto.getKichThuoc()).ifPresent(target::setKichThuoc);
        Optional.ofNullable(dto.getTrongLuong()).ifPresent(target::setTrongLuong);
    }

    public ProductDtoRespone convertToProductDtoRespone(ProductDomain productDomain){
        ProductDtoRespone dto = new ProductDtoRespone();

        Optional.of(productDomain.getMaSP()).ifPresent(dto::setMaSP);
        Optional.ofNullable(productDomain.getMaGHSH()).ifPresent(dto::setMaGHSH);
        Optional.ofNullable(productDomain.getTenSP()).ifPresent(dto::setTenSP);
        Optional.ofNullable(productDomain.getGiaSP()).ifPresent(dto::setGiaSP);
        Optional.ofNullable(productDomain.getSoLuong()).ifPresent(dto::setSoLuong);
        Optional.ofNullable(productDomain.getKichThuoc()).ifPresent(dto::setKichThuoc);
        Optional.ofNullable(productDomain.getTrongLuong()).ifPresent(dto::setTrongLuong);
        Optional.ofNullable(productDomain.getDaAn()).ifPresent(dto::setDaAn);
        dto.setImages(imageService.getAllImage(productDomain.getMaSP(), ChuSoHuu.SANPHAM));

        return dto;
    }
}
