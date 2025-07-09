package com.ecommerce.studentmarket.admin.service;

import com.ecommerce.studentmarket.admin.domain.AdminDomain;
import com.ecommerce.studentmarket.admin.dto.AdminDto;
import com.ecommerce.studentmarket.admin.exception.AdminAlreadyExistsException;
import com.ecommerce.studentmarket.admin.exception.AdminNotFoundException;
import com.ecommerce.studentmarket.admin.repositories.AdminRepository;
import com.ecommerce.studentmarket.student.user.exceptions.StudentAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AdminService
{

    @Autowired
    private AdminRepository adminRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    //Thêm tài khoản quản trị
    @Transactional(rollbackFor = AdminAlreadyExistsException.class)
    public AdminDomain createAdmin (AdminDto adminData){
        validateAdminNotExists(adminData);
        return adminRepository.save(convertToAdminDomain(adminData));
    }
    // Tìm tài khoản quản trị theo Mscb
    @Transactional(readOnly = true)
    public AdminDomain getAdminById (String mscb) {
        return adminRepository.findById(mscb).orElseThrow(() -> new AdminNotFoundException(mscb));
    }
    //Cập nhật tài khoản quản trị theo thông tin
    @Transactional(rollbackFor = AdminAlreadyExistsException.class)
    public AdminDomain updateAdminById(String mscb, AdminDto adminData){
        AdminDomain adminDomain = getAdminById(mscb);
        patchAdminFromDto(adminDomain, adminData);
        return adminRepository.save(adminDomain);
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

    //Kiểm tra dữ liệu tồn tại
    private void validateAdminNotExists(AdminDto adminData) throws AdminAlreadyExistsException{
        if (adminRepository.existsById(adminData.getMscb())) {
            throw new AdminAlreadyExistsException(adminData.getMscb());
        }

        if (adminRepository.existsBySdt(adminData.getSdt())) {
            throw new AdminAlreadyExistsException(adminData.getSdt());
        }
    }

    //Chuyển DTO sang Domain
    private AdminDomain convertToAdminDomain(AdminDto adminData){
        AdminDomain data = new AdminDomain();
        data.setMscb(adminData.getMscb());
        data.setPassword(adminData.getPassword());
        data.setHoTen(adminData.getHoTen());
        data.setSdt(adminData.getSdt());

        return data;
    }
    //Chuyển từ dữ liệu từ DTO sang Domain phục vụ cho Patch
    private  void patchAdminFromDto(AdminDomain target, AdminDto dto) throws AdminAlreadyExistsException{
        Optional.ofNullable(dto.getSdt()).ifPresent(sdt -> {
            if (adminRepository.existsBySdtAndMscbNot(sdt, target.getMscb())) {
                throw new AdminAlreadyExistsException(sdt);
            }
            target.setSdt(sdt);
        });
        Optional.ofNullable(dto.getHoTen()).ifPresent(target::setHoTen);

    }

}
