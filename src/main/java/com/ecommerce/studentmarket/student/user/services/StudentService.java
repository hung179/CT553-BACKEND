package com.ecommerce.studentmarket.student.user.services;

import com.ecommerce.studentmarket.student.cart.domains.CartDomain;
import com.ecommerce.studentmarket.student.user.domains.StudentDomain;
import com.ecommerce.studentmarket.student.user.dtos.StudentRequestDto;
import com.ecommerce.studentmarket.student.user.dtos.StudentResponseDto;
import com.ecommerce.studentmarket.student.user.enums.TrangThai;
import com.ecommerce.studentmarket.student.user.exceptions.StudentAlreadyExistsException;
import com.ecommerce.studentmarket.student.user.exceptions.StudentNotFoundException;
import com.ecommerce.studentmarket.student.user.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //    Thêm tài khoản sinh viên
    @Transactional(rollbackFor = Exception.class)
    public StudentDomain createStudent(StudentRequestDto studentDto) {
        validateStudentNotExists(studentDto);
        StudentDomain student = convertToStudentDomain(studentDto);
        CartDomain cart = new CartDomain();
        cart.setStudent(student);
        student.setCart(cart);
        return studentRepository.save(student);
    }


    //  Truy xuất toàn bộ tài khoản sinh viên
    @Transactional(readOnly = true)
    public List<StudentResponseDto> getAllStudent() {

        List<StudentDomain> studentDomains = studentRepository.findAll();

        List<StudentResponseDto> students = new ArrayList<>();

        for (StudentDomain student : studentDomains){
            students.add(convertToStudentResponseDto(student));
        }

        return students;
    }

    //    Truy xuất tài khoản sinh viên theo MSSV
    @Transactional(readOnly = true)
    public StudentResponseDto getStudentById(String mssv) {
        StudentDomain student = studentRepository.findById(mssv).orElseThrow(() -> new StudentNotFoundException(mssv));
        return convertToStudentResponseDto(student);
    }

    //    Cập nhật thông tin sinh viên
    @Transactional(rollbackFor = Exception.class)
    public StudentResponseDto updateStudent(String mssv, StudentRequestDto studentData) {
        StudentDomain studentDomain = studentRepository.findById(mssv).orElseThrow(() -> new StudentNotFoundException(mssv));;
        patchStudentFromDto(studentDomain, studentData);
        studentRepository.save(studentDomain);
        return getStudentById(mssv);
    }


    //  Thay đổi trạng thái tài khoản sinh viên
    public String changeAccountStatus(String mssv) {
        if (!studentRepository.existsById(mssv)) {
            throw new StudentNotFoundException(mssv);
        }

        StudentDomain studentDomain = studentRepository.findById(mssv).orElseThrow(() -> new StudentNotFoundException(mssv));;

        TrangThai newStatus = (studentDomain.getTrangThai() == TrangThai.HOATDONG)
                ? TrangThai.DINHCHI
                : TrangThai.HOATDONG;

        studentDomain.setTrangThai(newStatus);
        studentRepository.save(studentDomain);

        return "Đổi trạng thái sinh viên thành: " + newStatus.name();
    }

    //Kiểm tra dữ liệu tồn tại
    private void validateStudentNotExists(StudentRequestDto studentData) {
        if (studentRepository.existsById(studentData.getMssv())) {
            throw new StudentAlreadyExistsException(studentData.getMssv());
        }
        if (studentRepository.existsBySdt(studentData.getSdt())) {
            throw new StudentAlreadyExistsException(studentData.getSdt());
        }
    }

    //Chuyển DTO Request sang Domain
    private StudentDomain convertToStudentDomain(StudentRequestDto studentData) {
        StudentDomain data = new StudentDomain();
        Optional.ofNullable(studentData.getMssv()).ifPresent(data::setMssv);
        Optional.ofNullable(studentData.getPassword()).ifPresent(password -> data.setPassword(passwordEncoder.encode(password)));
        Optional.ofNullable(studentData.getHoTen()).ifPresent(data::setHoTen);
        Optional.ofNullable(studentData.getSdt()).ifPresent(data::setSdt);
        Optional.ofNullable(studentData.getLop()).ifPresent(data::setLop);
        Optional.ofNullable(studentData.getKhoa()).ifPresent(data::setKhoa);
        Optional.ofNullable(studentData.getGioiTinh()).ifPresent(data::setGioiTinh);
        Optional.ofNullable(studentData.getNgaySinh()).ifPresent(data::setNgaySinh);

        return data;
    }

//    Chuyển từ Domain sang DTO Response
    private StudentResponseDto convertToStudentResponseDto(StudentDomain studentData){
        StudentResponseDto student = new StudentResponseDto();

        Optional.ofNullable(studentData.getMssv()).ifPresent(student::setMssv);
        Optional.ofNullable(studentData.getHoTen()).ifPresent(student::setHoTen);
        Optional.ofNullable(studentData.getSdt()).ifPresent(student::setSdt);
        Optional.ofNullable(studentData.getLop()).ifPresent(student::setLop);
        Optional.ofNullable(studentData.getKhoa()).ifPresent(student::setKhoa);
        Optional.ofNullable(studentData.getGioiTinh()).ifPresent(student::setGioiTinh);
        Optional.ofNullable(studentData.getTrangThai()).ifPresent(student::setTrangThai);
        Optional.ofNullable(studentData.getRole()).ifPresent(student::setRole);
        Optional.ofNullable(studentData.getNgaySinh()).ifPresent(student::setNgaySinh);

        return student;
    }

    //Chuyển từ dữ liệu từ DTO sang Domain phục vụ cho Patch
    private void patchStudentFromDto(StudentDomain target, StudentRequestDto dto) {
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
