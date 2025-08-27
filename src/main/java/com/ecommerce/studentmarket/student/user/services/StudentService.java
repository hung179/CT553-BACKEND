package com.ecommerce.studentmarket.student.user.services;

import com.ecommerce.studentmarket.common.apiconfig.ApiResponse;
import com.ecommerce.studentmarket.common.apiconfig.ApiResponseType;
import com.ecommerce.studentmarket.common.cloudinary.domains.ImageDomain;
import com.ecommerce.studentmarket.common.cloudinary.dtos.ImageDto;
import com.ecommerce.studentmarket.common.cloudinary.enums.ChuSoHuu;
import com.ecommerce.studentmarket.common.cloudinary.enums.LoaiAnh;
import com.ecommerce.studentmarket.common.cloudinary.services.ImageService;
import com.ecommerce.studentmarket.student.cart.domains.CartDomain;
import com.ecommerce.studentmarket.student.ewallet.domains.EwalletDomain;
import com.ecommerce.studentmarket.student.store.domains.StoreDomain;
import com.ecommerce.studentmarket.student.user.domains.StudentDomain;
import com.ecommerce.studentmarket.student.user.dtos.StudentRequestDto;
import com.ecommerce.studentmarket.student.user.dtos.StudentResponseDto;
import com.ecommerce.studentmarket.student.user.enums.GioiTinh;
import com.ecommerce.studentmarket.student.user.enums.TrangThai;
import com.ecommerce.studentmarket.student.user.exceptions.StudentAlreadyExistsException;
import com.ecommerce.studentmarket.student.user.exceptions.StudentNotFoundException;
import com.ecommerce.studentmarket.student.user.repositories.StudentRepository;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageService imageService;

    //    Thêm tài khoản sinh viên
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse createStudent(StudentRequestDto studentDto, List<MultipartFile> files) {
        validateStudentNotExists(studentDto);
        StudentDomain student = convertToStudentDomain(studentDto);

        CartDomain cart = new CartDomain();
        cart.setStudent(student);
        student.setCart(cart);

        StoreDomain store = new StoreDomain();
        store.setStudent(student);
        student.setStore(store);

        EwalletDomain ewallet = new EwalletDomain();
        ewallet.setStudent(student);
        student.setWallet(ewallet);


        StudentDomain savedStudent = studentRepository.save(student);
        if(files != null && !files.isEmpty() && studentDto.getImage() != null) {
//            Do idChuSoHuu của image chỉ chấp nhận id kiểu Long nên phải dùng tạm id giỏ hàng :)
                studentDto.getImage().setIdChuSoHuu(savedStudent.getCart().getIdGioHang());
                studentDto.getImage().setChuSoHuu(ChuSoHuu.NGUOIDUNG);
                studentDto.getImage().setLoaiAnh(LoaiAnh.DAIDIEN);
        List<ImageDto> images = new ArrayList<>();
        images.add(studentDto.getImage());
        Long newImageId = null;

        List<ImageDomain> imageDomains = imageService.uploadMultipleImage(files, images);
        for (ImageDomain image: imageDomains){
                newImageId = image.getIdImg();
            }
        if (newImageId != null) {
        savedStudent.setImageId(newImageId);}
        }

        return new ApiResponse("Thêm thành công", true, ApiResponseType.SUCCESS);
    }

    //    Tìm sinh viên theo mã cửa hàng
    @Transactional(readOnly = true)
    public StudentResponseDto getStudentByStoreId(Long maGH) {
            StudentDomain student = studentRepository.findByStore_maGHDT(maGH);
            return convertToStudentResponseDto(student);
    }

    //Kiểm tra sinh viên theo mã gian hàng
    public Boolean checkStudentByStoreId(Long maGH){
        StudentDomain student = studentRepository.findByStore_maGHDT(maGH);
        return student.getTrangThai().equals(TrangThai.HOATDONG);
    }

    //  Truy xuất toàn bộ tài khoản sinh viên
    @Transactional(readOnly = true)
    public Page<StudentResponseDto> getAllStudent(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<StudentDomain> studentDomains = studentRepository.findAll(pageable);

        return studentDomains.map(this::convertToStudentResponseDto);
    }

    //    Truy xuất tài khoản sinh viên theo MSSV
    @Transactional(readOnly = true)
    public StudentResponseDto getStudentById(String mssv) {
        StudentDomain student = studentRepository.findById(mssv).orElseThrow(() -> new StudentNotFoundException(mssv));
        return convertToStudentResponseDto(student);
    }

    //    Cập nhật thông tin sinh viên
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse updateStudent(String mssv, StudentRequestDto studentData, List<MultipartFile> files) {
        StudentDomain studentDomain = studentRepository.findById(mssv).orElseThrow(() -> new StudentNotFoundException(mssv));
        ;
        patchStudentFromDto(studentDomain, studentData);
        StudentDomain student = studentRepository.save(studentDomain);
        if (files != null && !files.isEmpty()) {
//            Kiểm tra ảnh đại diện có bị thay thế

            boolean hasProductAvatarImage = (studentData.getImage().getLoaiAnh() == LoaiAnh.DAIDIEN);
            if (hasProductAvatarImage){
                List<ImageDomain> oldProductAvatarImages = imageService.getImage(
                        student.getCart().getIdGioHang(),
                        ChuSoHuu.NGUOIDUNG,
                        LoaiAnh.DAIDIEN
                );
                for (ImageDomain oldImage: oldProductAvatarImages){
                    imageService.hardDelete(oldImage.getIdImg());
                }
            }
            if (studentData.getImage() != null){
                studentData.getImage().setIdChuSoHuu(student.getCart().getIdGioHang());
                studentData.getImage().setLoaiAnh(LoaiAnh.DAIDIEN);
                studentData.getImage().setChuSoHuu(ChuSoHuu.NGUOIDUNG);
                List<ImageDto> images = new ArrayList<>();
                images.add(studentData.getImage());
                Long newImageId = null;

                List<ImageDomain> imageDomains = imageService.uploadMultipleImage(files, images);
                for (ImageDomain image: imageDomains){
                    newImageId = image.getIdImg();
                }
                if (newImageId != null) {
                    student.setImageId(newImageId);}
            }
        }
        studentRepository.save(student);
        return new ApiResponse("Chỉnh sửa thông tin thành công", true, ApiResponseType.SUCCESS);
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

    public StudentResponseDto getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        StudentDomain student = studentRepository.findById(name).orElseThrow(
                () -> new StudentNotFoundException(name)
        );

        return convertToStudentResponseDto(student);
    }

    public Page<StudentResponseDto> searchStudentByName(String tenSV, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<StudentDomain> studentPage = studentRepository.findByTrangThaiAndHoTenContainingIgnoreCaseOrTrangThaiAndMssvContainingIgnoreCase(TrangThai.HOATDONG, tenSV, TrangThai.HOATDONG, tenSV,pageable);

        return studentPage.map(this::convertToStudentResponseDto);
    }

    // Thêm student bằng file excel
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse createStudentsFromExcel(MultipartFile file) {
        if (file.isEmpty()) {
            return new ApiResponse("File không được để trống", false, ApiResponseType.NOTFOUND);
        }

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            List<String> errors = new ArrayList<>();
            List<String> successList = new ArrayList<>();
            int rowNumber = 0;

            // Skip header row
            if (rows.hasNext()) {
                rows.next();
                rowNumber++;
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                rowNumber++;

                try {
                    StudentRequestDto studentDto = parseRowToStudent(currentRow, rowNumber);

                    // Validate student not exists
                    if (studentRepository.existsById(studentDto.getMssv())) {
                        errors.add("Dòng " + rowNumber + ": MSSV " + studentDto.getMssv() + " đã tồn tại");
                        continue;
                    }

                    if (studentRepository.existsBySdt(studentDto.getSdt())) {
                        errors.add("Dòng " + rowNumber + ": SĐT " + studentDto.getSdt() + " đã tồn tại");
                        continue;
                    }

                    StudentDomain student = convertToStudentDomain(studentDto);

                    // Create related entities
                    CartDomain cart = new CartDomain();
                    cart.setStudent(student);
                    student.setCart(cart);

                    StoreDomain store = new StoreDomain();
                    store.setStudent(student);
                    student.setStore(store);

                    EwalletDomain ewallet = new EwalletDomain();
                    ewallet.setStudent(student);
                    student.setWallet(ewallet);

                    studentRepository.save(student);
                    successList.add("Dòng " + rowNumber + ": " + studentDto.getHoTen() + " (" + studentDto.getMssv() + ")");

                } catch (Exception e) {
                    errors.add("Dòng " + rowNumber + ": " + e.getMessage());
                }
            }

            String message = "Thêm thành công " + successList.size() + " sinh viên";
            if (!errors.isEmpty()) {
                message += ". Có " + errors.size() + " lỗi: " + String.join("; ", errors);
            }

            return new ApiResponse(message, errors.isEmpty(),
                    errors.isEmpty() ? ApiResponseType.SUCCESS : ApiResponseType.NOTFOUND);

        } catch (Exception e) {
            return new ApiResponse("Lỗi đọc file Excel: " + e.getMessage(), false, ApiResponseType.EXISTS);
        }
    }

    private StudentRequestDto parseRowToStudent(Row row, int rowNumber) throws Exception {
        StudentRequestDto student = new StudentRequestDto();

        try {
            // MSSV (Column 0)
            Cell mssvCell = row.getCell(0);
            if (mssvCell == null || mssvCell.getCellType() == CellType.BLANK) {
                throw new Exception("MSSV không được để trống");
            }
            student.setMssv(getCellValueAsString(mssvCell).toUpperCase());

            // Họ tên (Column 1)
            Cell hoTenCell = row.getCell(1);
            if (hoTenCell == null || hoTenCell.getCellType() == CellType.BLANK) {
                throw new Exception("Họ tên không được để trống");
            }
            student.setHoTen(getCellValueAsString(hoTenCell));

            // SĐT (Column 2)
            Cell sdtCell = row.getCell(2);
            if (sdtCell == null || sdtCell.getCellType() == CellType.BLANK) {
                throw new Exception("SĐT không được để trống");
            }
            student.setSdt(getCellValueAsString(sdtCell));

            // Lớp (Column 3)
            Cell lopCell = row.getCell(3);
            if (lopCell != null && lopCell.getCellType() != CellType.BLANK) {
                student.setLop(getCellValueAsString(lopCell).toUpperCase());
            }

            // Khóa (Column 4)
            Cell khoaCell = row.getCell(4);
            if (khoaCell != null && khoaCell.getCellType() != CellType.BLANK) {
                student.setKhoa((int) khoaCell.getNumericCellValue());
            }

            // Giới tính (Column 5)
            Cell gioiTinhCell = row.getCell(5);
            if (gioiTinhCell != null && gioiTinhCell.getCellType() != CellType.BLANK) {
                String gioiTinhStr = getCellValueAsString(gioiTinhCell);
                try {
                    student.setGioiTinh(GioiTinh.fromString(gioiTinhStr));
                } catch (IllegalArgumentException e) {
                    throw new Exception("Giới tính không hợp lệ: " + gioiTinhStr);
                }
            }

            // Ngày sinh (Column 6)
            Cell ngaySinhCell = row.getCell(6);
            if (ngaySinhCell != null && ngaySinhCell.getCellType() != CellType.BLANK) {
                if (ngaySinhCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(ngaySinhCell)) {
                    student.setNgaySinh(ngaySinhCell.getDateCellValue());
                } else {
                    // Try to parse as string
                    String dateStr = getCellValueAsString(ngaySinhCell);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    student.setNgaySinh(sdf.parse(dateStr));
                }
            }

            // Mật khẩu mặc định (Column 7)
            Cell passwordCell = row.getCell(7);
            if(passwordCell == null || passwordCell.getCellType() == CellType.BLANK){
                throw new Exception("Không được để trống mật khẩu sinh viên");
            }
            student.setPassword(getCellValueAsString(passwordCell));
            student.setTrangThai(TrangThai.HOATDONG);

        } catch (Exception e) {
            throw new Exception("Lỗi dữ liệu: " + e.getMessage());
        }

        return student;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
                } else {
                    // Remove decimal for whole numbers
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == Math.floor(numericValue)) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
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
        Optional.ofNullable(studentData.getCart().getIdGioHang()).ifPresent(student::setIdGioHang);
        Optional.ofNullable(studentData.getStore().getMaGHDT()).ifPresent(student::setMaGHDT);
        List<ImageDomain> list = imageService.getImage(
                studentData.getCart().getIdGioHang(),
                ChuSoHuu.NGUOIDUNG,
                LoaiAnh.DAIDIEN
        );

        ImageDomain image = list.stream()
                .filter(img -> img.getLoaiAnh() == LoaiAnh.DAIDIEN)
                .findFirst()
                .orElse(null);

        student.setImage(image);

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

    @Transactional(rollbackFor = Exception.class)
    public ApiResponse changePassword(String mssv, String oldPassword, String newPassword) {
        try {
            // Tìm sinh viên theo MSSV
            StudentDomain student = studentRepository.findById(mssv)
                    .orElseThrow(() -> new StudentNotFoundException(mssv));

            // Kiểm tra mật khẩu cũ có đúng không
            if (!passwordEncoder.matches(oldPassword, student.getPassword())) {
                return new ApiResponse("Mật khẩu cũ không chính xác", false, ApiResponseType.EXISTS);
            }

            // Kiểm tra mật khẩu mới không được giống mật khẩu cũ
            if (passwordEncoder.matches(newPassword, student.getPassword())) {
                return new ApiResponse("Mật khẩu mới không được giống mật khẩu cũ", false, ApiResponseType.EXISTS);
            }

            // Kiểm tra độ dài mật khẩu mới (tùy yêu cầu business)
            if (newPassword == null || newPassword.trim().length() < 6) {
                return new ApiResponse("Mật khẩu mới phải có ít nhất 6 ký tự", false, ApiResponseType.EXISTS);
            }

            // Mã hóa và cập nhật mật khẩu mới
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            student.setPassword(encodedNewPassword);

            // Lưu thay đổi
            studentRepository.save(student);

            return new ApiResponse("Đổi mật khẩu thành công", true, ApiResponseType.SUCCESS);

        } catch (StudentNotFoundException e) {
            return new ApiResponse("Không tìm thấy sinh viên với MSSV: " + mssv, false, ApiResponseType.NOTFOUND);
        } catch (Exception e) {
            return new ApiResponse("Lỗi hệ thống: " + e.getMessage(), false, ApiResponseType.EXISTS);
        }
    }
}
