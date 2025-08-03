package com.ecommerce.studentmarket.student.address.services;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import com.ecommerce.studentmarket.student.address.domains.AddressDomain;
import com.ecommerce.studentmarket.student.address.dtos.AddressRequestDto;
import com.ecommerce.studentmarket.student.address.dtos.AddressResponseDto;
import com.ecommerce.studentmarket.student.address.repositories.AddressRepository;
import com.ecommerce.studentmarket.student.user.domains.StudentDomain;
import com.ecommerce.studentmarket.student.user.dtos.StudentResponseDto;
import com.ecommerce.studentmarket.student.user.repositories.StudentRepository;
import com.ecommerce.studentmarket.student.user.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StudentRepository studentRepository;
//Tìm địa chỉ theo id
    public AddressResponseDto getAddressById(Long id){
        AddressDomain address = addressRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Không tìm thấy địa chỉ")
        );
        return convertToAddressResponseDto(address);
    }
//Lấy tất cả địa chỉ
    public Page<AddressResponseDto> getAllAddress(String mssv, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<AddressDomain> address = addressRepository.findByStudent_Mssv(mssv, pageable);
        return address.map(this::convertToAddressResponseDto);
    }
//Thêm địa chỉ
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse createAddress(String mssv ,AddressRequestDto addressRequestDto){

        Long count = addressRepository.countByStudent_Mssv(mssv);
        if (count >= 5) {
            throw new IllegalStateException("Sinh viên chỉ được sở hữu tối đa 5 địa chỉ.");
        }

        AddressDomain newAddress = convertToAddressDomain(addressRequestDto);
        if(Boolean.TRUE.equals(addressRequestDto.getMacDinhDC())) {
            List<AddressDomain> addresses = addressRepository.findByStudent_Mssv(mssv);
            addresses.forEach(address -> address.setMacDinhDC(false));
            addressRepository.saveAll(addresses);
        }

        StudentDomain student = studentRepository.findById(mssv).orElseThrow(
                () -> new RuntimeException("Không tìm thấy sinh viên")
        );

        newAddress.setStudent(student);

        addressRepository.save(newAddress);



        return new ApiResponse("Tạo địa chỉ mới thành công", true, ApiResponseType.SUCCESS);
    }

//    Cập nhật địa chỉ
@Transactional(rollbackFor = Exception.class)
public ApiResponse patchAddress(String mssv, Long maDC ,AddressRequestDto addressRequestDto) {

    AddressDomain addressDomain = addressRepository.findById(maDC)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ mới"));

    patchAddressFromDto(addressDomain, addressRequestDto);

    List<AddressDomain> addresses = addressRepository.findByStudent_Mssv(mssv);

    if (addressRequestDto.getMacDinhDC()) {
        addresses.forEach(address -> {address.setMacDinhDC(false);
        });
        addressRepository.saveAll(addresses);
    }

    addressDomain.setMacDinhDC(addressRequestDto.getMacDinhDC());
    addressRepository.save(addressDomain);


    // Kiểm tra nếu tất cả địa chỉ đều không phải mặc định thì chọn phần tử đầu tiên
    if (!Boolean.TRUE.equals(addressDomain.getMacDinhDC())) {
        // Nếu địa chỉ hiện tại không phải mặc định → kiểm tra có địa chỉ mặc định nào không
        boolean hasDefault = addressRepository.findByStudent_Mssv(mssv)
                .stream()
                .anyMatch(AddressDomain::getMacDinhDC);

        if (!hasDefault) {
            List<AddressDomain> allAddresses = addressRepository.findByStudent_Mssv(mssv);
            if (!allAddresses.isEmpty()) {
                AddressDomain first = allAddresses.getFirst();
                first.setMacDinhDC(true);
                addressRepository.save(first);
            }
        }
    }

    return new ApiResponse("Cập nhật địa chỉ mới thành công", true, ApiResponseType.SUCCESS);
}


//  Xóa địa chỉ
@Transactional
public ApiResponse deleteAddress(String mssv, Long maDC) {

    AddressDomain addressDomain = addressRepository.findById(maDC)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ cần xóa"));

    if (!addressDomain.getStudent().getMssv().equals(mssv)) {
        throw new RuntimeException("Địa chỉ không thuộc sinh viên này");
    }

    // Lấy danh sách các địa chỉ còn lại (ngoại trừ cái sắp xóa)
    List<AddressDomain> otherAddresses = addressRepository.findByStudent_Mssv(mssv).stream()
            .filter(a -> !a.getMaDC().equals(maDC))
            .toList();

    // Nếu đang xóa địa chỉ mặc định và còn địa chỉ khác
    if (Boolean.TRUE.equals(addressDomain.getMacDinhDC()) && !otherAddresses.isEmpty()) {
        AddressDomain newDefault = otherAddresses.getFirst();
        newDefault.setMacDinhDC(true);
        addressRepository.save(newDefault);
    }

    addressRepository.deleteById(maDC);

    return new ApiResponse("Xóa địa chỉ thành công", true, ApiResponseType.SUCCESS);
}



    private void patchAddressFromDto(AddressDomain addressDomain, AddressRequestDto addressRequestDto ) {

        Optional.ofNullable(addressRequestDto.getTinhDC()).ifPresent(addressDomain::setTinhDC);
        Optional.ofNullable(addressRequestDto.getHuyenDC()).ifPresent(addressDomain::setHuyenDC);
        Optional.ofNullable(addressRequestDto.getXaDC()).ifPresent(addressDomain::setXaDC);
        Optional.ofNullable(addressRequestDto.getChiTietDC()).ifPresent(addressDomain::setChiTietDC);

    }

    private AddressDomain convertToAddressDomain(AddressRequestDto addressRequestDto ) {
        AddressDomain addressDomain = new AddressDomain();

        Optional.ofNullable(addressRequestDto.getTinhDC()).ifPresent(addressDomain::setTinhDC);
        Optional.ofNullable(addressRequestDto.getHuyenDC()).ifPresent(addressDomain::setHuyenDC);
        Optional.ofNullable(addressRequestDto.getXaDC()).ifPresent(addressDomain::setXaDC);
        Optional.ofNullable(addressRequestDto.getChiTietDC()).ifPresent(addressDomain::setChiTietDC);
        Optional.ofNullable(addressRequestDto.getMacDinhDC()).ifPresent(addressDomain::setMacDinhDC);

        return addressDomain;
    }

    private AddressResponseDto convertToAddressResponseDto(AddressDomain addressDomain ) {
        AddressResponseDto addressResponseDto = new AddressResponseDto();

        Optional.ofNullable(addressDomain.getMaDC()).ifPresent(addressResponseDto::setMaDC);
        Optional.ofNullable(addressDomain.getTinhDC()).ifPresent(addressResponseDto::setTinhDC);
        Optional.ofNullable(addressDomain.getHuyenDC()).ifPresent(addressResponseDto::setHuyenDC);
        Optional.ofNullable(addressDomain.getXaDC()).ifPresent(addressResponseDto::setXaDC);
        Optional.ofNullable(addressDomain.getChiTietDC()).ifPresent(addressResponseDto::setChiTietDC);
        Optional.ofNullable(addressDomain.getMacDinhDC()).ifPresent(addressResponseDto::setMacDinhDC);

        return addressResponseDto;
    }
}
