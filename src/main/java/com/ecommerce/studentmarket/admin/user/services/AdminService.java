package com.ecommerce.studentmarket.admin.user.services;

import com.ecommerce.studentmarket.admin.systemwallet.domains.SystemWalletDomain;
import com.ecommerce.studentmarket.admin.systemwallet.repositories.SystemWalletRepository;
import com.ecommerce.studentmarket.admin.user.domains.AdminDomain;
import com.ecommerce.studentmarket.admin.user.dtos.AdminRequestDto;
import com.ecommerce.studentmarket.admin.user.dtos.AdminResponseDto;
import com.ecommerce.studentmarket.admin.user.exceptions.AdminAlreadyExistsException;
import com.ecommerce.studentmarket.admin.user.exceptions.AdminNotFoundException;
import com.ecommerce.studentmarket.admin.user.repositories.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class AdminService
{

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SystemWalletRepository systemWalletRepository;

    //Thêm tài khoản quản trị
    @Transactional(rollbackFor = AdminAlreadyExistsException.class)
    public AdminDomain createAdmin (AdminRequestDto adminData){
        validateAdminNotExists(adminData);
        return adminRepository.save(convertToAdminDomain(adminData));
    }
    // Tìm tài khoản quản trị theo Mscb
    @Transactional(readOnly = true)
    public AdminResponseDto getAdminById (String mscb) {
        AdminDomain admin = adminRepository.findById(mscb).orElseThrow(() -> new AdminNotFoundException(mscb));
        return convertToAdminResponseDto(admin);
    }
    //Cập nhật tài khoản quản trị theo thông tin
    @Transactional(rollbackFor = AdminAlreadyExistsException.class)
    public AdminResponseDto updateAdminById(String mscb, AdminRequestDto adminData){
        AdminDomain adminDomain = adminRepository.findById(mscb).orElseThrow(() -> new AdminNotFoundException(mscb));
        patchAdminFromDto(adminDomain, adminData);
        adminRepository.save(adminDomain);
        return getAdminById(mscb);
    }

    //Xóa tài khoản quản trị
    @Transactional(rollbackFor = AdminNotFoundException.class)
    public String deleteAdminById(String mscb){
        if (!adminRepository.existsById(mscb)) {
            throw new AdminNotFoundException(mscb);
        }
        adminRepository.deleteById(mscb);
        return "Xóa thành công";
    }

    public AdminResponseDto getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        AdminDomain admin = adminRepository.findById(name).orElseThrow(
                () -> new AdminNotFoundException(name)
        );

        return convertToAdminResponseDto(admin);
    }

    //Kiểm tra dữ liệu tồn tại
    private void validateAdminNotExists(AdminRequestDto adminData) throws AdminAlreadyExistsException{
        if (adminRepository.existsById(adminData.getMscb())) {
            throw new AdminAlreadyExistsException(adminData.getMscb());
        }

        if (adminRepository.existsBySdt(adminData.getSdt())) {
            throw new AdminAlreadyExistsException(adminData.getSdt());
        }
    }

    //Chuyển DTO sang Domain
    private AdminDomain convertToAdminDomain(AdminRequestDto adminData){
        AdminDomain data = new AdminDomain();
        data.setMscb(adminData.getMscb());
        data.setPassword(passwordEncoder.encode(adminData.getPassword()));
        data.setHoTen(adminData.getHoTen());
        data.setSdt(adminData.getSdt());
        SystemWalletDomain systemWallet = systemWalletRepository.findTopByOrderByMaVHTAsc().orElseThrow();
        data.setSystemWallet(systemWallet);

        return data;
    }

    private AdminResponseDto convertToAdminResponseDto(AdminDomain adminData){
        AdminResponseDto data = new AdminResponseDto();
        Optional.ofNullable(adminData.getMscb()).ifPresent(data::setMscb);
        Optional.ofNullable(adminData.getHoTen()).ifPresent(data::setHoTen);
        Optional.ofNullable(adminData.getSdt()).ifPresent(data::setSdt);
        Optional.ofNullable(adminData.getRole()).ifPresent(data::setRole);

        return data;
    }

    //Chuyển từ dữ liệu từ DTO sang Domain phục vụ cho Patch
    private  void patchAdminFromDto(AdminDomain target, AdminRequestDto dto) throws AdminAlreadyExistsException{
        Optional.ofNullable(dto.getSdt()).ifPresent(sdt -> {
            if (adminRepository.existsBySdtAndMscbNot(sdt, target.getMscb())) {
                throw new AdminAlreadyExistsException(sdt);
            }
            target.setSdt(sdt);
        });
        Optional.ofNullable(dto.getHoTen()).ifPresent(target::setHoTen);
    }
}
