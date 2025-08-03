package com.ecommerce.studentmarket.product.item.services;

import com.ecommerce.studentmarket.common.cloudinary.domains.ImageDomain;
import com.ecommerce.studentmarket.common.cloudinary.enums.ChuSoHuu;
import com.ecommerce.studentmarket.common.cloudinary.enums.LoaiAnh;
import com.ecommerce.studentmarket.common.cloudinary.services.ImageService;
import com.ecommerce.studentmarket.product.category.dtos.CategoryResponseDto;
import com.ecommerce.studentmarket.product.category.repositories.CategoryRepository;
import com.ecommerce.studentmarket.product.category.services.CategoryService;
import com.ecommerce.studentmarket.product.item.domains.ProductDomain;
import com.ecommerce.studentmarket.product.item.dtos.ProductRequestDto;
import com.ecommerce.studentmarket.product.item.dtos.ProductResponseDto;
import com.ecommerce.studentmarket.product.item.exceptions.ProductAlreadyDeletedException;
import com.ecommerce.studentmarket.product.item.exceptions.ProductNotFoundException;
import com.ecommerce.studentmarket.product.item.repositories.ProductRepository;
import com.ecommerce.studentmarket.student.user.dtos.StudentResponseDto;
import com.ecommerce.studentmarket.student.user.services.StudentService;
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

    @Autowired
    private StudentService studentService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    //Lấy toàn bộ sản phẩm theo trang và kích cỡ trang
    public Page<ProductResponseDto> getAllProduct(Long maGHDT, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDomain> productDomainPage = productRepository.findByDaXoaFalseAndDaAnFalseAndMaGHSHNot(maGHDT, pageable);

        return productDomainPage.map(this::convertToProductDtoRespone);
    }

    //   Lấy sản phẩm theo từ khóa tìm kiếm
    public Page<ProductResponseDto> searchProductByName(Long maGHSH, String tenSP, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDomain> productDomainPage = productRepository.findByTenSPContainingIgnoreCaseAndDaXoaFalseAndDaAnFalseAndMaGHSHNot(tenSP, maGHSH, pageable);
        return productDomainPage.map(this::convertToProductDtoRespone);
    }

//    Lấy sản phẩm theo tên và người sở hữu
    public Page<ProductResponseDto> searchProductByNameAndMaGHSH(Long maGHSH, String tenSP, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDomain> productDomainPage = productRepository.findByTenSPContainingIgnoreCaseAndDaXoaFalseAndMaGHSH(tenSP, maGHSH, pageable);
        return productDomainPage.map(this::convertToProductDtoRespone);
    }

    public ProductDomain getProductById(Long maSP) {
        //      Nếu có thể hãy thêm ngoại lệ để bắt việc product có thể không tìm thấy
        return productRepository.findByMaSPAndDaXoaFalse(maSP);
    }

    //    Trả về sản phẩm theo mã sản phẩm
    public ProductResponseDto getProductResponetById(Long maSP) {
        ProductDomain productDomain = productRepository.findById(maSP).orElseThrow(() -> new ProductNotFoundException(maSP));
        return convertToProductDtoRespone(productDomain);
    }

    //Thêm sản phẩm
    @Transactional(rollbackFor = Exception.class)
    public ProductDomain createProduct(ProductRequestDto productRequestDto, List<MultipartFile> files){

        ProductDomain savedProduct = productRepository.save(convertToProductDomain(productRequestDto));

        if(files != null && !files.isEmpty() && productRequestDto.getImages() != null){

            productRequestDto.getImages().forEach(imageDto -> {
                imageDto.setIdChuSoHuu(savedProduct.getMaSP());
                imageDto.setChuSoHuu(ChuSoHuu.SANPHAM);
            });
            List<Long> newImageId = new ArrayList<Long>();
            List<ImageDomain> images = imageService.uploadMultipleImage(files, productRequestDto.getImages());
            for (ImageDomain image: images){
                newImageId.add(image.getIdImg());
            }
            savedProduct.setImageIds(newImageId);
        }

        return savedProduct;
    }

    //    Cập nhật sản phẩm
    @Transactional(rollbackFor = Exception.class)
    public ProductDomain updateProduct(Long maSP, ProductRequestDto productRequestDto, List<MultipartFile> files){
        ProductDomain productDomain = getProductById(maSP);
        if (files != null && !files.isEmpty()){
//            Kiểm tra ảnh đại diện có bị thay thế

            boolean hasProductAvatarImage = productRequestDto.getImages().stream().anyMatch(img -> img.getLoaiAnh() == LoaiAnh.DAIDIEN);
            boolean hasProductDetailImage = productRequestDto.getImages().stream().anyMatch(img -> img.getLoaiAnh() == LoaiAnh.MOTA);

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
            if (productRequestDto.getImages() != null && productRequestDto.getImages().size() == files.size()){
                productRequestDto.getImages().forEach(imageDto -> {
                    imageDto.setIdChuSoHuu(maSP);
                    imageDto.setChuSoHuu(ChuSoHuu.SANPHAM);
                });

                List<ImageDomain> newImage = imageService.uploadMultipleImage(files, productRequestDto.getImages());
                List<Long> newImageId = new ArrayList<Long>();
                for (ImageDomain image: newImage){
                    newImageId.add(image.getIdImg());
                }
                productDomain.setImageIds(newImageId);
            }
        }
        patchProductFromDto(productDomain, productRequestDto);
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
    public Page<ProductResponseDto> getProductByStoreId(Long maGHSH, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDomain> products = productRepository.findByMaGHSHAndDaXoaFalse(maGHSH, pageable);
        return products.map(this::convertToProductDtoRespone);
    }
//  Lấy sản phẩm theo người bán không bị ẩn
    public Page<ProductResponseDto> getProductByStoreIdNotHidden(Long maGHSH, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDomain> products = productRepository.findByMaGHSHAndDaXoaFalseAndDaAnFalse(maGHSH, pageable);
        return products.map(this::convertToProductDtoRespone);
    }

    //Chuyển DTO sang Domain
    private ProductDomain convertToProductDomain(ProductRequestDto productRequestDto) {
        ProductDomain data = new ProductDomain();

        Optional.ofNullable(productRequestDto.getMaGHSH()).ifPresent(data::setMaGHSH);
        Optional.ofNullable(productRequestDto.getTenSP()).ifPresent(data::setTenSP);
        Optional.ofNullable(productRequestDto.getGiaSP()).ifPresent(data::setGiaSP);
        Optional.ofNullable(productRequestDto.getSoLuong()).ifPresent(data::setSoLuong);
        Optional.ofNullable(productRequestDto.getKichThuoc()).ifPresent(data::setKichThuoc);
        Optional.ofNullable(productRequestDto.getTrongLuong()).ifPresent(data::setTrongLuong);
        Optional.ofNullable(productRequestDto.getDaAn()).ifPresent(data::setDaAn);
        Optional.ofNullable(productRequestDto.getMoTa()).ifPresent(data::setMoTa);
        Optional.ofNullable(productRequestDto.getDanhMuc()).ifPresent(data::setCategory);
        return data;
    }

    public void patchProductFromDto(ProductDomain target, ProductRequestDto dto) {
        Optional.ofNullable(dto.getTenSP()).ifPresent(target::setTenSP);
        Optional.ofNullable(dto.getGiaSP()).ifPresent(target::setGiaSP);
        Optional.ofNullable(dto.getSoLuong()).ifPresent(target::setSoLuong);
        Optional.ofNullable(dto.getKichThuoc()).ifPresent(target::setKichThuoc);
        Optional.ofNullable(dto.getTrongLuong()).ifPresent(target::setTrongLuong);
        Optional.ofNullable(dto.getMoTa()).ifPresent(target::setMoTa);
        Optional.ofNullable(dto.getDanhMuc()).ifPresent(target::setCategory);
        Optional.ofNullable(dto.getDaAn()).ifPresent(target::setDaAn);
    }

    public ProductResponseDto convertToProductDtoRespone(ProductDomain productDomain){
        ProductResponseDto dto = new ProductResponseDto();

        Optional.of(productDomain.getMaSP()).ifPresent(dto::setMaSP);
        Optional.ofNullable(productDomain.getMaGHSH()).ifPresent(dto::setMaGHSH);
        Optional.ofNullable(productDomain.getTenSP()).ifPresent(dto::setTenSP);
        Optional.ofNullable(productDomain.getGiaSP()).ifPresent(dto::setGiaSP);
        Optional.ofNullable(productDomain.getSoLuong()).ifPresent(dto::setSoLuong);
        Optional.ofNullable(productDomain.getKichThuoc()).ifPresent(dto::setKichThuoc);
        Optional.ofNullable(productDomain.getTrongLuong()).ifPresent(dto::setTrongLuong);
        Optional.ofNullable(productDomain.getDaAn()).ifPresent(dto::setDaAn);
        dto.setImages(imageService.getAllImage(productDomain.getMaSP(), ChuSoHuu.SANPHAM));
        Optional.ofNullable(productDomain.getMoTa()).ifPresent(dto::setMoTa);

        Long maGHSH = productDomain.getMaGHSH();
        StudentResponseDto student = studentService.getStudentByStoreId(maGHSH);
        Optional.ofNullable(student.getMssv()).ifPresent(dto::setMssv);
        Optional.ofNullable(student.getHoTen()).ifPresent(dto::setHoTen);

        CategoryResponseDto category = categoryService.getCategoryById(productDomain.getCategory().getMaDM());

        Optional.ofNullable(category).ifPresent(dto::setDanhMuc);


        return dto;
    }
}
