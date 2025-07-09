package com.ecommerce.studentmarket.student.user.services;

import com.ecommerce.studentmarket.student.cart.domains.CartDomain;
import com.ecommerce.studentmarket.student.user.domains.StudentDomain;
import com.ecommerce.studentmarket.student.user.dtos.StudentDto;
import com.ecommerce.studentmarket.student.user.enums.TrangThai;
import com.ecommerce.studentmarket.student.user.exceptions.StudentAlreadyExistsException;
import com.ecommerce.studentmarket.student.user.exceptions.StudentNotFoundException;
import com.ecommerce.studentmarket.student.user.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    //    Thêm tài khoản sinh viên
    @Transactional(rollbackFor = Exception.class)
    public StudentDomain createStudent(StudentDto studentDto) {
        validateStudentNotExists(studentDto);
        StudentDomain student = convertToStudentDomain(studentDto);
        CartDomain cart = new CartDomain();
        cart.setStudent(student);
        student.setCart(cart);
        return studentRepository.save(student);
    }


    //  Truy xuất toàn bộ tài khoản sinh viên
    @Transactional(readOnly = true)
    public List<StudentDomain> getAllStudent() {
        return studentRepository.findAll();
    }

    //    Truy xuất tài khoản sinh viên theo MSSV
    @Transactional(readOnly = true)
    public StudentDomain getStudentById(String mssv) {
        return studentRepository.findById(mssv).orElseThrow(() -> new StudentNotFoundException(mssv));
    }

    //    Cập nhật thông tin sinh viên
    @Transactional(rollbackFor = Exception.class)
    public StudentDomain updateStudent(String mssv, StudentDto studentData) {
        StudentDomain studentDomain = getStudentById(mssv);
        patchStudentFromDto(studentDomain, studentData);
        return studentRepository.save(studentDomain);
    }


    //  Thay đổi trạng thái tài khoản sinh viên
    public String changeAccountStatus(String mssv) {
        if (!studentRepository.existsById(mssv)) {
            throw new StudentNotFoundException(mssv);
        }

        StudentDomain studentDomain = getStudentById(mssv);

        TrangThai newStatus = (studentDomain.getTrangThai() == TrangThai.HOATDONG)
                ? TrangThai.DINHCHI
                : TrangThai.HOATDONG;

        studentDomain.setTrangThai(newStatus);
        studentRepository.save(studentDomain);

        return "Đổi trạng thái sinh viên thành: " + newStatus.name();
    }

    //Kiểm tra dữ liệu tồn tại
    private void validateStudentNotExists(StudentDto studentData) {
        if (studentRepository.existsById(studentData.getMssv())) {
            throw new StudentAlreadyExistsException(studentData.getMssv());
        }
        if (studentRepository.existsBySdt(studentData.getSdt())) {
            throw new StudentAlreadyExistsException(studentData.getSdt());
        }
    }

    //Chuyển DTO sang Domain
    private StudentDomain convertToStudentDomain(StudentDto studentData) {
        StudentDomain data = new StudentDomain();
        Optional.ofNullable(studentData.getMssv()).ifPresent(data::setMssv);
        Optional.ofNullable(studentData.getPassword()).ifPresent(data::setPassword);
        Optional.ofNullable(studentData.getHoTen()).ifPresent(data::setHoTen);
        Optional.ofNullable(studentData.getSdt()).ifPresent(data::setSdt);
        Optional.ofNullable(studentData.getLop()).ifPresent(data::setLop);
        Optional.ofNullable(studentData.getKhoa()).ifPresent(data::setKhoa);
        Optional.ofNullable(studentData.getGioiTinh()).ifPresent(data::setGioiTinh);
        Optional.ofNullable(studentData.getNgaySinh()).ifPresent(data::setNgaySinh);

        return data;
    }

    //Chuyển từ dữ liệu từ DTO sang Domain phục vụ cho Patch
    private void patchStudentFromDto(StudentDomain target, StudentDto dto) {
        Optional.ofNullable(dto.getHoTen()).ifPresent(target::setHoTen);
        Optional.ofNullable(dto.getSdt()).ifPresent(sdt -> {
            if (studentRepository.existsBySdtAndMssvNot(sdt, target.getMssv())) {
                throw new StudentAlreadyExistsException(sdt);
            }
            target.setSdt(sdt);
        });
        Optional.ofNullable(dto.getLop()).ifPresent(target::setLop);
        Optional.ofNullable(dto.getKhoa()).ifPresent(target::setKhoa);
        Optional.ofNullable(dto.getGioiTinh()).ifPresent(target::setGioiTinh);
        Optional.ofNullable(dto.getNgaySinh()).ifPresent(target::setNgaySinh);
    }
}
