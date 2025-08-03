package com.ecommerce.studentmarket.student.store.services;

import com.ecommerce.studentmarket.product.item.dtos.ProductResponseDto;
import com.ecommerce.studentmarket.product.item.services.ProductService;
import com.ecommerce.studentmarket.student.store.domains.StoreDomain;
import com.ecommerce.studentmarket.student.store.dtos.StoreDtoRequest;
import com.ecommerce.studentmarket.student.store.dtos.StoreDtoResponse;
import com.ecommerce.studentmarket.student.store.exceptions.StoreNotFoundException;
import com.ecommerce.studentmarket.student.store.repositories.StoreRepository;
import com.ecommerce.studentmarket.student.user.enums.TrangThai;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductService productService;
//  Lấy thông tin và các sản phẩm cửa hàng (Dành cho chủ cửa hàng)
    public StoreDtoResponse getStoreByMshAndProductNotHidden(String msh, Integer page, Integer size){
        StoreDomain storeDomain = storeRepository.findByStudent_MssvAndStudent_TrangThai(msh, TrangThai.HOATDONG);

        if (storeDomain == null){
            throw new StoreNotFoundException(msh);
        }

        Page<ProductResponseDto> products = productService.getProductByStoreId(storeDomain.getMaGHDT(), page, size);

        return convertDomainToDtoResponse(storeDomain, products);

    }
//  Lấy thông tin và các sản phẩm cửa hàng (Dành cho khách xem)
    public StoreDtoResponse getStoreByMsh(String msh, Integer page, Integer size){
        StoreDomain storeDomain = storeRepository.findByStudent_MssvAndStudent_TrangThai(msh, TrangThai.HOATDONG);

        if (storeDomain == null){
            throw new StoreNotFoundException(msh);
        }

        Page<ProductResponseDto> products = productService.getProductByStoreIdNotHidden(storeDomain.getMaGHDT(), page, size);

        return convertDomainToDtoResponse(storeDomain, products);
    }

    public String updateStore(String msh, StoreDtoRequest storeDtoRequest){
        StoreDomain storeDomain = storeRepository.findByStudent_MssvAndStudent_TrangThai(msh, TrangThai.HOATDONG);

        if (storeDomain == null){
            throw new StoreNotFoundException(msh);
        }

        storeDomain.setMoTaGHDT(storeDtoRequest.getMoTa());
        storeRepository.save(storeDomain);

        return "Cập nhật thành công";
    }

    private StoreDtoResponse convertDomainToDtoResponse(StoreDomain storeDomain, Page<ProductResponseDto> products){

        StoreDtoResponse store = new StoreDtoResponse();

        Optional.ofNullable(storeDomain.getMaGHDT()).ifPresent(store::setMaGHDT);
        Optional.ofNullable(storeDomain.getStudent().getMssv()).ifPresent(store::setMsh);
        Optional.ofNullable(storeDomain.getMoTaGHDT()).ifPresent(store::setMoTa);
        Optional.ofNullable(storeDomain.getStudent().getHoTen()).ifPresent(store::setHoTenSH);
        Optional.ofNullable(storeDomain.getStudent().getSdt()).ifPresent(store::setSdt);
        Optional.ofNullable(products).ifPresent(store::setProducts);

        return store;
    }

}
